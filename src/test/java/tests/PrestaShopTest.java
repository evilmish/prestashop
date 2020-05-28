package tests;

import enums.ColorCategories;
import enums.FilterCategories;
import enums.SocialTitle;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AccessoriesPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.NewAccountCreationPage;
import utils.Utils;

import java.math.BigDecimal;

public class PrestaShopTest extends TestBase {

    @Test
    public void purchaseMakingTest() {

        String name = "Johnny";
        String surName = "Knoxville";
        SoftAssert softAssert = new SoftAssert();

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

        String actualUser = getMainPage()
                .getNavigationBar()
                .getLoggedUserNameAndSurname();
        String expectedUser = name + " " + surName;
        softAssert.assertEquals(actualUser, expectedUser, "User not logged in");

        // Open "Accessories" section
        AccessoriesPage accessories = getMainPage()
                .getNavigationBar()
                .goToAccessoriesPage();

        // Filter out items of white colour within price range 18-23

        accessories
                .changePriceRange(18, 23)
                .selectSpecificCheckBox(FilterCategories.COLOR, ColorCategories.WHITE);

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

        softAssert.assertEquals(firstAddedItemTotalPrice,
                firstItemExpectedTotalPrice,
                "Expected and Actual Total Prices Differ");
        softAssert.assertEquals(firstAddedItemTotalPrice,
                purchaseTotalSummary,
                "Total Summary Differs from first Added Item Total Price");

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

        softAssert.assertEquals(allItemsTotalPrice,
                purchaseTotalSummary,
                "Total Price Of All Prices Differ From Total Summary");

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

        softAssert.assertEquals(checkoutPage.getTotalPrice(),
                purchaseTotalSummary,
                "Purchase total Summary Differs From Checkout Price");

        // Confirm your order and check order details
        checkoutPage.goToOrderConfirmationPage();

    }


}
