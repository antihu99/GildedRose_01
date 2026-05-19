package com.gildedrose;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class TexttestFixture {
    public static void main(String[] args) {
        int days = 2;
        if (args.length > 0) {
            days = Integer.parseInt(args[0]) + 1;
        }

        printReport(System.out, days);
    }

    static String generateReport(int days) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8);
        printReport(out, days);
        out.flush();
        return output.toString(StandardCharsets.UTF_8);
    }

    static void printReport(PrintStream out, int days) {
        out.println("OMGHAI!");

        Item[] items = new Item[] {
                new Item("+5 Dexterity Vest", 10, 20), //
                new Item("Aged Brie", 2, 0), //
                new Item("Elixir of the Mongoose", 5, 7), //
                new Item("Sulfuras, Hand of Ragnaros", 0, 80), //
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Conjured Mana Cake", 3, 6) };

        GildedRose app = new GildedRose(items);

        for (int i = 0; i < days; i++) {
            out.println("-------- day " + i + " --------");
            out.println("name, sellIn, quality");
            for (Item item : items) {
                out.println(item);
            }
            out.println();
            app.updateQuality();
        }
    }

}
