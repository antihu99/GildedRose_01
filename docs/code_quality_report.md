# GildedRose.updateQuality() Code Quality Report

## 분석 기준

- 대상: `src/main/java/com/gildedrose/GildedRose.java`의 `updateQuality()`
- 관점: SOLID, Code Smell, 변경 용이성, 회귀 위험
- 우선순위: `1`이 가장 높고, `5`가 가장 낮음

## 문제점 분석

| 문제점 | 위반 원칙/스멜 | 영향 | 개선 방향 | 우선순위 |
| --- | --- | --- | --- | --- |
| `updateQuality()`가 아이템 순회, 타입 판별, 품질 변경, 판매기한 감소, 만료 후 처리까지 모두 수행한다. | SRP 위반, Long Method | 하나의 변경이 전체 메서드에 영향을 주며, 특정 아이템 규칙을 수정할 때 다른 아이템 동작을 깨뜨릴 가능성이 높다. | 공통 흐름과 아이템별 품질 정책을 분리한다. 먼저 메서드 추출로 `updateSellIn`, `increaseQuality`, `decreaseQuality`, `isExpired` 같은 의도를 드러낸 뒤, 필요하면 아이템별 전략 클래스로 확장한다. | 1 - 모든 스멜의 중심 원인이고 이후 리팩토링의 기반이다. |
| `"Aged Brie"`, `"Backstage passes to a TAFKAL80ETC concert"`, `"Sulfuras, Hand of Ragnaros"` 문자열 비교로 아이템 타입을 직접 판별한다. | OCP 위반, Primitive Obsession, Duplicated Code | 신규 아이템이 추가될 때마다 기존 조건문을 수정해야 하므로 확장에 닫혀 있지 않다. 문자열 오타나 이름 변경에도 취약하다. | 아이템 이름 상수 또는 타입 판별 메서드를 도입하고, 장기적으로는 아이템별 업데이트 정책을 캡슐화한다. `Item` 클래스는 수정하지 않고 `GildedRose` 내부 또는 별도 패키지에서 정책 매핑을 구성한다. | 2 - 새 요구사항 추가 시 변경 범위와 회귀 위험을 크게 키운다. |
| 품질 증감 조건이 여러 곳에 반복된다. 예: `quality < 50`, `quality > 0`, `quality = quality + 1`, `quality = quality - 1` 패턴이 중복된다. | Duplicated Code, Shotgun Surgery | 품질 경계 규칙을 바꾸거나 보정 로직을 수정할 때 여러 지점을 동시에 고쳐야 한다. 일부 지점 누락 시 품질 범위 불변식이 깨질 수 있다. | `increaseQuality(Item item)`, `decreaseQuality(Item item)` 같은 경계 보장 메서드를 추출한다. 품질 상한/하한을 한 곳에서 관리해 중복 조건을 제거한다. | 3 - 품질 범위는 핵심 도메인 규칙이므로 중복 제거 효과가 크다. |
| `0`, `1`, `5`, `6`, `10`, `11`, `50` 같은 숫자가 의미 없이 직접 사용된다. | Magic Number | 백스테이지 패스 임계값, 품질 상한, 품질 하한, 하루 단위 감소 규칙의 의미가 코드만으로 드러나지 않는다. 특히 `< 11`, `< 6`은 비즈니스 규칙과 실제 날짜 기준의 관계를 읽기 어렵게 만든다. | `MIN_QUALITY`, `MAX_QUALITY`, `FIRST_BACKSTAGE_THRESHOLD`, `SECOND_BACKSTAGE_THRESHOLD`, `SELL_IN_EXPIRED` 같은 상수로 의미를 부여한다. | 4 - 즉시 버그를 만들지는 않지만 가독성과 규칙 변경 비용을 악화시킨다. |
| 중첩 `if`가 깊고 부정 조건이 많다. 예: `!Aged Brie && !Backstage`, `!Sulfuras`, 만료 후 다시 여러 타입을 판별한다. | High Cyclomatic Complexity, Arrow Code, Negative Condition | 실행 경로가 많아져 리뷰와 테스트 케이스 도출이 어렵다. 만료 전/후 규칙과 아이템별 예외가 섞여 있어 새로운 조건을 추가할수록 복잡도가 급격히 증가한다. | 가드 절, 의도 있는 조건 메서드, 아이템별 처리 메서드로 분기 구조를 평탄화한다. 일반 아이템, `Aged Brie`, `Backstage passes`, `Sulfuras`의 규칙을 독립적으로 읽히게 만든다. | 2 - 복잡도가 회귀 위험과 테스트 누락 가능성을 직접 높인다. |
| `Sulfuras` 예외 처리가 여러 지점에 흩어져 있다. 품질 감소 방지와 판매기한 감소 방지가 각각 별도 조건으로 존재한다. | SRP 위반, Feature Envy, Duplicated Conditional | 전설 아이템의 불변 규칙이 한 곳에 모여 있지 않아, 이후 로직 추가 시 실수로 `Sulfuras`를 변경할 가능성이 있다. | `isLegendary(item)` 또는 `updateSulfuras(item)`처럼 예외 규칙을 명시적으로 분리한다. 전략 구조를 적용한다면 `Sulfuras` 정책은 아무 것도 변경하지 않는 구현으로 둔다. | 3 - 예외 규칙이 명확하지 않아 향후 변경에서 실수하기 쉽다. |
| 백스테이지 패스 규칙이 일반 증가 로직 내부에 끼어 있고, 판매기한 만료 후 품질을 0으로 만드는 규칙도 별도 하단 분기에 있다. | SRP 위반, Temporal Coupling, Low Cohesion | 하나의 아이템 규칙이 메서드 상단과 하단에 흩어져 있어 전체 동작을 한눈에 파악하기 어렵다. 임계일 변경이나 만료 규칙 변경 시 수정 위치를 놓치기 쉽다. | `updateBackstagePass(item)`로 만료 전 증가량과 만료 후 품질 초기화를 한 곳에 모은다. 임계값별 증가량은 명확한 조건 순서로 표현한다. | 3 - 백스테이지는 조건이 가장 많아 테스트와 리팩토링 우선 대상이다. |
| `items[i].quality = items[i].quality - items[i].quality`로 품질을 0으로 만든다. | Obscure Intent, Readability Smell | 결과는 0이지만 의도가 즉시 드러나지 않는다. 읽는 사람이 산술 트릭인지 도메인 규칙인지 판단해야 한다. | `item.quality = MIN_QUALITY` 또는 `resetQuality(item)`처럼 의도를 직접 표현한다. | 5 - 영향 범위는 작지만 가독성 개선이 쉽다. |

## 리팩토링 우선순위 요약

| 우선순위 | 리팩토링 작업 | 이유 |
| --- | --- | --- |
| 1 | `updateQuality()` 메서드 분해 | SRP 위반과 Long Method를 먼저 줄여야 이후 변경을 안전하게 진행할 수 있다. |
| 2 | 아이템 타입 판별과 분기 구조 정리 | OCP 위반과 복잡도를 줄여 신규 아이템 추가 비용을 낮춘다. |
| 3 | 품질 증감/경계값 처리 공통화 | 핵심 도메인 불변식인 `quality` 범위를 한 곳에서 보장한다. |
| 4 | 매직 넘버와 문자열 상수화 | 규칙의 의미를 드러내고 오타 및 임계값 변경 위험을 줄인다. |
| 5 | 표현 개선 및 세부 가독성 정리 | `quality - quality` 같은 모호한 표현을 제거해 유지보수성을 높인다. |
