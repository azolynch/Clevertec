package org.example;

public interface ProductBuilder {
    ProductBuilder setName(String name);

    ProductBuilder setPrice(int price);

    ProductBuilder setPromo(boolean promo);

    ProductBuilder setId(int id);

    Product build();
}