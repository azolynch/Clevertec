package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Purchase {
    private static final int SALE = 10;
    ArrayList<Product> arrayListAllProduct = new ArrayList<>();
    ArrayList<Product> listProduct = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();
    int costWithSale;
    int totalCost;
    int totalSale;
    int totalSalePercent;
    HashMap<Integer, Integer> numberCardSale = new HashMap<>();

    public Purchase() {
    }

    public Purchase(String fileNameProduct, String fileNamePromo) {
        try (Scanner sc = new Scanner(new FileReader(fileNameProduct))) {
            while (sc.hasNext()) {
                arrayListAllProduct.add(FactoryProduct.getProduct(sc.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Scanner sc = new Scanner(new FileReader(fileNamePromo))) {
            while (sc.hasNext()) {
                numberCardSale.put(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCostWithSale() {
        return Product.getPriceStr(costWithSale);
    }

    public String getTotalCost() {
        return Product.getPriceStr(totalCost);
    }

    public String getTotalSale() {
        return Product.getPriceStr(totalSale);
    }

    public ArrayList<Product> createPurchase(ArrayList<String[]> idCount) {
        for (String[] temp : idCount) {
            try {
                count.add(Integer.parseInt(temp[1]));
                this.listProduct.add(arrayListAllProduct.get(Integer.parseInt(temp[0])));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error");
            }

        }
        return listProduct;
    }

    public int getCostProduct() {
        for (int i = 0; i < count.size(); i++) {
            totalCost += listProduct.get(i).price * count.get(i);
        }
        return totalCost;
    }

    public String getCostWithSale(String numCard) {
        int key = Integer.parseInt(numCard.substring(numCard.length() - 4));
        int cardSale = 0;
        if (numberCardSale.get(key) != null) {
            cardSale = numberCardSale.get(key);
            totalSalePercent = cardSale;
        }

        int countPromo = 0;
        for (Product temp : listProduct) {
            if (temp.promo) {
                countPromo++;
            }
        }

        if (countPromo <= 5) {
            totalSale = totalCost / 100 * cardSale;

        } else {
            totalSalePercent += SALE;
            totalSale = totalCost * (cardSale + SALE) / 100;
        }
        costWithSale = totalCost - totalSale;
        return Product.getPriceStr(costWithSale);
    }

    public StringBuilder getCheque() {
        StringBuilder cheque = new StringBuilder("""
                                CASH RECEIPT
                              SUPERMARKET  123
                          12, MILKYWAY Galaxy/ Earth
                              Tel:123-456-7890
                CASHIER: 1234                 DATE:\040""").
                append(String.valueOf(java.time.LocalDate.now()).replace("-", "/")).
                append("\n                              TIME: ").
                append(String.valueOf(java.time.LocalTime.now()), 0, 8).
                append("\n________________________________________________").
                append("\nQTY  DESCRIPTION               PRICE   TOTAL");
        int sizeDescription = "DESCRIPTION".length() + 15;
        int sizeName = 0;
        int sizePrice = "PRICE".length();
        for (int i = 0; i < count.size(); i++) {
            if (sizeDescription - listProduct.get(i).name.length() >= 0)
                sizeName = sizeDescription - listProduct.get(i).name.length();
            if (listProduct.get(i).getPrice().length() > sizePrice)
                sizePrice = listProduct.get(i).getPrice().length();

            cheque.append("\n ").append(count.get(i)).append("   ").
                    append(listProduct.get(i).name.replace("_", " ")).append(" ".repeat(sizeName)).
                    append(listProduct.get(i).getPrice()).
                    append(" ".repeat(sizePrice - listProduct.get(i).getPrice().length())).
                    append("  ").append(Product.getPriceStr(count.get(i) * listProduct.get(i).price));
        }
        cheque.append("""

                        ================================================
                        TAXABLE TOT.                          \s""").
                append(getCostWithSale()).append("\n").append("VAT").append(totalSalePercent).
                append("%                                  ").append(getTotalSale()).append("\n").
                append("TOTAL                                  ").append(getTotalCost()).
                append("\n________________________________________________");
        return cheque;
    }
}