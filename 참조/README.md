# Gilded Rose

Gilded Rose 는 게임 “World of Warcraft”에 나오는 여관 이름입니다.  
아이템은 퀄리티 속성을 가지고 있으며, 예외 사항이 있는 아이템들이 있습니다.  
- 아이템은 [아이템 이름, 퀄리티, 유통기한]의 속성을 가집니다.  
- 아이템의 퀄리티는 0 이상이고 아이템의 퀄리티는 하루가 지날 때마다 1씩 줄어듭니다.  
- 유통 기한이 지난 아이템의 퀄리티는 2배의 속도로 떨어집니다.  
- 퀄리티는 최대값이 50입니다.  

### Aged Brie, Backstage Pass, Sulfuras 의 예외적 규칙  

- Aged Brie는 하루가 지날 때마다 퀄리티가 1씩 증가합니다.  
- 유통기한이 지나면 퀄리티가 2씩 증가합니다.  
- Backstage Pass 는 유통기한(콘서트일)이 다가올수록 퀄리티가 증가합니다.  
  유통기한이 11일 이상일 때는 1, 10일 이하일 때는 2, 5일 이하일 때는 3씩 증가하지만  
  콘서트 날이 지나면 퀄리티는 0이 됩니다.  
- Sulfuras의 퀄리티는 변화가 없습니다.  
  
---------------------------------------------------------  
## 실습 목적 :  테스트 코드 작성을 통한 legacy code 이해

### GildedRose 의 Legacy 테스트 코드
- GildedRoseTest : foo()
- TexttestFixture :  main()
  
  
### GildedRoseTest.java : failed test 수정
- updateQuality() 의  test case를 추가 작성해 보세요.
- unit test 내용 : 문서상에 나타난 동작들 확인
  
### TexttestFixture.java  
: non-automated golden-master test  
=> Automated test 로 변경(ApprovalTest 이용)  
  
### unit test VS golden-master test 비교

## GildedRose 테스트 케이스 정리

아래 테스트 케이스는 교재의 `실습 #7 GildedRose 단위테스트` 표를 기준으로 하되, 현재 코드베이스의 실제 아이템 이름과 요구사항의 경계 조건을 반영해 정리한 목록이다.

| No | 단계 | 테스트 이름 | 입력값 | 예상값 | 설명 | Remark |
|---:|---|---|---|---|---|---|
| 1 | 기존 동작 보호 | `normal_item_decreases_by_1_before_sell_date` | `noname, 10, 20` | `sellIn = 9`, `quality = 19` | 일반 아이템은 판매 기한 전에는 하루가 지날 때 `sellIn`과 `quality`가 각각 1씩 감소해야 한다. | 신규 추가 |
| 2 | 기존 동작 보호 | `normal_item_quality_never_negative` | `noname, 0, 0` | `sellIn = -1`, `quality = 0` | 일반 아이템의 품질은 아무리 감소해도 0 미만으로 내려가면 안 된다. | 교재 그대로 |
| 3 | 기존 동작 보호 | `normal_item_degrades_twice_after_sell_date` | `noname, 0, 5` | `sellIn = -1`, `quality = 3` | 판매 기한이 지난 일반 아이템은 품질이 보통보다 두 배 빠르게 감소해야 한다. | 교재 그대로 |
| 4 | 기존 동작 보호 | `sulfuras_never_changes_before_sell_date` | `Sulfuras, Hand of Ragnaros, 0, 80` | `sellIn = 0`, `quality = 80` | 전설 아이템인 Sulfuras는 판매 기한과 품질이 변하지 않아야 한다. | 교재 수정 |
| 5 | 기존 동작 보호 | `sulfuras_never_changes_after_sell_date` | `Sulfuras, Hand of Ragnaros, -1, 80` | `sellIn = -1`, `quality = 80` | Sulfuras는 이미 판매 기한이 지난 상태에서도 `sellIn`과 `quality`가 변하지 않아야 한다. | 교재 수정 |
| 6 | 기존 동작 보호 | `aged_brie_increases_by_1_before_sell_date` | `Aged Brie, 2, 0` | `sellIn = 1`, `quality = 1` | Aged Brie는 시간이 지날수록 품질이 증가하며, 판매 기한 전에는 하루에 1씩 증가해야 한다. | 신규 추가 |
| 7 | 기존 동작 보호 | `aged_brie_increases_twice_after_sell_date` | `Aged Brie, 0, 0` | `sellIn = -1`, `quality = 2` | 판매 기한이 지난 Aged Brie는 품질이 하루에 2씩 증가해야 한다. | 교재 수정 |
| 8 | 기존 동작 보호 | `aged_brie_quality_never_more_than_50` | `Aged Brie, 0, 50` | `sellIn = -1`, `quality = 50` | 일반 품질 상한은 50이므로 Aged Brie도 50을 초과해서 증가하면 안 된다. | 교재 수정 |
| 9 | 기존 동작 보호 | `backstage_increases_by_1_when_more_than_10_days` | `Backstage passes to a TAFKAL80ETC concert, 15, 0` | `sellIn = 14`, `quality = 1` | Backstage pass는 공연까지 10일보다 많이 남았을 때 하루에 품질이 1 증가해야 한다. | 교재 수정 |
| 10 | 기존 동작 보호 | `backstage_increases_by_2_when_10_days_or_less` | `Backstage passes to a TAFKAL80ETC concert, 10, 0` | `sellIn = 9`, `quality = 2` | 공연까지 10일 이하로 남으면 Backstage pass의 품질은 하루에 2 증가해야 한다. | 신규 추가 |
| 11 | 기존 동작 보호 | `backstage_increases_by_3_when_5_days_or_less` | `Backstage passes to a TAFKAL80ETC concert, 5, 0` | `sellIn = 4`, `quality = 3` | 공연까지 5일 이하로 남으면 Backstage pass의 품질은 하루에 3 증가해야 한다. | 신규 추가 |
| 12 | 기존 동작 보호 | `backstage_quality_never_more_than_50` | `Backstage passes to a TAFKAL80ETC concert, 10, 49` | `sellIn = 9`, `quality = 50` | Backstage pass도 품질 상한 50을 초과해서 증가하면 안 된다. | 신규 추가 |
| 13 | 기존 동작 보호 | `backstage_quality_drops_to_0_after_concert` | `Backstage passes to a TAFKAL80ETC concert, 0, 20` | `sellIn = -1`, `quality = 0` | 공연일이 지나면 Backstage pass의 품질은 즉시 0이 되어야 한다. | 교재 수정 |
| 14 | 기존 동작 보호 | `backstage_quality_drops_to_0_even_if_over_50_after_concert` | `Backstage passes to a TAFKAL80ETC concert, 0, 51` | `sellIn = -1`, `quality = 0` | 비정상적으로 품질이 50을 초과한 상태라도 공연이 지나면 품질은 0으로 떨어져야 한다. | 교재 수정 |
| 15 | 기존 동작 보호 | `empty_items_do_nothing` | 빈 배열 | `items.length = 0`, 예외 없음 | 아이템 목록이 비어 있어도 `updateQuality()`는 예외 없이 종료되어야 한다. | 교재 그대로 |
| 16 | 신규 기능 TDD | `conjured_degrades_twice_as_fast_before_sell_date` | `Conjured Mana Cake, 3, 6` | `sellIn = 2`, `quality = 4` | Conjured 아이템은 판매 기한 전에도 일반 아이템보다 두 배 빠르게 품질이 감소해야 한다. 처음에는 실패해야 정상이다. | 신규 기능 |
| 17 | 신규 기능 TDD | `conjured_degrades_twice_as_fast_after_sell_date` | `Conjured Mana Cake, 0, 6` | `sellIn = -1`, `quality = 2` | 판매 기한이 지난 Conjured 아이템은 일반 아이템의 기한 후 감소량보다 두 배 빠르게, 하루에 4 감소해야 한다. | 신규 기능 |

### 기존 동작 보호 To-Do List
- 교재의 Test Code 로 2,3,8,9,10,11,12,13 테스트 케이스 완료

- [x] 사전 작업: 초기 실패 테스트의 기대값을 수정한다.
- [x] No.1 `normal_item_decreases_by_1_before_sell_date`: 일반 아이템은 판매 기한 전 하루가 지나면 `sellIn`과 `quality`가 각각 1씩 감소한다.
- [x] No.2 `normal_item_quality_never_negative`: 일반 아이템의 품질은 아무리 감소해도 0 미만으로 내려가지 않는다.
- [x] No.3 `normal_item_degrades_twice_after_sell_date`: 판매 기한이 지난 일반 아이템은 품질이 2 감소한다.
- [x] No.4 `sulfuras_never_changes_before_sell_date`: Sulfuras는 판매 기한 전에도 `sellIn`과 `quality`가 변하지 않는다.
- [x] No.5 `sulfuras_never_changes_after_sell_date`: Sulfuras는 판매 기한 후에도 `sellIn`과 `quality`가 변하지 않는다.
- [x] No.6 `aged_brie_increases_by_1_before_sell_date`: Aged Brie는 판매 기한 전 하루가 지나면 품질이 1 증가한다.
- [x] No.7 `aged_brie_increases_twice_after_sell_date`: 판매 기한이 지난 Aged Brie는 품질이 2 증가한다.
- [x] No.8 `aged_brie_quality_never_more_than_50`: Aged Brie의 품질은 50을 초과하지 않는다.
- [x] No.9 `backstage_increases_by_1_when_more_than_10_days`: Backstage passes는 공연까지 10일 초과로 남으면 품질이 1 증가한다.
- [x] No.10 `backstage_increases_by_2_when_10_days_or_less`: Backstage passes는 공연까지 10일 이하로 남으면 품질이 2 증가한다.
- [x] No.11 `backstage_increases_by_3_when_5_days_or_less`: Backstage passes는 공연까지 5일 이하로 남으면 품질이 3 증가한다.
- [x] No.12 `backstage_quality_never_more_than_50`: Backstage passes의 품질은 50을 초과하지 않는다.
- [x] No.13 `backstage_quality_drops_to_0_after_concert`: Backstage passes는 공연이 지나면 품질이 0이 된다.
- [x] No.14 `backstage_quality_drops_to_0_even_if_over_50_after_concert`: Backstage passes는 품질이 50을 초과한 상태라도 공연이 지나면 품질이 0이 된다.
- [x] No.15 `empty_items_do_nothing`: 빈 배열이면 `updateQuality()`가 예외 없이 종료된다.


### 상수리팩토링 To-Do List
- [x] 아이템 이름과 품질 경계값을 `static final` 상수로 추출한다.


### 신규 기능 To-Do List
- [x] No.16 `conjured_degrades_twice_as_fast_before_sell_date`: Conjured 아이템은 판매 기한 전에도 일반 아이템보다 품질이 2배 빠르게 감소한다.
- [x] No.17 `conjured_degrades_twice_as_fast_after_sell_date`: 판매 기한이 지난 Conjured 아이템은 품질이 하루에 4 감소한다.

## Step 0: 레거시 코드 문제점 분석

분석 대상은 `GildedRose.updateQuality()`이다.  
이 단계에서는 코드를 변경하지 않고, 이후 리팩터링 Step들이 참조할 레거시 코드의 문제점을 정리한다.

### 핵심 문제 4가지

1. **가독성 저하**
   - `updateQuality()` 하나에 모든 아이템 규칙이 중첩 조건문으로 들어 있다.
   - 특히 판매기한 이후 처리 구간에서는 조건 중첩이 깊어 흐름을 한눈에 파악하기 어렵다.
   - 관련 위치: `GildedRose.java`의 `updateQuality()` 전체, 특히 판매기한 이후 처리 분기.

2. **OCP 위반**
   - 새 아이템 타입을 추가하려면 `updateQuality()`의 기존 조건문을 직접 수정해야 한다.
   - F&B 같은 신규 타입이 추가될수록 기존 분기 조건이 계속 늘어난다.
   - 관련 위치: `AGED_BRIE`, `BACKSTAGE`, `SULFURAS`, `CONJURED` 이름 비교가 모여 있는 조건문.

3. **타입 코드 남용**
   - `Item.name` 문자열 비교가 아이템 타입 판별 역할을 한다.
   - 문자열 오타나 이름 변경에 취약하고, 타입별 동작이 명확한 단위로 분리되어 있지 않다.
   - 관련 위치: `items[i].name.equals(...)`를 반복해서 사용하는 타입 분기.

4. **테스트 난이도 높음**
   - 전체 결과 기반 테스트는 가능하지만, 타입별 규칙을 독립적으로 검증하기 어렵다.
   - quality 변경, sellIn 변경, 타입 판별, 경계값 처리가 한 메서드에 뒤섞여 있어 실패 원인을 좁히기 어렵다.
   - 관련 위치: `updateQuality()` 안에서 quality 변경, sellIn 변경, 만료 후 처리가 함께 수행되는 흐름.

### 보조 관찰 사항

- 일반 아이템과 Conjured 아이템의 품질 감소 로직이 판매기한 전/후 처리에 반복된다.
- quality 변경 후 sellIn을 감소시키고, 다시 만료 후 처리를 하는 시간 순서가 코드에 숨어 있다.
- Backstage, Aged Brie, Sulfuras 같은 도메인 규칙이 조건문 내부 구현 디테일로 흩어져 있다.
- `MIN_QUALITY`, `MAX_QUALITY` 경계 처리가 여러 위치에 분산되어 있다.
- `Sulfuras` 예외 처리가 quality 변경, sellIn 변경, 만료 후 처리 조건에 나뉘어 있다.
- `items[i]` 접근이 반복되어 코드가 장황하고 실수 가능성이 높다.
- `updateQuality()`가 이름과 달리 타입 판별, quality 변경, sellIn 변경, 만료 정책까지 모두 담당한다.
- 새 요구사항이 추가될수록 조건 조합이 늘어나 회귀 위험이 커진다.

### Step 0 To-Do List

- [x] 레거시 코드 핵심 문제 4가지를 정리한다.
- [x] 각 문제점을 현재 `GildedRose.updateQuality()` 코드 위치와 연결한다.
- [x] 보조 관찰 사항을 정리한다.
- [x] 이후 Step 계획 수립 시 참조할 기준으로 기록한다.

## Step 1: 회귀 테스트 보호막 확인

이 단계에서는 리팩터링 전에 현재 테스트 보호막을 확인하고 기준선을 기록한다.  
production code와 test code는 변경하지 않는다.

### 테스트 기준선

- 실행 명령: `mvn test`
- 실행 시각: `2026-05-14T16:09:56+09:00`
- 결과: `BUILD SUCCESS`
- 테스트 결과: `Tests run: 23, Failures: 0, Errors: 0, Skipped: 0`

### Approval Test 기준선

- `ApprovalTest.testPrintTextFixture.approved.txt`와 현재 `ApprovalTest` 출력이 일치한다.
- 대표 아이템들의 2일치 출력이 golden master 기준선으로 고정되어 있다.
- 이후 동작 보존 리팩터링에서는 Approval Test 출력이 변경되면 안 된다.

### 현재 테스트 보호 범위

| 영역 | 보호 중인 동작 | 현재 보호 상태 |
| --- | --- | --- |
| 일반 아이템 | 판매 전 quality -1, 판매 후 quality -2, quality 0 미만 방지 | 보호됨 |
| Aged Brie | 판매 전 quality +1, 판매 후 quality +2, quality 50 초과 방지 | 보호됨 |
| Sulfuras | 판매 전/후 sellIn과 quality 불변 | 보호됨 |
| Backstage passes | 10일 초과 +1, 10일 이하 +2, 5일 이하 +3, 공연 후 0, quality 50 초과 방지 | 보호됨 |
| Conjured | 판매 전 quality -2, 판매 후 quality -4 | 보호됨 |
| 빈 item 배열 | 예외 없이 종료 | 보호됨 |
| Golden Master | 대표 아이템들의 2일치 전체 출력 | 보호됨 |

### 부족하거나 개선할 테스트 후보

- Conjured 아이템의 quality가 0 미만으로 내려가지 않는 경계 테스트.
- Conjured 아이템의 quality가 1 또는 2일 때 감소 후 0으로 보정되는지 확인하는 테스트.
- 일반 아이템의 판매 후 테스트에서 quality뿐 아니라 sellIn 변화까지 함께 검증하는 보강 테스트.
- Approval Test 기간을 2일보다 길게 늘릴지 여부는 별도 계획에서 판단한다.

### Step 1 To-Do List

- [x] `mvn test` 기준선을 기록한다.
- [x] Approval Test 기준선을 기록한다.
- [x] 현재 테스트 보호 범위를 정리한다.
- [x] 부족한 경계 테스트 후보를 정리한다.
- [x] 이후 Step에서 테스트 보강 필요 여부를 기록한다.

## Step 2: 작은 구조 정리

이 단계에서는 교재 Step 2의 `Extract Constant`, `Extract Variable`, `Remove Unused Code` 중 현재 코드에 남아 있는 잔여 작업만 수행한다.  
상수 추출은 이미 완료되어 있으므로 다시 수행하지 않고, `items[i]` 반복 접근을 로컬 변수로 추출한다.

### 교재 Step 2 대비 현재 상태

| 항목 | 현재 상태 | Step 2 처리 |
| --- | --- | --- |
| Extract Constant | 이미 완료됨 | 변경 없음 |
| Extract Variable | `items[i]` 반복 접근이 남아 있음 | `Item item = items[i];`로 추출 |
| Remove Unused Code | 현재 Step에서 별도 제거 대상 없음 | 변경 없음 |

### 적용 범위

- `GildedRose.updateQuality()` 반복문 안에 `Item item = items[i];`를 추가한다.
- 반복문 내부의 `items[i].name`, `items[i].quality`, `items[i].sellIn` 접근을 `item.name`, `item.quality`, `item.sellIn`으로 치환한다.
- 반복문 형태, 조건문 구조, 도메인 로직은 변경하지 않는다.
- Step 0의 문제점 중 `items[i]` 반복 접근과 가독성 저하를 일부 완화한다.

### Step 2 To-Do List

- [x] 교재 Step 2 중 이미 완료된 상수 추출 상태를 확인한다.
- [x] `updateQuality()` 반복문 안에서 `Item item = items[i];` 로컬 변수를 추출한다.
- [x] 반복문 내부의 `items[i]` 직접 접근을 `item` 접근으로 치환한다.
- [x] 조건문 구조와 동작은 변경하지 않는다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 3: 조건문 단순화

이 단계에서는 교재 Step 3의 `Invert if`, `Merge else if` 흐름에 맞춰 `updateQuality()`의 조건문을 읽기 쉬운 형태로 정리한다.  
동작을 변경하지 않고, 다음 Step의 메서드 추출이 쉬운 구조를 만든다.

### 교재 Step 3 대비 적용 내용

| 항목 | 적용 내용 |
| --- | --- |
| Invert if | `Aged Brie`와 `Backstage` 처리를 부정 조건이 아닌 긍정 조건으로 먼저 분기한다. |
| Merge else if | `Sulfuras` 예외 처리를 빈 `else if` 분기로 드러낸다. |
| 조건 중첩 완화 | 판매기한 이후 처리에서 `Aged Brie`, `Backstage`, `Sulfuras`, 일반/Conjured 분기를 `if / else if / else`로 정리한다. |

### 적용 범위

- 첫 번째 품질 변경 분기를 `Aged Brie`/`Backstage` 긍정 조건 중심으로 정리한다.
- `Sulfuras`는 변경되지 않는 아이템임을 빈 `else if` 분기로 명시한다.
- 판매기한 이후 처리의 부정 조건 중첩을 `if / else if / else` 구조로 바꾼다.
- Conjured 감소 로직과 sellIn 감소 순서는 유지한다.
- 메서드 추출, 클래스 분리, helper 메서드 추가는 Step 4 이후로 미룬다.

### Step 3 To-Do List

- [x] 첫 번째 큰 조건문을 부정 조건에서 긍정 조건 중심으로 반전한다.
- [x] `Sulfuras` 예외 처리를 `else if` 분기로 명시한다.
- [x] 판매기한 이후 처리의 중첩 조건을 `if / else if / else` 구조로 정리한다.
- [x] Conjured 동작과 sellIn 감소 순서를 유지한다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 4: 메서드 추출

이 단계에서는 교재 Step 4의 `Extract Method` 흐름에 맞춰 타입별 품질 변경 로직과 `sellIn` 변경 로직을 private method로 분리한다.  
교재 Java 예시를 최대한 따르되, 현재 코드에 이미 구현된 Conjured 동작은 `updateNormalItem()` 안에서 보존한다.

### 교재 Step 4 대비 적용 내용

| 교재 메서드 | 현재 적용 |
| --- | --- |
| `updateAgedBrie(Item item)` | Aged Brie의 판매 전/후 quality 증가 처리 |
| `updateBackstage(Item item)` | Backstage passes의 구간별 quality 증가와 공연 후 0 처리 |
| `updateSulfuras(Item item)` | 변경 없음 동작을 빈 메서드로 표현 |
| `updateNormalItem(Item item)` | 일반 아이템 감소 처리와 Conjured 기존 감소 규칙 보존 |
| `updateSellIn(Item item)` | Sulfuras를 제외한 sellIn 감소 처리 |

### 적용 범위

- `updateQuality()`는 반복, 타입 dispatch, `updateSellIn()` 호출만 담당하도록 축소한다.
- 반복문은 교재 Java 예시처럼 `for (Item item : items)`로 변경한다.
- 판매기한 이후 판정은 교재 방식에 맞춰 각 update method 안에서 `item.sellIn < 1`로 처리한다.
- 클래스 분리, 추상 클래스, factory 도입은 Step 5 이후로 미룬다.
- Conjured 별도 메서드나 클래스 분리는 이번 Step에서 하지 않는다.

### Step 4 To-Do List

- [x] `updateQuality()`를 반복, 타입 dispatch, `updateSellIn()` 호출 중심으로 축소한다.
- [x] `updateAgedBrie(Item item)`을 추출한다.
- [x] `updateBackstage(Item item)`을 추출한다.
- [x] `updateSulfuras(Item item)`을 추출한다.
- [x] `updateNormalItem(Item item)`을 추출하고 Conjured 기존 동작을 보존한다.
- [x] `updateSellIn(Item item)`을 추출한다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 5: 타입별 클래스 분리

이 단계에서는 교재 Step 5의 `Move Method + New Class` 흐름에 맞춰 Step 4에서 추출한 타입별 품질 변경 로직을 별도 클래스로 이동한다.  
`GildedRoseItem` 추상 클래스와 factory 도입은 교재상 다음 단계이므로 이번 Step에서는 적용하지 않는다.

### 교재 Step 5 대비 적용 내용

| 교재 클래스 | 현재 적용 |
| --- | --- |
| `AgedBrieItem` | Aged Brie의 판매 전/후 quality 증가 처리 이동 |
| `BackstagePassItem` | Backstage passes의 구간별 quality 증가와 공연 후 0 처리 이동 |
| `SulfurasItem` | 변경 없음 동작을 빈 `updateQuality()`로 표현 |
| `NormalItem` | 일반 아이템 감소 처리와 Conjured 기존 감소 규칙 보존 |

### 적용 범위

- `GildedRose.updateQuality()`는 타입 판별, 타입별 객체 생성, `updateSellIn()` 호출만 담당하도록 축소한다.
- 타입별 quality 변경 로직은 package-private item wrapper 클래스로 이동한다.
- 새 클래스들은 기존 `Item` 인스턴스를 생성자로 받아 감싸며 public API를 변경하지 않는다.
- Conjured는 아직 별도 클래스로 분리하지 않고 `NormalItem` 안에서 기존 동작을 보존한다.
- Step 0의 문제점 중 가독성 저하, 책임 혼재, 타입별 로직 분산 문제를 완화한다.
- OCP 위반과 문자열 기반 타입 판별은 아직 남아 있으며, 추상 클래스와 factory 도입 Step에서 계속 다룬다.

### 검증 결과

- 실행 명령: `mvn test`
- 실행 시각: `2026-05-14T16:53:35+09:00`
- 결과: `BUILD SUCCESS`
- 테스트 결과: `Tests run: 23, Failures: 0, Errors: 0, Skipped: 0`
- Approval Test 출력은 기존 기준선과 일치한다.

### Step 5 To-Do List

- [x] `AgedBrieItem` 클래스를 추가하고 Aged Brie 품질 변경 로직을 이동한다.
- [x] `BackstagePassItem` 클래스를 추가하고 Backstage 품질 변경 로직을 이동한다.
- [x] `SulfurasItem` 클래스를 추가하고 변경 없음 동작을 이동한다.
- [x] `NormalItem` 클래스를 추가하고 일반 아이템 및 Conjured 기존 동작을 보존한다.
- [x] `GildedRose.updateQuality()`를 타입별 객체 dispatch 중심으로 축소한다.
- [x] `updateSellIn(Item item)`은 `GildedRose`에 유지한다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 6: 공통 추상 클래스와 Factory 도입

이 단계에서는 교재 Step 6의 `GildedRoseItem` 추상 클래스와 Factory 도입 흐름에 맞춰 타입별 item wrapper를 공통 타입으로 묶는다.  
`GildedRose.updateQuality()`는 factory로 item wrapper를 생성한 뒤 `updateQuality()`와 `updateSellIn()`을 호출하는 orchestration만 담당한다.

### 교재 Step 6 대비 적용 내용

| 교재 항목 | 현재 적용 |
| --- | --- |
| `GildedRoseItem` | `Item`을 감싸는 추상 클래스로 추가 |
| `updateQuality()` | 타입별 클래스가 override하는 추상 메서드로 이동 |
| `updateSellIn()` | `GildedRoseItem` 기본 구현으로 이동 |
| Factory | `GildedRoseItem.create(Item item)` static factory method로 적용 |
| `Sulfuras` 예외 | `SulfurasItem.updateSellIn()` override로 sellIn 불변 보존 |

### 적용 범위

- `AgedBrieItem`, `BackstagePassItem`, `NormalItem`, `SulfurasItem`은 `GildedRoseItem`을 상속한다.
- 타입별 클래스의 중복 `Item` 필드는 `GildedRoseItem`의 `protected Item item`으로 올린다.
- `GildedRose`의 타입별 생성 분기는 `GildedRoseItem.create(Item item)`로 이동한다.
- `GildedRose`의 private `updateSellIn(Item item)`은 제거한다.
- Conjured는 아직 별도 클래스로 분리하지 않고 `NormalItem` 안에서 기존 동작을 보존한다.
- Step 0의 문제점 중 OCP 위반, 타입 코드 남용, 책임 혼재를 완화한다.
- 문자열 기반 타입 판별은 factory 내부에 남아 있으며, 새 타입 추가 시 factory 수정이 필요한 상태는 이후 확장 Step에서 계속 검토한다.

### 검증 결과

- 실행 명령: `mvn test`
- 실행 시각: `2026-05-14T16:58:43+09:00`
- 결과: `BUILD SUCCESS`
- 테스트 결과: `Tests run: 23, Failures: 0, Errors: 0, Skipped: 0`
- Approval Test 출력은 기존 기준선과 일치한다.

### Step 6 To-Do List

- [x] `GildedRoseItem` 추상 클래스를 추가한다.
- [x] 공통 `Item` 필드와 생성자를 `GildedRoseItem`으로 이동한다.
- [x] 타입별 클래스가 `GildedRoseItem`을 상속하도록 변경한다.
- [x] `GildedRoseItem.create(Item item)` factory method를 추가한다.
- [x] `GildedRose.updateQuality()`에서 직접 타입별 객체를 생성하는 분기를 제거한다.
- [x] `updateSellIn()` 책임을 `GildedRoseItem` 계층으로 이동한다.
- [x] `SulfurasItem.updateSellIn()`을 no-op으로 override한다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 7: F&B 신규 타입 확장

이 단계에서는 교재 Step 7의 Food & Beverage(F&B) 예시를 반영해 신규 item type을 추가한다.  
Step 6까지 만든 `GildedRoseItem` 계층과 factory를 확장 지점으로 사용하며, `GildedRose.updateQuality()`의 orchestration 구조는 변경하지 않는다.

### F&B 요구사항

- F&B 아이템은 이름에 `[F&B]`가 포함된 item으로 판별한다.
- 판매 기한 전에는 일반 아이템보다 2배 빠르게 quality가 감소한다.
- 판매 기한이 지난 후에는 하루에 quality가 4 감소한다.
- quality는 0 미만으로 내려가지 않는다.
- sellIn은 일반 아이템처럼 하루에 1 감소한다.

### 교재 Step 7 대비 적용 내용

| 교재 항목 | 현재 적용 |
| --- | --- |
| `FoodBeverageItem` | `GildedRoseItem`을 상속하는 신규 클래스로 추가 |
| F&B 판별 | `item.name.contains("[F&B]")`를 factory에 추가 |
| 판매 전 감소 | quality -2 |
| 판매 후 감소 | 추가 quality -2, 총 quality -4 |
| 경계값 | `MIN_QUALITY` 아래로 내려가지 않도록 보정 |

### 적용 범위

- `GildedRoseItem.create(Item item)`에 F&B 분기를 추가한다.
- F&B 품질 변경 규칙은 `FoodBeverageItem.updateQuality()` 안에만 둔다.
- `GildedRose.updateQuality()`는 변경하지 않는다.
- Approval Test fixture에 F&B 대표 아이템을 추가하고 golden master 기준선을 의도적으로 갱신한다.
- Step 0의 문제점 중 OCP 위반은 factory 한 곳 수정으로 완화되었고, 타입별 품질 규칙 분산 문제는 신규 클래스로 격리해 완화되었다.
- 문자열 기반 타입 판별은 factory 내부에 남아 있으므로 완전한 해결은 아니며, 최종 정리 Step에서 남은 리스크로 검토한다.

### 검증 결과

- TDD 실패 확인: F&B 테스트 추가 직후 `mvn -Dtest=GildedRoseTest test` 실행 시 2개 테스트 실패.
  - 판매 전 F&B: 기대 quality `18`, 실제 `19`
  - 판매 후 F&B: 기대 quality `16`, 실제 `18`
- 구현 후 단위 테스트: `mvn -Dtest=GildedRoseTest test`
  - 결과: `Tests run: 25, Failures: 0, Errors: 0, Skipped: 0`
- Approval Test 기준선 갱신:
  - `[F&B] Bread, 5, 20`과 `[F&B] Milk, 0, 20`을 fixture에 추가했다.
  - Approval Test가 신규 출력으로 실패하는 것을 확인한 뒤 approved output을 의도적으로 갱신했다.
- 전체 테스트: `mvn test`
  - 실행 시각: `2026-05-14T17:10:03+09:00`
  - 결과: `BUILD SUCCESS`
  - 테스트 결과: `Tests run: 26, Failures: 0, Errors: 0, Skipped: 0`
  - Approval Test 출력은 기존 기준선과 일치한다.

### Step 7 To-Do List

- [x] PDF의 F&B 요구사항을 README에 기록한다.
- [x] F&B 판매 기한 전 quality 감소 테스트를 추가한다.
- [x] F&B 판매 기한 후 quality 감소 테스트를 추가한다.
- [x] F&B quality 0 미만 방지 테스트를 추가한다.
- [x] 실패 테스트를 먼저 확인한다.
- [x] `FoodBeverageItem` 클래스를 추가한다.
- [x] `GildedRoseItem.create(Item item)`에 F&B factory 분기를 추가한다.
- [x] `GildedRose.updateQuality()` orchestration 구조를 유지한다.
- [x] Approval Test fixture와 approved output에 F&B 대표 케이스를 반영한다.
- [x] `mvn test`로 전체 동작을 검증한다.

## Step 8: 추가 리팩터링

이 단계에서는 교재 24page의 Step 8~12 추가 리팩터링을 현재 코드 상태에 맞게 적용한다.  
동작 변경 없이 중복을 줄이고 `GildedRose.updateQuality()`의 orchestration을 더 단순하게 만든다.

### 교재 Step 8~12 대비 적용 내용

| 교재 Step | 항목 | 현재 적용 |
| --- | --- | --- |
| Step 8 | Split Declaration | 현재 코드는 선언 분리가 필요하지 않아 적용하지 않음 |
| Step 9~10 | Extract Method / Remove Duplication | `GildedRoseItem.decreaseQuality()`로 quality 감소 중복 제거 |
| Step 11 | Factory Method 정리 | Step 6의 `GildedRoseItem.create(Item item)` 구조 유지 |
| Step 12 | Inline Variable | `GildedRose.updateQuality()`의 임시 변수를 inline하고 `update()` template method 도입 |

### 적용 범위

- `GildedRoseItem`에 `update()` template method를 추가한다.
- `GildedRoseItem`에 `decreaseQuality()` helper를 추가해 quality 0 미만 방지 규칙을 공통화한다.
- `NormalItem`은 `decreaseQuality()`와 `decreaseConjuredQuality()`를 사용하도록 정리한다.
- `FoodBeverageItem`은 `decreaseQuality()` 호출 횟수로 판매 전 -2, 판매 후 -4 규칙을 표현한다.
- `GildedRose.updateQuality()`는 `GildedRoseItem.create(item).update();` 한 줄 호출로 축소한다.
- factory 분기 순서와 F&B 판별 조건은 변경하지 않는다.
- Approval Test approved output은 변경하지 않는다.

### 검증 결과

- 실행 명령: `mvn test`
- 실행 시각: `2026-05-14T17:18:27+09:00`
- 결과: `BUILD SUCCESS`
- 테스트 결과: `Tests run: 26, Failures: 0, Errors: 0, Skipped: 0`
- Approval Test 출력은 기존 기준선과 일치한다.

### Step 8 To-Do List

- [x] 교재 Step 8~12 중 현재 코드에 적용할 항목과 적용하지 않을 항목을 구분한다.
- [x] `GildedRoseItem.update()` template method를 추가한다.
- [x] `GildedRoseItem.decreaseQuality()` helper를 추가한다.
- [x] `NormalItem`의 quality 감소 중복을 helper 호출로 정리한다.
- [x] `FoodBeverageItem`의 quality 감소 중복을 helper 호출로 정리한다.
- [x] `GildedRose.updateQuality()`의 임시 변수를 inline한다.
- [x] factory 구조와 분기 순서를 유지한다.
- [x] `mvn test`로 동작 보존을 확인한다.

## Step 9: 최종 정리와 회고

이 단계에서는 전체 GildedRose 리팩터링 실습의 최종 상태를 정리한다.  
production code와 test code는 변경하지 않고, 최종 구조와 테스트 결과, 해결된 문제와 남은 리스크를 문서화한다.

### 최종 구조 요약

- `GildedRose.updateQuality()`는 item 순회와 `GildedRoseItem.create(item).update()` 호출만 담당한다.
- `GildedRoseItem.create(Item item)`이 item type dispatch를 담당한다.
- 타입별 quality 규칙은 `AgedBrieItem`, `BackstagePassItem`, `SulfurasItem`, `NormalItem`, `FoodBeverageItem`에 분리되어 있다.
- 공통 update 흐름은 `GildedRoseItem.update()`에 있으며, quality 감소 경계 처리는 `GildedRoseItem.decreaseQuality()`로 공통화되어 있다.
- `SulfurasItem`은 `updateSellIn()`을 override해 sellIn 불변 규칙을 보존한다.

### Step 0 문제점 대비 결과

| 문제점 | 최종 상태 |
| --- | --- |
| 가독성 저하 | `updateQuality()`의 중첩 조건문이 제거되어 크게 완화됨 |
| OCP 위반 | 새 타입 추가 시 주로 factory와 신규 클래스만 수정하면 되어 완화됨. 단, factory 수정은 여전히 필요함 |
| 타입 코드 남용 | 문자열 판별이 factory에 격리되어 완화됨. 단, 문자열 기반 판별 자체는 남아 있음 |
| 테스트 난이도 높음 | 타입별 단위 테스트와 Approval Test 기준선으로 회귀 보호가 강화됨 |

### 남은 리스크와 개선 후보

- item type 판별은 아직 `Item.name` 문자열에 의존한다.
- `Conjured`는 별도 `ConjuredItem`이 아니라 `NormalItem` 내부 조건으로 남아 있다.
- 새 타입 추가 시 `GildedRoseItem.create(Item item)` factory 수정이 필요하다.
- `target/` 빌드 산출물이 git status에 계속 보이므로 `.gitignore` 추가 여부를 별도 판단할 수 있다.
- README의 일부 기존 실습 설명과 테스트 코드 주석에는 과거 인코딩 깨짐 흔적이 남아 있어, 필요하면 별도 문서 정리 Step으로 다룰 수 있다.

### 최종 검증 결과

- 실행 명령: `mvn test`
- 실행 시각: `2026-05-14T17:23:33+09:00`
- 결과: `BUILD SUCCESS`
- 테스트 결과: `Tests run: 26, Failures: 0, Errors: 0, Skipped: 0`
- Approval Test 출력은 approved output과 일치한다.

### Step 9 To-Do List

- [x] 최종 구조를 요약한다.
- [x] Step 0의 레거시 코드 문제점 대비 결과를 정리한다.
- [x] 남은 리스크와 개선 후보를 기록한다.
- [x] `mvn test`로 최종 검증을 수행한다.
- [x] 최종 테스트 결과를 기록한다.
