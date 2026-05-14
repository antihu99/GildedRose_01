package com.gildedrose;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GildeRoseItemtest {

    private Item[] items;
    private GildedRose app;

    // private GildeRoseItemtest(Item[] ITEMS, GildedRose APP) {
    //     this.items = ITEMS;
    //     this.app = APP;
    // }

    @BeforeEach
    void setUp() {

        items = new Item[] { new Item("+5 Dexterity Vest", 10, 20)};
        // items = new Item[] { new Item("Aged Brie", 2, 0) };
        // items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)};
        // items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", -1, 80) };
        // items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 0, 80)};
        // items = new Item[] { new Item("Elixir of the Mongoose", 5, 7)};
        
        app = new GildedRose(items);
        // debug console print
        System.out.println(" app.items[0].name : " +  app.item[0].toString());
    }

    @Test
    @DisplayName("이름 반환 확인")
    void testGetName() {
        assertEquals("+5 Dexterity Vest", app.item[0].name);
    }

    @Test
    @DisplayName("Before sellIn 확인")
    void testBeforellIn() {
        assertEquals(10, app.item[0].sellIn);
    }

    @Test
    @DisplayName("Before quality 확인")
    void testBeforequality() {
        assertEquals(20, app.item[0].quality);
    }

    @Test
    @DisplayName("updateQuality 확인")
    void testUpdateQuality() {
        app.updateQuality();

        assertAll(
            () -> assertEquals(9, app.item[0].sellIn),
            () -> assertEquals(19, app.item[0].quality)
        );    
    }

    // 단위 Test 에서는 오류
    // @Test
    // @DisplayName("After sellIn 확인")
    // void testAfterllIn() {
    //     assertEquals(9, app.items[0].sellIn);
    // }

    // @Test
    // @DisplayName("After quality 확인")
    // void testAfterGetquality() {
    //     assertEquals(21, app.items[0].quality);
    // }
    

}
