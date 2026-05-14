package com.gildedrose;

class GildedRose {

    // 상수 정의
    static final String AGED_BRIE = "Aged Brie";
    static final String BACKSTAGE = "Backstage concert";
    static final String SULFURAS  = "Sulfuras, Hand of Ragnaros";
    static final String CONJURED  = "Conjured Mana Cake";
    static final int MAX_QUALITY  = 50;
    static final int MIN_QUALITY  = 0;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }
    
    public void updateQuality() {

        for (Item item : items) {

            // Sulfuras
            if (item.name.equals(SULFURAS)) {
                continue;
            }

            // 기본 sellIn 감소
            item.sellIn--;

            // Aged Brie
            if (item.name.equals(AGED_BRIE)) {
                increaseQuality(item);

                if (item.sellIn < 0) {
                    increaseQuality(item);
                }
            }

            // Backstage
            else if (item.name.equals(BACKSTAGE)) {

                if (item.sellIn < 0) {
                    item.quality = 0;
                } else {
                    increaseQuality(item);

                    if (item.sellIn < 10) {
                        increaseQuality(item);
                    }

                    if (item.sellIn < 5) {
                        increaseQuality(item);
                    }
                }
            }

            // 일반 아이템
            else {
                decreaseQuality(item);

                if (item.sellIn < 0) {
                    decreaseQuality(item);
                }
            }
        }
    }

    private void increaseQuality(Item item) {
        if (item.quality < MAX_QUALITY) {
            item.quality++;
        }
    }

    private void decreaseQuality(Item item) {
        if (item.quality > MIN_QUALITY) {
            item.quality--;
        }
    }

}