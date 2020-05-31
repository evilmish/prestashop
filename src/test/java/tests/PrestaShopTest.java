package tests;

import enums.ColorCategories;
import enums.FilterCategories;
import enums.Title;
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
    private BigDecimal purchaseTotalPrice;
    private int randomItem;
    private int listedItemAmount;
    private List<OrderedItem> orderedItemsInCart;

    @Test
    public void registerAccountTest() {

        String name = "Johnny";
        String surName = "Knoxville";

        //small hack to have always new email so we dont face issue with "email already exists"
        String randomString = RandomStringUtils.random(7, true, false);
        String email = name + "." + surName + "@" + randomString + ".com";

        // Register an account, check you're logged in
        AccountCreationPage accountCreatingPage = getMainPage()
                .getNavigationBar()
                .goToLoginPage()
                .goToAccountCreationPage();

        accountCreatingPage
                .choseTitle(Title.MR)
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
        BigDecimal newMinPrice = BigDecimal.valueOf(18);
        BigDecimal newMaxPrice = BigDecimal.valueOf(23);

        // Open "Accessories" section
        AccessoriesPage accessories = getMainPage()
                .getNavigationBar()
                .goToAccessoriesPage();

        // Filter out items of white colour within price range 18-23
        accessories
                .changePriceRange(newMinPrice, newMaxPrice)
                .selectCheckBox(FilterCategories.COLOR, ColorCategories.WHITE);

        // Check the items are correctly filtered
        softAssert.assertTrue(accessories.isListedItemPricesBetweenRange(newMinPrice, newMaxPrice),
                "Some item prices are outside range - min: " + newMinPrice + " max: " + newMaxPrice);
        softAssert.assertTrue(accessories.isListedItemColorsMatch(ColorCategories.WHITE),
                "Some item color do not match: " + ColorCategories.WHITE.getValue());

        // Randomly choose one of items, increase quantity of items and add to cart
        listedItemAmount = accessories.getListedItemAmount();
        randomItem = RandomUtils.nextInt(0, listedItemAmount);

        CartPage cartPage = accessories
                .choseItem(randomItem)
                .increaseQuantityBy(6)
                .addToCart()
                .goToCheckoutPage();

        // Check a price is correctly calculated
        int addedItemQuantity = cartPage.getItemQuantity(0);
        BigDecimal addedItemPrice = cartPage.getItemPrice(0);
        BigDecimal addedItemTotalPrice = cartPage.getItemTotalPrice(0);
        BigDecimal addedItemExpectedTotalPrice = addedItemPrice.multiply(BigDecimal.valueOf(addedItemQuantity));
        purchaseTotalPrice = cartPage.getPurchaseTotalPrice();

        softAssert.assertEquals(addedItemTotalPrice,
                addedItemExpectedTotalPrice,
                "Actual and expected total prices differ");
        softAssert.assertEquals(addedItemTotalPrice,
                purchaseTotalPrice,
                "Total price differs from added item total price");
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = {"addFirstItemToCartTest"})
    public void addSecondItemToCardTest() {

        // Go back to filtered list of items, choose one more item
        // Go to cart
        randomItem = RandomUtils.nextInt(0, listedItemAmount);
        CartPage cartPage = new CartPage()
                .goBackToSortedAccessoriesPage()
                .choseItem(randomItem)
                .addToCart()
                .goToCheckoutPage();

        //Check a total price is correctly calculated
        BigDecimal allItemTotalPrice = cartPage.getAllItemTotalPrice();
        purchaseTotalPrice = cartPage.getPurchaseTotalPrice();
        // Needed for next tests to compare with checkout page
        orderedItemsInCart = cartPage.getOrderedItemsList();

        Assert.assertEquals(allItemTotalPrice,
                purchaseTotalPrice,
                "Total price of all items differ from price in summary");
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
                purchaseTotalPrice,
                "Checkout total price differs from purchased item price");

        // Confirm your order and check order details
        OrderConfirmationPage confirmationPage = checkoutPage.goToOrderConfirmationPage();

        List<OrderedItem> orderedItemsInConfirmationPage = confirmationPage.getOrderedItems();
        String confirmationMessage = confirmationPage.getConfirmationMessage();
        BigDecimal confirmationTotalPrice = confirmationPage.getTotalPrice();

        for (int i = 0; i < orderedItemsInConfirmationPage.size(); i++) {
            softAssert.assertEquals(orderedItemsInConfirmationPage.get(i).toString(), orderedItemsInCart.get(i).toString());
        }
        softAssert.assertEquals(confirmationMessage, "YOUR ORDER IS CONFIRMED",
                "Confirmation message not found");
        softAssert.assertEquals(confirmationTotalPrice, purchaseTotalPrice,
                "Confirmation total price differs from purchase total price");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = {"addSecondItemToCardTest"})
    public void logOutTest() {

        // Logout, check you've been successfully logged out
        LoginPage loginPage = new OrderConfirmationPage().getNavigationBar().logout();
        Assert.assertFalse(loginPage.getNavigationBar().isLogOutButtonVisible(),
                "Log out button is visible when should not");
        Assert.assertFalse(loginPage.getNavigationBar().isAccountButtonVisible(),
                "Account button is visible when should not");
        Assert.assertTrue(loginPage.isLoginButtonAvailable(),
                "Login button not visible");
    }
}
