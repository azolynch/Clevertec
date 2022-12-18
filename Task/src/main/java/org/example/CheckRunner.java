package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckRunner {
    public static void main(String[] args) {
        String cardSale;
        ArrayList<String[]> idCountList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        Purchase purchase = new Purchase("src/main/resources/product.txt",
                "src/main/resources/promo.txt");

        for (; ; ) {
            String str = sc.next();
            try {
                if (str.matches("\\d+-\\d+")) {
                    idCountList.add(str.split("-"));
                } else if (str.matches("card-\\d{4}")) {
                    cardSale = str;
                    break;
                } else System.out.println("Enter again, please");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        purchase.createPurchase(idCountList);
        purchase.getCostProduct();
        purchase.getCostWithSale(cardSale);
        try (FileWriter fileWriter = new FileWriter("src/main/resources/cheque.txt", false)) {
            fileWriter.write(String.valueOf(purchase.getCheque()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
//        idCountList.add("1-2".split("-"));
//        idCountList.add("4-1".split("-"));
//        idCountList.add("3-4".split("-"));
//        idCountList.add("2-3".split("-"));
//        idCountList.add("1-5".split("-"));
//        idCountList.add("10-5".split("-"));
//        idCountList.add("8-9".split("-"));
//        idCountList.add("7-9".split("-"));
//        idCountList.add("2-3".split("-"));
//        idCountList.add("6-2".split("-"));