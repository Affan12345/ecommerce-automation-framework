package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;

public class CheckoutTest extends BaseTest {

    @Test
    public void removeProductFromCartTest() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        productsPage.openCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.removeProduct("sauce-labs-backpack");

        Assert.assertEquals(cartPage.getCartItemCount(), 1);
    }

    @Test
    public void completeOrderEndToEndTest() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.openCart();

        new CartPage(driver).clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillInformation("Test", "User", "1207");
        

        Assert.assertEquals(checkoutPage.getConfirmationMessage(),
                "Thank you for your order!");
    }
}