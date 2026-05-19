# Gilded Rose 요구사항 분석

작성 관점: 시니어 Java QA 엔지니어  
대상 프로젝트: Gilded Rose Java 프로젝트 (Java 21, Maven, JUnit 5)

## 1. 아이템 타입별 비즈니스 규칙

| 아이템 타입 | 식별 이름/조건 | `sellIn` 변경 규칙 | `quality` 변경 규칙 | 만료 후 규칙 | 주요 제약 |
|---|---|---:|---|---|---|
| Normal | 특수 아이템이 아닌 일반 아이템 | 매일 1 감소 | 매일 1 감소 | `sellIn < 0`이 되면 하루에 총 2 감소 | `quality`는 0 미만으로 내려가지 않음 |
| Aged Brie | `Aged Brie` | 매일 1 감소 | 매일 1 증가 | `sellIn < 0`이 되면 하루에 총 2 증가 | `quality`는 50 초과 불가 |
| Backstage Pass | `Backstage passes to a TAFKAL80ETC concert` | 매일 1 감소 | 기본 1 증가, `sellIn <= 10`이면 2 증가, `sellIn <= 5`이면 3 증가 | 공연일 이후(`sellIn < 0`) `quality`는 0 | `quality`는 50 초과 불가 |
| Sulfuras | `Sulfuras, Hand of Ragnaros` | 변경 없음 | 변경 없음 | 변경 없음 | `quality` 0~50 제한의 예외 |
| Conjured | 이름이 `Conjured`로 시작하거나 Conjured 아이템으로 정의된 항목 | 매일 1 감소 | 일반 아이템보다 2배 빠르게 감소 | `sellIn < 0`이 되면 일반 만료 후 감소율의 2배로 감소 | `quality`는 0 미만으로 내려가지 않음 |

## 2. 예외/경계값 조건

| 구분 | 경계/예외 조건 | 기대 결과 | QA 확인 포인트 |
|---|---|---|---|
| `quality` 하한 | `quality = 0` | Normal/Conjured는 더 이상 감소하지 않음 | 업데이트 후 음수가 되지 않는지 검증 |
| `quality` 상한 | `quality = 50` | Aged Brie/Backstage Pass는 더 이상 증가하지 않음 | 업데이트 후 51 이상이 되지 않는지 검증 |
| `quality` 일반 범위 | `0 <= quality <= 50` | Sulfuras를 제외한 모든 아이템에 적용 | 모든 테스트에서 `quality` 범위 검증 포함 |
| `sellIn = 0` | 오늘이 판매 마지막 날 | 업데이트 중 `sellIn`은 -1이 되고, 만료 후 규칙이 적용됨 | `sellIn` 감소와 `quality` 변경을 함께 검증 |
| `sellIn = -1` | 이미 만료된 상태 | 만료 후 규칙이 계속 적용됨 | Normal/Aged Brie/Conjured/Backstage Pass 각각 검증 |
| `sellIn < 0` | 음수 판매 기한 | 0으로 보정하지 않고 계속 감소 가능 | `sellIn` 클램핑 금지 검증 |
| Sulfuras 예외 | `Sulfuras, Hand of Ragnaros` | `sellIn`, `quality` 모두 불변 | `quality`가 80이어도 유지되는지 검증 |

## 3. Conjured 신규 요구사항 명세

1. Conjured 아이템은 일반 아이템과 동일하게 매일 `sellIn`이 1 감소한다.
2. Conjured 아이템의 `quality`는 일반 아이템보다 2배 빠르게 감소한다.
3. 판매 기한 전(`sellIn >= 0`)에는 하루에 `quality`가 2 감소한다.
4. 판매 기한이 지난 후(`sellIn < 0`)에는 일반 아이템의 만료 후 감소량도 2배로 적용되어 하루에 `quality`가 4 감소한다.
5. Conjured 아이템도 Sulfuras가 아니므로 `quality`는 0 미만으로 내려가면 안 된다.
6. Conjured 아이템은 Aged Brie, Backstage Pass, Sulfuras 규칙보다 우선 적용되지 않도록 명확히 식별 기준을 둔다.

## 4. 테스트해야 할 주요 시나리오 목록

1. Normal 아이템은 `sellIn > 0`, `quality > 0`일 때 업데이트 후 `sellIn`이 1 감소하고 `quality`가 1 감소한다.
2. Normal 아이템은 `sellIn = 0`일 때 업데이트 후 `sellIn = -1`이 되고 `quality`가 총 2 감소한다.
3. Normal 아이템은 `sellIn < 0`일 때 업데이트 후 `quality`가 2 감소한다.
4. Normal 아이템은 `quality = 0`일 때 업데이트 후에도 `quality`가 0으로 유지된다.
5. Aged Brie는 `sellIn > 0`일 때 업데이트 후 `quality`가 1 증가한다.
6. Aged Brie는 `sellIn = 0`일 때 업데이트 후 `sellIn = -1`이 되고 `quality`가 총 2 증가한다.
7. Aged Brie는 `quality = 50`일 때 업데이트 후에도 `quality`가 50을 초과하지 않는다.
8. Backstage Pass는 `sellIn > 10`일 때 `quality`가 1 증가한다.
9. Backstage Pass는 `sellIn = 10` 또는 `sellIn`이 6~10일 때 `quality`가 2 증가한다.
10. Backstage Pass는 `sellIn = 5` 또는 `sellIn`이 1~5일 때 `quality`가 3 증가한다.
11. Backstage Pass는 `sellIn = 0`에서 업데이트 후 공연일이 지나면 `quality`가 0이 된다.
12. Backstage Pass는 증가 구간에서도 `quality`가 50을 초과하지 않는다.
13. Sulfuras는 업데이트 후 `sellIn`이 변하지 않는다.
14. Sulfuras는 업데이트 후 `quality`가 변하지 않는다.
15. Sulfuras는 `quality = 80` 같은 일반 상한 초과 값도 예외로 유지된다.
16. Conjured 아이템은 `sellIn > 0`일 때 업데이트 후 `quality`가 2 감소한다.
17. Conjured 아이템은 `sellIn = 0`일 때 업데이트 후 `sellIn = -1`이 되고 `quality`가 총 4 감소한다.
18. Conjured 아이템은 `sellIn < 0`일 때 업데이트 후 `quality`가 4 감소한다.
19. Conjured 아이템은 `quality`가 감소량보다 작을 때도 0 미만으로 내려가지 않는다.
20. 모든 비-Sulfuras 아이템은 업데이트 후 `quality`가 0 이상 50 이하인지 검증한다.
21. 모든 비-Sulfuras 아이템은 `sellIn`이 음수여도 0으로 보정되지 않고 계속 감소하는지 검증한다.
22. 여러 아이템이 배열에 함께 있을 때 각 아이템 규칙이 독립적으로 적용되는지 검증한다.
