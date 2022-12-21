package org.example;

public class Product {
    int id;
    String name;
    int price;
    boolean promo;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return getPriceStr(price);
    }

    public boolean isPromo() {
        return promo;
    }

    public static String getPriceStr(int value) {
        return String.format("$%d.%02d", value / 100, value % 100);
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + price + ";" + promo;
    }
}
