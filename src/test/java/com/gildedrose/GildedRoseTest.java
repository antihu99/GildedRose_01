package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Gilded Rose 품질 업데이트")
public class GildedRoseTest {
    private static final String NORMAL_ITEM = "+5 Dexterity Vest";
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String CONJURED = "Conjured Mana Cake";

    @Nested
    @DisplayName("Normal 아이템")
    class NormalItemTest {
        @DisplayName("Given 일반 아이템 상태 When 하루가 지나면 Then sellIn은 1 감소하고 quality는 규칙대로 감소한다")
        @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "5, 10, 4, 9",
                "0, 10, -1, 8",
                "-1, 10, -2, 8",
                "5, 0, 4, 0",
                "0, 1, -1, 0"
        })
        void updatesNormalItem(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItemAfterOneDay(NORMAL_ITEM, sellIn, quality, expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Aged Brie")
    class AgedBrieTest {
        @DisplayName("Given Aged Brie 상태 When 하루가 지나면 Then sellIn은 1 감소하고 quality는 최대 50까지 증가한다")
        @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "2, 0, 1, 1",
                "0, 10, -1, 12",
                "-1, 10, -2, 12",
                "2, 50, 1, 50",
                "0, 49, -1, 50"
        })
        void updatesAgedBrie(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItemAfterOneDay(AGED_BRIE, sellIn, quality, expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Backstage Pass")
    class BackstagePassTest {
        @DisplayName("Given Backstage Pass 상태 When 하루가 지나면 Then 기간 구간별 증가 또는 만료 후 0이 적용된다")
        @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "15, 20, 14, 21",
                "10, 20, 9, 22",
                "5, 20, 4, 23",
                "0, 20, -1, 0",
                "-1, 20, -2, 0",
                "5, 49, 4, 50",
                "15, 50, 14, 50"
        })
        void updatesBackstagePass(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItemAfterOneDay(BACKSTAGE_PASS, sellIn, quality, expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Sulfuras")
    class SulfurasTest {
        @DisplayName("Given Sulfuras 상태 When 하루가 지나면 Then sellIn과 quality가 모두 변하지 않는다")
        @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "0, 80, 0, 80",
                "-1, 80, -1, 80",
                "5, 80, 5, 80",
                "0, 50, 0, 50",
                "-1, 0, -1, 0"
        })
        void keepsSulfurasUnchanged(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItemAfterOneDay(SULFURAS, sellIn, quality, expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Conjured")
    class ConjuredTest {
        @DisplayName("Given Conjured 상태 When 하루가 지나면 Then 일반 아이템보다 두 배 빠르게 quality가 감소한다")
        @ParameterizedTest(name = "[{index}] sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "3, 6, 2, 4",
                "0, 10, -1, 6",
                "-1, 10, -2, 6",
                "3, 1, 2, 0",
                "0, 3, -1, 0",
                "5, 0, 4, 0"
        })
        void updatesConjuredItem(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItemAfterOneDay(CONJURED, sellIn, quality, expectedSellIn, expectedQuality);
        }
    }

    private void assertItemAfterOneDay(String name, int sellIn, int quality, int expectedSellIn, int expectedQuality) {
        // Given
        Item item = new Item(name, sellIn, quality);
        Item[] items = new Item[] { item };
        GildedRose app = new GildedRose(items);

        // When
        app.updateQuality();

        // Then
        assertAll(
                () -> assertEquals(name, item.name),
                () -> assertEquals(expectedSellIn, item.sellIn),
                () -> assertEquals(expectedQuality, item.quality)
        );
    }
}
