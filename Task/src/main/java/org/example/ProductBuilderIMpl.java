package org.example;

public class ProductBuilderIMpl implements ProductBuilder {
    Product product = new Product();

    @Override
    public ProductBuilder setName(String name) {
        product.name = name;
        return this;
    }

    @Override
    public ProductBuilder setPrice(int price) {
        product.price = price;
        return this;
    }

    @Override
    public ProductBuilder setPromo(boolean promo) {
        product.promo = promo;
        return this;
    }

    @Override
    public ProductBuilder setId(int id) {
        product.id = id;
        return this;
    }

    @Override
    public Product build() {
        return product;
    }
}
