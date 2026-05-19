package com.gildedrose;

class GildedRose {
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String CONJURED_PREFIX = "Conjured";
    private static final int MIN_QUALITY = 0;
    private static final int MAX_QUALITY = 50;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            updateItem(item);
        }
    }

    private void updateItem(Item item) {
        if (isSulfuras(item)) {
            return;
        }

        int startingSellIn = item.sellIn;
        item.sellIn = item.sellIn - 1;

        if (isAgedBrie(item)) {
            increaseQuality(item, startingSellIn <= 0 ? 2 : 1);
        } else if (isBackstagePass(item)) {
            updateBackstagePass(item, startingSellIn);
        } else if (isConjured(item)) {
            decreaseQuality(item, startingSellIn <= 0 ? 4 : 2);
        } else {
            decreaseQuality(item, startingSellIn <= 0 ? 2 : 1);
        }
    }

    private void updateBackstagePass(Item item, int startingSellIn) {
        if (item.sellIn < 0) {
            item.quality = MIN_QUALITY;
            return;
        }

        int increase = 1;
        if (startingSellIn <= 10) {
            increase++;
        }
        if (startingSellIn <= 5) {
            increase++;
        }
        increaseQuality(item, increase);
    }

    private void increaseQuality(Item item, int amount) {
        item.quality = Math.min(MAX_QUALITY, item.quality + amount);
    }

    private void decreaseQuality(Item item, int amount) {
        item.quality = Math.max(MIN_QUALITY, item.quality - amount);
    }

    private boolean isAgedBrie(Item item) {
        return AGED_BRIE.equals(item.name);
    }

    private boolean isBackstagePass(Item item) {
        return BACKSTAGE_PASS.equals(item.name);
    }

    private boolean isSulfuras(Item item) {
        return SULFURAS.equals(item.name);
    }

    private boolean isConjured(Item item) {
        return item.name.startsWith(CONJURED_PREFIX);
    }
}