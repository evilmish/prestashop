package tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccessoriesPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.NewAccountCreationPage;
import utils.SocialTitle;
import utils.Utils;

import java.math.BigDecimal;

public class BaseTest extends TestBase {

    @Test
    public void purchaseMakingTest() {

        String name = "Johnny";
        String surName = "Knoxville";

        //small hack to have always new email so we dont face issue with "email already exists"
        String generatedRandomStringForEmail = RandomStringUtils.random(7, true, false);
        String email = name + "." + surName + "@" + generatedRandomStringForEmail + ".com";

        // Register an account, check you're logged in
        NewAccountCreationPage createAccountPage = getMainPage()
                .getNavigationBar()
                .goToSignInPage()
                .goToAccountCreationPage();

        createAccountPage
                .choseSocialTitle(SocialTitle.MR)
                .enterFirstName(name)
                .enterLastName(surName)
                .enterEmail(email)
                .enterPassword("123456")
                .agreeWithTerms()
                .submitRegistrationForm();

        String actualUser = getMainPage().getLoggedUserNameAndSurname();
        String expectedUser = name + " " + surName;
        Assert.assertEquals(actualUser, expectedUser);

        // Open "Accessories" section
        AccessoriesPage accessories = getMainPage()
                .getNavigationBar()
                .goToAccessoriesPage();


        // Randomly choose one of items, increase quantity of items and add to cart
        int listedProductQuantity = accessories.returnAmountOfListedItems();
        int randomItem = RandomUtils.nextInt(0, listedProductQuantity);

        CartPage cartPage = accessories
                .choseItem(randomItem)
                .increaseQuantityBy(6)
                .addToCart()
                .proceedToCheckout();

        // Check a price is correctly calculated
        int addedItemQuantity = cartPage.getItemAmount(0);
        BigDecimal firstAddedItemPrice = cartPage.getItemPrice(0);
        BigDecimal firstAddedItemTotalPrice = cartPage.getItemTotalPrice(0);
        BigDecimal firstItemExpectedTotalPrice = firstAddedItemPrice.multiply(BigDecimal.valueOf(addedItemQuantity));
        BigDecimal purchaseTotalSummary = cartPage.getPurchaseTotalPrice();
        Assert.assertEquals(firstAddedItemTotalPrice, firstItemExpectedTotalPrice);
        Assert.assertEquals(firstAddedItemTotalPrice, purchaseTotalSummary);

        // Go back to filtered list of items, choose one more item
        // Go to cart
        //TODO re-think this approach
        int newRandomItem = Utils.returnNewRandomNumberIfItsNotProvidedNumber(randomItem, listedProductQuantity);

        cartPage
                .goBackToSortedAccessoriesPage()
                .choseItem(newRandomItem)
                .addToCart()
                .proceedToCheckout();

        //Check a total price is correctly calculated
        BigDecimal secondAddedItemTotalPrice = cartPage.getItemTotalPrice(1);
        BigDecimal allItemsTotalPrice = firstAddedItemTotalPrice.add(secondAddedItemTotalPrice);
        purchaseTotalSummary = cartPage.getPurchaseTotalPrice();
        Assert.assertEquals(allItemsTotalPrice, purchaseTotalSummary);

        // Checkout
        CheckoutPage checkoutPage = cartPage.goToCheckOutPage();

        // fill out the form
        // choose a shipping method
        // choose "payment by Check", check the total price

        checkoutPage
                .enterAddress("My Address")
                .enterCity("Riga")
                .enterPostCode("77777")
                .continueToShippingMethod()
                .choseDeliveryOption(0)
                .continueToPaymentMethod()
                .chosePayByCheckPaymentMethod()
                .agreeWithTerms();

        Assert.assertEquals(checkoutPage.getTotalPrice(), purchaseTotalSummary);

        // Confirm your order and check order details
        checkoutPage.goToOrderConfirmationPage();


    }

}
