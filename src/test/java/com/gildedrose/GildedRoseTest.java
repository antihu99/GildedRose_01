package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;

public class GildedRoseTest {

    @Test
    public void foo() {
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        // Error 발생 코드
        // assertEquals("fixme", app.items[0].name);
        
        assertEquals("foo", app.items[0].name);
    }

    @Test 
    @DisplayName("경계값:품질 0값 Test")
    void qualityNeverNegative() {
        Item[] items = {new Item("Normal", 5,0)};
        GildedRose gr = new GildedRose(items);
        gr.updateQuality();
        assertTrue(items[0].quality >= 0);
    }

    @Test 
    @DisplayName("경계값: Max50 Test")
    void agedBrieMax50() {
        Item[] items = {new Item("Aged Brie",5,50)};
        new GildedRose(items).updateQuality();
        assertTrue(items[0].quality <= 50);
    }

    @Test 
    @DisplayName("경계값: 유통기한 지남 Test")
    void normalDegradesTwice() {
        Item[] items = { new Item("Normal",0,10)};
        new GildedRose(items).updateQuality();
        assertEquals(8, items[0].quality);
    }


    // Java — @ParameterizedTest 대응
    @ParameterizedTest(name="sellIn={0},q={1}→{2}")
    @CsvSource({
    "15, 20, 21", // > 10: +1
    "11, 20, 21", // 경계
    "10, 20, 22", // +2 시작
    " 6, 20, 22", // 경계
    " 5, 20, 23", // +3 시작
    " 1, 20, 23", // 경계
    " 0, 20, 0", // concert 후
    " 5, 50, 50", // 상한 50
    " 0, 50, 0" // 0으로
    })
    void backstageTest( int sellIn, int initQ, int exp) {
        Item[] items = { new Item(    "Backstage concert",    sellIn, initQ) };
        new GildedRose(items).updateQuality();
        assertEquals(exp, items[0].quality);    
    }



}