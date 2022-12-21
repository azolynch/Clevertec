import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class TestRunner {
    private final Purchase purchase = new Purchase("src/main/resources/product.txt",
            "src/main/resources/promo.txt");
    private final ArrayList<String[]> idCount = new ArrayList<>();
    private final StringBuilder cheque = new StringBuilder("""
                            CASH RECEIPT
                          SUPERMARKET  123
                      12, MILKYWAY Galaxy/ Earth
                          Tel:123-456-7890
            CASHIER: 1234                 DATE:\040""").
            append(String.valueOf(java.time.LocalDate.now()).replace("-", "/")).
            append("\n                              TIME: ").
            append(String.valueOf(java.time.LocalTime.now()), 0, 8).
            append("""

                    ________________________________________________
                    QTY  DESCRIPTION               PRICE   TOTAL
                     2   Milk                      $1.50  $3.00
                     1   Melon                     $4.73  $4.73
                     4   Bananas                   $2.35  $9.40
                     3   Bread                     $1.00  $3.00
                     5   Milk                      $1.50  $7.50
                    ================================================
                    TAXABLE TOT.                           $26.28
                    VAT5%                                  $1.35
                    TOTAL                                  $27.63
                    ________________________________________________""");
    private final ArrayList<Product> productsList = new ArrayList<>();

    {
        idCount.add("1-2".split("-"));
        idCount.add("4-1".split("-"));
        idCount.add("3-4".split("-"));
        idCount.add("2-3".split("-"));
        idCount.add("1-5".split("-"));
        productsList.add(new ProductBuilderIMpl().setId(1).setName("Milk").setPrice(150).setPromo(false).build());
        productsList.add(new ProductBuilderIMpl().setId(4).setName("Melon").setPrice(473).setPromo(false).build());
        productsList.add(new ProductBuilderIMpl().setId(3).setName("Bananas").setPrice(235).setPromo(true).build());
        productsList.add(new ProductBuilderIMpl().setId(2).setName("Bread").setPrice(100).setPromo(true).build());
        productsList.add(new ProductBuilderIMpl().setId(1).setName("Milk").setPrice(150).setPromo(false).build());
    }

    @Test
    public void testPurchaseCreatePurchase() {
        Assertions.assertEquals(purchase.createPurchase(idCount).toString(), productsList.toString());
        Assertions.assertEquals(purchase.createPurchase(idCount).get(0).toString(), productsList.get(0).toString());
        Assertions.assertEquals(purchase.createPurchase(idCount).get(1).toString(), productsList.get(1).toString());
        Assertions.assertEquals(purchase.createPurchase(idCount).get(2).toString(), productsList.get(2).toString());
        Assertions.assertEquals(purchase.createPurchase(idCount).get(3).toString(), productsList.get(3).toString());
        Assertions.assertEquals(purchase.createPurchase(idCount).get(4).toString(), productsList.get(4).toString());

    }

    @Test
    public void testPurchaseGetCostProduct() {
        purchase.createPurchase(idCount);
        Assertions.assertEquals(purchase.getCostProduct(), 2763);
    }

    @Test
    public void testPurchaseGetCostWithSale() {
        purchase.createPurchase(idCount);
        purchase.getCostProduct();
        Assertions.assertEquals(purchase.getCostWithSale("card-1111"), "$26.28");
        Assertions.assertEquals(purchase.getCostWithSale("card-1423"), "$24.39");
        Assertions.assertEquals(purchase.getCostWithSale("card-3524"), "$27.63");
        Assertions.assertEquals(purchase.getCostWithSale("card-2352"), "$26.82");
        Assertions.assertEquals(purchase.getCostWithSale("card-5353"), "$27.63");
        Assertions.assertEquals(purchase.getCostWithSale("card-7543"), "$25.20");
    }

    @Test
    public void testPurchaseGetCheque() {
        purchase.createPurchase(idCount);
        purchase.getCostProduct();
        purchase.getCostWithSale("card-1111");
        Assertions.assertEquals(purchase.getCheque().toString(), cheque.toString());
    }

    @Test
    public void testPurchaseGetter() {
        purchase.createPurchase(idCount);
        purchase.getCostProduct();
        purchase.getCostWithSale("card-1111");
        Assertions.assertEquals(purchase.getTotalCost(), "$27.63");
        Assertions.assertEquals(purchase.getTotalSale(), "$1.35");
        Assertions.assertEquals(purchase.getCostWithSale(), "$26.28");
    }

    @Test
    public void testPurchaseWrong() throws RuntimeException {
        idCount.add("2--7".split("-"));
        Assertions.assertThrows(RuntimeException.class, () -> purchase.createPurchase(idCount));
        Assertions.assertThrows(RuntimeException.class, () -> new Purchase("src/1111", "src/2222"));
    }

    @Test
    public void testProductBuilder() {
        Product product = new ProductBuilderIMpl().setId(0).setName("Butter").setPrice(250).setPromo(true).build();
        Assertions.assertEquals(product.getId(), 0);
        Assertions.assertEquals(product.getName(), "Butter");
        Assertions.assertEquals(product.getPrice(), "$2.50");
        Assertions.assertTrue(product.isPromo());
        Assertions.assertEquals(product.toString(), "0;Butter;250;true");
    }

    @Test
    public void testProductGetPriceStr() {
        Assertions.assertEquals(Product.getPriceStr(1111), "$11.11");
        Assertions.assertEquals(Product.getPriceStr(0), "$0.00");
        Assertions.assertEquals(Product.getPriceStr(1000), "$10.00");
        Assertions.assertEquals(Product.getPriceStr(1001), "$10.01");
        Assertions.assertEquals(Product.getPriceStr(11), "$0.11");
        Assertions.assertEquals(Product.getPriceStr(1), "$0.01");
    }

    @Test
    public void testFactoryProduct() {
        try (Scanner sc = new Scanner(new FileReader("src/test/resources/testProductList.txt"))) {
            Assertions.assertEquals(FactoryProduct.getProduct(sc.nextLine()).toString(), "0;Cheese;762;true");
            Assertions.assertEquals(FactoryProduct.getProduct(sc.nextLine()).toString(), "1;Milk;150;false");
            Assertions.assertEquals(FactoryProduct.getProduct(sc.nextLine()).toString(), "2;Bread;100;true");
            Assertions.assertEquals(FactoryProduct.getProduct(sc.nextLine()).toString(), "3;Bananas;235;true");
            Assertions.assertEquals(FactoryProduct.getProduct(sc.nextLine()).toString(), "4;Melon;473;false");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFactoryPromo() {
        try (Scanner sc = new Scanner(new FileReader("src/test/resources/testPromoList.txt"))) {
            Assertions.assertEquals(FactoryPromo.getPromo(sc.nextLine()).toString(), "3231;2");
            Assertions.assertEquals(FactoryPromo.getPromo(sc.nextLine()).toString(), "1241;4");
            Assertions.assertEquals(FactoryPromo.getPromo(sc.nextLine()).toString(), "3534;87");
            Assertions.assertEquals(FactoryPromo.getPromo(sc.nextLine()).toString(), "7856;6");
            Assertions.assertEquals(FactoryPromo.getPromo(sc.nextLine()).toString(), "6764;8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}