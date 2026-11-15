package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.ProductsPage;

public class ProductsTest extends BaseTest {

    @Test
    public void productsPageShowsSixProductsTest() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(driver);

        Assert.assertEquals(productsPage.getPageTitle(), "Products");
        Assert.assertEquals(productsPage.getProductCount(), 6);
    }

    @Test
    public void addSingleProductTest() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(driver);

        productsPage.addProductToCart("sauce-labs-backpack");

        Assert.assertEquals(productsPage.getCartCount(), "1");
    }

    @Test
    public void addMultipleProductsTest() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(driver);

        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");

        Assert.assertEquals(productsPage.getCartCount(), "2");
    }
}