# GildedRose 리팩터링 로드맵

## 요약

이 문서는 GildedRose 리팩터링 실습의 진행 상태와 합의된 방향을 기억하기 위한 공유 문서다.
단계별 구현 절차서가 아니며, 각 Step은 코드 변경 전에 별도의 상세 계획을 먼저 수립한다.

로드맵은 `GildedRose_04` Java 프로젝트와 리팩터링 실습 PDF를 기준으로 한다.
목표는 현재의 레거시 `updateQuality()` 구현을 안전하게 리팩터링하여, 읽기 쉽고 확장하기 쉬운 구조로 이동하는 것이다.

## 진행 상태

- 현재 Step: 완료
- 마지막 완료 Step: Step 9
- 다음 행동: 필요 시 후속 개선 계획 수립
- 현재 코드 변경 허용 여부: 아니오

## 상태 추적표

| Step | 이름 | 상태 | 산출물 | 검증 기준 | 비고 |
| --- | --- | --- | --- | --- | --- |
| Step 0 | 레거시 코드 문제점 검토 | Verified | 레거시 코드 4가지 문제점 문서화 | 문제점이 현재 코드와 매핑됨 | `README.md`에 분석 내용 기록 완료 |
| Step 1 | 회귀 테스트 보호막 확인 | Verified | 테스트 기준선과 부족한 커버리지 목록 | `mvn test` 기준선 기록 | `README.md`에 테스트 보호막 기록 완료 |
| Step 2 | 작은 구조 정리 계획 | Verified | 기계적 정리 대상 목록 | 동작 변경 없음 | `items[i]` 로컬 변수 추출 완료 |
| Step 3 | 조건문 단순화 계획 | Verified | 조건문 정리 순서 | Approval 출력 변경 없음 | 부정 조건과 중첩 조건 단순화 완료 |
| Step 4 | 메서드 추출 계획 | Verified | 추출 후보 메서드와 호출 흐름 | 추출 후 테스트 통과 | 타입별 update 메서드와 `updateSellIn()` 추출 완료 |
| Step 5 | 다형성 전환 계획 | Verified | 타입별 클래스 분리 | `mvn test` 통과 | `AgedBrieItem`, `BackstagePassItem`, `SulfurasItem`, `NormalItem` 분리 완료 |
| Step 6 | 타입별 클래스 이전 계획 | Verified | 공통 추상 클래스와 Factory 도입 | `mvn test` 통과 | `GildedRoseItem` 계층과 factory 도입 완료 |
| Step 7 | F&B 확장 계획 | Verified | F&B 요구사항과 테스트, `FoodBeverageItem` | `mvn test` 통과 | `[F&B]` item 확장 완료 |
| Step 8 | 추가 리팩터링 계획 | Verified | 교재 Step 8~12 적용 계획과 정리 결과 | Approval 출력 변경 없음, `mvn test` 통과 | 중복 제거, `update()` 도입, inline 완료 |
| Step 9 | 최종 정리와 회고 | Verified | 최종 구조 요약과 남은 리스크 | `mvn test` 통과 및 상태 갱신 | 실습 종료 |

상태 값:

- `Not Started`: 아직 시작하지 않음
- `Planning`: 상세 계획 수립 중
- `Plan Approved`: 상세 계획 승인됨
- `Implementing`: 구현 중
- `Implemented`: 구현 완료
- `Verified`: 검증 완료
- `Blocked`: 진행 차단됨
- `Skipped`: 의도적으로 생략됨

## 작업 원칙

- 각 Step의 상세 계획이 승인되기 전에는 production code나 test code를 변경하지 않는다.
- Step 계획이 승인되면 해당 Step 기준으로 `README.md`의 To-Do List를 갱신한다.
- 각 Step의 상세 계획을 수립할 때는 `README.md`에 기록된 레거시 코드 문제점 분석을 먼저 참조한다.
- Step 계획에는 해당 Step이 어떤 문제점을 해결하거나 완화하는지 명시한다.
- 현재 Step 범위를 벗어나는 문제점은 억지로 함께 처리하지 않고, 이후 Step의 검토 대상으로 남긴다.
- 구현 후에는 `README.md`의 To-Do List를 기준으로 Step 완료 여부를 검토한다.
- Step 완료 시 `README.md`의 To-Do List와 레거시 코드 문제점 분석을 함께 확인하여 해결, 완화, 보류 상태를 기록한다.
- 완료 기준을 만족하지 못한 To-Do 항목이 있으면 작업을 보완하고 다시 검증한다.
- Step이 정상 완료되면 `README.md`의 To-Do 항목을 완료 상태로 표시한다.
- 동작 보존 Step에서는 Approval Test 출력이 변경되면 안 된다.
- 동작 변경 Step에서는 요구사항, 기대 결과, 승인 여부를 먼저 기록한 뒤 변경한다.
- 새로 추가되는 abstraction은 기존 `Item` 인스턴스를 감싸는 방식으로 만들고 public API를 유지한다.
- `Item` 클래스와 `items` 필드의 public 형태는 변경하지 않는다.
- 특정 Step 진행 중에는 관련 없는 정리 작업을 섞지 않는다.

## 로드맵 Step

### Step 0: 레거시 코드 문제점 검토

- 목표: PDF 3페이지를 출발점으로 현재 `updateQuality()` 구현의 주요 문제 4가지를 문서화한다.
- 산출물: 가독성 문제, Open/Closed Principle 위반, 책임 혼재, 중복과 회귀 위험에 대한 짧은 정리.
- 완료 기준: 문제점이 현재 코드 위치와 함께 문서화된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 1: 회귀 테스트 보호막 확인

- 목표: 구조 리팩터링 전에 현재 동작 기준선을 확정한다.
- 산출물: `mvn test` 결과, Approval Test 기준선 상태, 부족한 경계 테스트 목록.
- 완료 기준: 일반 아이템, Aged Brie, Sulfuras, Backstage passes, Conjured 아이템, 빈 item 배열의 핵심 동작이 확인된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 2: 작은 구조 정리 계획

- 목표: 동작 변경이 없어야 하는 기계적 정리 대상을 식별한다.
- 산출물: 상수, 로컬 변수, 반복적인 item 접근 등 정리 대상 목록.
- 완료 기준: 정리 범위와 검증 기준이 승인된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 3: 조건문 단순화 계획

- 목표: 중첩 조건과 부정 조건을 동작 변경 없이 더 읽기 쉬운 구조로 바꾸는 방법을 계획한다.
- 산출물: 조건문 단순화 순서와 동작 보존 기준.
- 완료 기준: Approval Test diff 없이 작은 단위로 정리할 수 있는 계획이 확정된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 4: 메서드 추출 계획

- 목표: 현재 클래스 안에서 quality 변경 책임과 sellIn 변경 책임을 집중된 메서드로 분리한다.
- 산출물: 추출할 메서드 후보와 예상 호출 흐름.
- 완료 기준: 각 추출을 독립적으로 검증할 수 있는 순서가 명확해진다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 5: 다형성 전환 계획

- 목표: 조건문 중심 구조를 객체 중심 dispatch 구조로 안전하게 전환하는 방법을 계획한다.
- 산출물: 추상화 방식, factory 접근, 타입별 이전 전략.
- 완료 기준: 기존 conditional branch를 한 번에 제거하지 않고 타입별로 검증하며 옮기는 전략이 확정된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 6: 타입별 클래스 이전 계획

- 목표: 기존 동작을 타입별 객체로 점진적으로 이동한다.
- 산출물: Normal, Aged Brie, Backstage, Sulfuras, Conjured 동작의 이전 순서와 검증 기준.
- 완료 기준: 기존 동작을 보존하면서 `GildedRose.updateQuality()`가 orchestration 중심으로 축소될 수 있다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 7: F&B 확장 계획

- 목표: 리팩터링된 구조를 이용해 신규 F&B item type을 확장 실습으로 계획한다.
- 산출물: F&B 요구사항, characterization test, 확장 방식.
- 완료 기준: 기존 동작 변경을 최소화하면서 확장 지점을 통해 새 동작을 추가할 수 있는 계획이 확정된다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 8: 추가 리팩터링 계획

- 목표: 교재 24page의 Step 8~12 추가 리팩터링을 현재 코드 상태에 맞게 적용한다.
- 산출물: 변수 선언 분리, 중복 제거, 메서드 추출, factory 정리, inline 적용 결과.
- 완료 기준: 동작 변경 없이 Approval Test 출력이 유지되고 `mvn test`가 통과한다.
- 적용 후보:
  - Step 8: Split Declaration. 필요한 경우 `GildedRoseItem` 변수 선언과 할당을 분리한다.
  - Step 9~10: Extract Method와 중복 제거. `NormalItem`과 `FoodBeverageItem`의 quality 감소 중복을 공통 메서드로 정리한다.
  - Step 11: Factory Method 정리. 현재 `GildedRoseItem.create(Item item)` 구조가 교재 의도와 맞는지 검토하고 필요한 범위만 보정한다.
  - Step 12: Inline Variable. `GildedRose.updateQuality()`의 임시 변수를 inline할 수 있는지 검토하고 적용한다.
- 상세 계획: 구현 전에 별도로 작성한다.

### Step 9: 최종 정리와 회고

- 목표: 구조, 테스트, 남은 리스크를 검토하고 실습을 마무리한다.
- 산출물: 최종 구조 요약, 테스트 결과, 변경 전후 비교, 다음 개선 후보.
- 완료 기준: `mvn test`가 통과하고, Approval Test 정책을 만족하며, `ROADMAP.md`와 `README.md` 상태가 갱신된다.
- 상세 계획: 구현 전에 별도로 작성한다.

## 테스트와 검증 규칙

- 각 구현 Step 후 `mvn test`를 실행한다.
- 동작 보존 Step에서는 Approval Test 출력이 변경되면 안 된다.
- 동작 변경 Step에서는 구현 전에 요구사항과 기대 결과를 기록한다.
- 각 Step 종료 시 이 파일의 `진행 상태`와 `상태 추적표`를 갱신한다.
- 각 Step 종료 시 `README.md`의 To-Do List가 실제 완료 상태와 일치하는지 확인한다.

## 가정

- 이 로드맵 파일은 `GildedRose_04/ROADMAP.md`이다.
- Step별 실행 체크리스트는 `GildedRose_04/README.md`의 To-Do List에 기록한다.
- PDF 6페이지의 `Employee` 예제는 개념 설명용이며 이 프로젝트의 구현 대상이 아니다.
- `Item` 클래스와 `items` 필드의 public 형태는 변경하지 않는다.
- Conjured 기능은 이미 구현되어 있으며, 이후 Step에서는 기존 동작을 새 구조로 이동한다.
- F&B item type은 레거시 안전 리팩터링 이후 진행하는 확장 실습으로 다룬다.
