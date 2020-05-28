package tests;

import enums.ColorCategories;
import enums.FilterCategories;
import enums.SocialTitle;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AccessoriesPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.NewAccountCreationPage;

import java.math.BigDecimal;

public class PrestaShopTest extends TestBase {
    private BigDecimal purchaseTotalSummary;
    private int randomItem;
    private int listedProductQuantity;

    @Test
    public void registerAccountTest() {
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

        String actualUser = getMainPage()
                .getNavigationBar()
                .getLoggedUserNameAndSurname();
        String expectedUser = name + " " + surName;
        Assert.assertEquals(actualUser, expectedUser, "User not logged in");
    }

    @Test(dependsOnMethods = {"registerAccountTest"})
    public void addFirstItemToCartTest() {

        SoftAssert softAssert = new SoftAssert();
        // Open "Accessories" section
        AccessoriesPage accessories = getMainPage()
                .getNavigationBar()
                .goToAccessoriesPage();

        // Filter out items of white colour within price range 18-23
        accessories
                .changePriceRange(18, 23)
                .selectSpecificCheckBox(FilterCategories.COLOR, ColorCategories.WHITE);

        // Randomly choose one of items, increase quantity of items and add to cart
        listedProductQuantity = accessories.returnAmountOfListedItems();
        randomItem = RandomUtils.nextInt(0, listedProductQuantity);

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
        purchaseTotalSummary = cartPage.getPurchaseTotalPrice();

        softAssert.assertEquals(firstAddedItemTotalPrice,
                firstItemExpectedTotalPrice,
                "Expected and Actual Total Prices Differ");
        softAssert.assertEquals(firstAddedItemTotalPrice,
                purchaseTotalSummary,
                "Total Summary Differs from first Added Item Total Price");

    }

    @Test(dependsOnMethods = {"addFirstItemToCartTest"})
    public void addSecondItemToCardTest() {

        // Go back to filtered list of items, choose one more item
        // Go to cart
        randomItem = RandomUtils.nextInt(0, listedProductQuantity);

        CartPage cartPage = new CartPage()
                .goBackToSortedAccessoriesPage()
                .choseItem(randomItem)
                .addToCart()
                .proceedToCheckout();

        //Check a total price is correctly calculated
        BigDecimal allItemsTotalPrice = cartPage.getAllItemTotalPrice();
        purchaseTotalSummary = cartPage.getPurchaseTotalPrice();

        Assert.assertEquals(allItemsTotalPrice,
                purchaseTotalSummary,
                "Total Price Of All Prices Differ From Total Summary");
    }

    @Test(dependsOnMethods = {"addSecondItemToCardTest"})
    public void checkOutTest() {

        // Checkout
        CheckoutPage checkoutPage = new CartPage().goToCheckOutPage();
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

        Assert.assertEquals(checkoutPage.getTotalPrice(),
                purchaseTotalSummary,
                "Purchase total Summary Differs From Checkout Price");

        // Confirm your order and check order details
        checkoutPage.goToOrderConfirmationPage();

    }
}
