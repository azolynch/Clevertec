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

        System.out.println("Enter data in format idItem-Count. For example, 1-1, 2-2, ...\nTo complete the input, enter your card in the format - card-#### (example сard-1234,\nif there is no card, enter card-0000. \nWrite:");
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