package org.example;

public class FactoryPromo {
    private enum FieldsPromo {
        NON_SALE {
            @Override
            Promo getPromo(String[] mas) {
                return new Promo(Integer.parseInt(mas[0]));
            }
        },
        SALE {
            @Override
            Promo getPromo(String[] mas) {
                return new Promo(Integer.parseInt(mas[0]), Integer.parseInt(mas[1]));
            }
        };

        abstract Promo getPromo(String[] mas);
    }

    public static FieldsPromo getFieldsPromo(int sizeMas) {
        return FieldsPromo.values()[sizeMas - 1];
    }

    public static Promo getPromo(String str) {
        String[] mas = str.split(" ");
        try {
            return getFieldsPromo(mas.length).getPromo(mas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
