package org.example;

public class FactoryProduct {
    private enum FieldsProduct {
        NON_PROMO_PRODUCT {
            @Override
            Product getProduct(String[] mas) {
                return new ProductBuilderIMpl().setId(Integer.parseInt(mas[0])).setName(mas[1]).setPrice(Integer.parseInt(mas[2])).build();
            }
        },
        PROMO_PRODUCT {
            @Override
            Product getProduct(String[] mas) {
                return new ProductBuilderIMpl().setId(Integer.parseInt(mas[0])).setName(mas[1]).setPrice(Integer.parseInt(mas[2])).setPromo(Boolean.parseBoolean(mas[3])).build();
            }
        };

        abstract Product getProduct(String[] mas);
    }

    public static FieldsProduct getFieldsProduct(int sizeMas) {
        return FieldsProduct.values()[sizeMas - 3];
    }

    public static Product getProduct(String str) {
        String[] mas = str.split(" ");
        try {
            return getFieldsProduct(mas.length).getProduct(mas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
