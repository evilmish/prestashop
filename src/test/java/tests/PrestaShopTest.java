package tests;

import enums.ColorCategories;
import enums.FilterCategories;
import enums.SocialTitle;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import pages.order_info.OrderedItem;

import java.math.BigDecimal;
import java.util.List;

public class PrestaShopTest extends TestBase {
    private BigDecimal purchaseTotalSummary;
    private int randomItem;
    private int listedProductQuantity;
    private List<OrderedItem> orderedItems;

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
        Assert.assertTrue(getMainPage().getNavigationBar().isLogOutButtonVisible(),
                "Log out button not visible");
    }

    @Test(dependsOnMethods = {"registerAccountTest"})
    public void addFirstItemToCartTest() {

        SoftAssert softAssert = new SoftAssert();
        int newMinPrice = 18;
        int newMaxPrice = 23;

        // Open "Accessories" section
        AccessoriesPage accessories = getMainPage()
                .getNavigationBar()
                .goToAccessoriesPage();

        // Filter out items of white colour within price range 18-23
        accessories
                .changePriceRange(newMinPrice, newMaxPrice)
                .selectSpecificCheckBox(FilterCategories.COLOR, ColorCategories.WHITE);

        // Check the items are correctly filtered
        boolean isItemsCorrectPrice = accessories
                .isAllListedItemPricesAreBetweenRange(BigDecimal.valueOf(newMinPrice), BigDecimal.valueOf(newMaxPrice));
        boolean isItemsCorrectColor = accessories
                .isAllListedItemColorsMatch(ColorCategories.BLACK);
        softAssert.assertTrue(isItemsCorrectPrice, "Some of Item Prices are outside range");
        softAssert.assertTrue(isItemsCorrectColor, "Some of Item Colors are wrong");

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
        String itemName = cartPage.getItemName(0);
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
        softAssert.assertAll();

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
        orderedItems = cartPage.getOrderedItemsList();

        Assert.assertEquals(allItemsTotalPrice,
                purchaseTotalSummary,
                "Total Price Of All Prices Differ From Total Summary");
    }

    @Test(dependsOnMethods = {"addSecondItemToCardTest"})
    public void checkOutTest() {

        SoftAssert softAssert = new SoftAssert();

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
                .choseFirstDeliveryOption()
                .continueToPaymentMethod()
                .chosePayByCheckPaymentMethod()
                .agreeWithTerms();

        softAssert.assertEquals(checkoutPage.getTotalPrice(),
                purchaseTotalSummary,
                "Purchase total Summary Differs From Checkout Price");

        // Confirm your order and check order details

        OrderConfirmedPage confirmationPage = checkoutPage.goToOrderConfirmationPage();

        List<OrderedItem> itemsInConfirmationPage = confirmationPage.getOrderedItems();
        String confirmationMessage = confirmationPage.getConfirmationMessage();
        BigDecimal totalPrice = confirmationPage.getTotalPrice();

        for (int i = 0; i < itemsInConfirmationPage.size(); i++) {
            softAssert.assertEquals(itemsInConfirmationPage.get(i).toString(), orderedItems.get(i).toString());
        }
        softAssert.assertEquals(confirmationMessage, "YOUR ORDER IS CONFIRMED", "Confirmation message not found");
        softAssert.assertEquals(totalPrice, purchaseTotalSummary, "Total price differ");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = {"addSecondItemToCardTest"})
    public void logOutTest() {

        // Logout, check you've been successfully logged out
        SignInPage signInPage = new OrderConfirmedPage().getNavigationBar().logout();
        Assert.assertFalse(signInPage.getNavigationBar().isLogOutButtonVisible(), "Log Out button is visible when should not");
        Assert.assertFalse(signInPage.getNavigationBar().isAccountButtonVisible(), "Account button is visible when should not");
        Assert.assertTrue(signInPage.isLoginButtonAvailable(), "Login button not visible");

    }


}
