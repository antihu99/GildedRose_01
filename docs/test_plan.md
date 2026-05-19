# Gilded Rose 테스트 계획서

작성 관점: 시니어 QA 리드  
대상 스택: Java 21, JUnit 5, Maven, JaCoCo  
대상 기능: `GildedRose.updateQuality()`

## 1. 목적과 범위

이 계획서는 Gilded Rose의 핵심 비즈니스 규칙을 단위 테스트로 보호하고, 리팩토링 및 Conjured 신규 요구사항 구현 시 회귀를 빠르게 탐지하기 위해 작성되었습니다.

테스트 범위는 `GildedRose.updateQuality()` 호출 전후의 `Item.name`, `Item.sellIn`, `Item.quality` 상태 검증입니다. `Item` 클래스는 레거시 제약상 수정하지 않으며, 테스트는 공개 필드 상태를 직접 검증합니다.

## 2. 단위 테스트 범위와 우선순위

| 우선순위 | 테스트 대상 | 핵심 검증 | 이유 |
|---:|---|---|---|
| P0 | Normal 아이템 | `sellIn` 1 감소, `quality` 1 감소, 만료 후 2 감소, 하한 0 | 모든 특수 아이템의 기준 동작이며 회귀 영향이 큼 |
| P0 | Aged Brie | `quality` 1 증가, 만료 후 2 증가, 상한 50 | 일반 감소 규칙과 반대 방향이라 조건 분기 오류 가능성이 큼 |
| P0 | Backstage Pass | 10일/5일 경계별 증가량, 공연 후 0, 상한 50 | 조건이 중첩되어 경계값 결함 가능성이 가장 큼 |
| P0 | Sulfuras | `sellIn` 불변, `quality` 불변, `quality = 80` 유지 | 일반 상한 규칙의 예외이며 공통 로직 적용 시 쉽게 깨짐 |
| P0 | Conjured | 판매 기한 전 2 감소, 만료 후 4 감소, 하한 0 | 신규 요구사항이며 현재 레거시 구현에서는 별도 처리되지 않을 가능성이 큼 |
| P1 | 복합 배열 처리 | 여러 아이템을 한 번에 업데이트해도 각 아이템 규칙이 독립 적용됨 | 실제 사용 형태가 배열 기반이며 루프 처리 회귀를 탐지 |
| P1 | 불변/식별 검증 | 업데이트 후 `name` 유지, 정확한 아이템명 매칭 | 문자열 기반 분기이므로 식별 조건 변경 리스크가 있음 |
| P2 | 다일 업데이트 | 2일 이상 연속 호출 시 누적 결과 검증 | Characterization 및 승인 테스트 보완용 |

권장 테스트 클래스 구성은 다음과 같습니다.

1. `GildedRoseNormalItemTest`
2. `GildedRoseAgedBrieTest`
3. `GildedRoseBackstagePassTest`
4. `GildedRoseSulfurasTest`
5. `GildedRoseConjuredTest`
6. `GildedRoseBatchUpdateTest`

아이템별 입력 조합이 많은 테스트는 JUnit 5 `@ParameterizedTest`와 `@CsvSource` 또는 `@MethodSource`를 사용합니다. 각 테스트 이름은 Given-When-Then 구조로 기대 동작이 드러나게 작성합니다.

## 3. 필수 경계값 테스트

### 3.1 `quality` 경계값

| `quality` | 적용 대상 | 기대 결과 |
|---:|---|---|
| 0 | Normal | 업데이트 후에도 0 미만으로 내려가지 않음 |
| 0 | Conjured | 감소량이 2 또는 4여도 0 유지 |
| 0 | Aged Brie | 판매 기한 전 1 증가, `sellIn = 0`에서는 2 증가 |
| 0 | Backstage Pass | 구간별 증가 또는 공연 후 0 적용 |
| 1 | Normal | 판매 기한 전 0, 만료 후에도 0 미만 금지 |
| 1 | Conjured | 판매 기한 전/후 모두 0으로 클램프 |
| 49 | Aged Brie | 판매 기한 전 50, 만료 후에도 50 초과 금지 |
| 49 | Backstage Pass | 10일 이하/5일 이하 구간에서도 50 초과 금지 |
| 50 | Aged Brie | 업데이트 후 50 유지 |
| 50 | Backstage Pass | 공연 전 증가 구간에서도 50 유지, 공연 후에는 0 |
| 80 | Sulfuras | 일반 상한 예외로 80 유지 |

### 3.2 `sellIn` 경계값

| `sellIn` | 적용 대상 | 기대 결과 |
|---:|---|---|
| 0 | Normal | 업데이트 후 `sellIn = -1`, `quality` 총 2 감소 |
| 0 | Aged Brie | 업데이트 후 `sellIn = -1`, `quality` 총 2 증가 |
| 0 | Backstage Pass | 업데이트 후 공연이 지나 `quality = 0` |
| 0 | Conjured | 업데이트 후 `sellIn = -1`, `quality` 총 4 감소 |
| 0 | Sulfuras | `sellIn = 0`, `quality` 모두 불변 |
| -1 | Normal | 업데이트 후 `sellIn = -2`, `quality` 2 감소 |
| -1 | Aged Brie | 업데이트 후 `sellIn = -2`, `quality` 2 증가 |
| -1 | Backstage Pass | 업데이트 후 `sellIn = -2`, `quality = 0` |
| -1 | Conjured | 업데이트 후 `sellIn = -2`, `quality` 4 감소 |
| -1 | Sulfuras | `sellIn = -1`, `quality` 모두 불변 |

Backstage Pass는 추가 경계로 `sellIn = 11`, `10`, `6`, `5`, `1`, `0`을 포함합니다.

## 4. 예외 및 특이 케이스 목록

1. `quality`는 Sulfuras를 제외하고 0 미만으로 내려가면 안 됩니다.
2. `quality`는 Sulfuras를 제외하고 50을 초과하면 안 됩니다.
3. Sulfuras는 `quality = 80`도 예외 값으로 유지해야 합니다.
4. Sulfuras는 `sellIn`이 0 또는 음수여도 감소하지 않아야 합니다.
5. `sellIn`은 음수가 될 수 있으며 0으로 보정하지 않습니다.
6. `sellIn = 0`은 업데이트 중 `sellIn = -1`이 되므로 만료 후 규칙 적용 여부를 반드시 검증합니다.
7. Backstage Pass는 `sellIn <= 10`, `sellIn <= 5`, `sellIn < 0` 조건의 우선순위가 올바르게 적용되어야 합니다.
8. Backstage Pass는 공연 이후 증가값과 무관하게 최종 `quality = 0`이어야 합니다.
9. Conjured는 일반 아이템보다 2배 빠르게 감소하고, 만료 후에는 하루 총 4 감소해야 합니다.
10. Conjured 식별은 Aged Brie, Backstage Pass, Sulfuras와 충돌하지 않아야 합니다.
11. 여러 아이템이 배열에 함께 있어도 한 아이템의 업데이트가 다른 아이템에 영향을 주면 안 됩니다.
12. `name` 값은 업데이트 후 변경되지 않아야 합니다.
13. 빈 배열 입력 시 예외 없이 아무 작업도 하지 않는지 확인합니다.
14. `items` 또는 배열 원소가 `null`인 경우는 현재 명세에 없으므로 기본 테스트 범위에서는 제외하고, 제품 정책이 정해지면 별도 예외 테스트로 추가합니다.

## 5. 테스트 설계 전략

먼저 현재 레거시 동작을 Characterization Test로 고정합니다. 이후 요구사항 기준 테스트를 추가해 Conjured처럼 현재 미구현된 동작은 실패 테스트로 확인한 뒤 구현을 진행합니다.

핵심 단위 테스트는 한 번의 `updateQuality()` 호출에 집중합니다. 다일 업데이트는 승인 테스트 또는 별도 시나리오 테스트로 분리해 원인 분석이 쉬운 단위 테스트와 보완 관계를 유지합니다.

반복 조합은 파라미터화합니다. 예를 들어 Normal/Conjured의 하한 테스트, Aged Brie/Backstage Pass의 상한 테스트, Backstage Pass의 날짜 구간 테스트는 입력과 기대값을 표 형태 데이터로 관리합니다.

모든 테스트는 `sellIn`과 `quality`를 함께 검증합니다. 품질 변화만 검증하면 만료 전환 시점 결함을 놓칠 수 있고, 판매 기한만 검증하면 핵심 비즈니스 결함을 놓칠 수 있습니다.

## 6. 커버리지 목표와 JaCoCo 달성 전략

목표 커버리지는 다음과 같습니다.

| 지표 | 목표 |
|---|---:|
| Line Coverage | 90% 이상 |
| Branch Coverage | 85% 이상 |
| Method Coverage | 100% |
| Class Coverage | 100% |

JaCoCo 달성 전략은 다음과 같습니다.

1. `pom.xml`에 `jacoco-maven-plugin`을 추가하고 `prepare-agent`, `report`, `check`를 Maven 생명주기에 연결합니다.
2. `mvn test`에서 단위 테스트를 실행하고, `mvn verify`에서 JaCoCo 커버리지 리포트와 품질 게이트를 확인합니다.
3. 리포트는 `target/site/jacoco/index.html`을 기준으로 확인합니다.
4. 초기에는 라인 90%, 브랜치 85%를 목표로 두고, 리팩토링 후 분기 수가 줄어들면 브랜치 목표를 90% 이상으로 상향합니다.
5. `GildedRose.updateQuality()`의 문자열 분기, 상한/하한 조건, 만료 후 조건을 모두 통과하도록 테스트 데이터를 설계합니다.
6. `TexttestFixture` 같은 수동 실행용 클래스는 커버리지 목표에서 제외하거나 별도 통합/승인 테스트 영역으로 분리합니다.
7. 커버리지 수치만 맞추는 테스트를 피하고, 각 테스트는 명확한 요구사항 또는 경계값과 연결합니다.

권장 JaCoCo 체크 기준 예시는 다음과 같습니다.

```xml
<limits>
    <limit>
        <counter>LINE</counter>
        <value>COVEREDRATIO</value>
        <minimum>0.90</minimum>
    </limit>
    <limit>
        <counter>BRANCH</counter>
        <value>COVEREDRATIO</value>
        <minimum>0.85</minimum>
    </limit>
</limits>
```

## 7. 실행 및 완료 기준

테스트 실행 명령은 다음을 기준으로 합니다.

```bash
mvn test
mvn verify
```

완료 기준은 다음과 같습니다.

1. P0 테스트가 모두 Green입니다.
2. `quality` 0, 1, 49, 50과 `sellIn` 0, -1 경계값이 모두 테스트에 포함되어 있습니다.
3. Sulfuras 예외와 Backstage Pass 공연 후 0 처리 테스트가 포함되어 있습니다.
4. Conjured 요구사항 테스트가 추가되어 구현 전에는 실패하고, 구현 후에는 Green입니다.
5. JaCoCo 기준 Line 90% 이상, Branch 85% 이상을 만족합니다.
6. 리팩토링 전후로 전체 테스트 결과가 동일하게 Green입니다.
