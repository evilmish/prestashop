package pages;

import com.codeborne.selenide.ElementsCollection;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.*;
import static java.math.BigDecimal.ZERO;
import static utils.Utils.parseAmountWithCurrencyToBigDecimal;

public class CartPage {

    public ElementsCollection getAllItemsInACart() {
        return $$(".cart-item");
    }

    public int getItemAmount(int item) {
        String quantity = getAllItemsInACart().get(item).find("input").attr("value");
        return Integer.parseInt(quantity);
    }

    public BigDecimal getItemPrice(int item) {
        String price = getAllItemsInACart().get(item).find(".price").getText();
        return parseAmountWithCurrencyToBigDecimal(price);
    }

    public BigDecimal getItemTotalPrice(int item) {
        String totalPrice = getAllItemsInACart().get(item).find(".price strong").getText();
        return parseAmountWithCurrencyToBigDecimal(totalPrice);

    }

    public BigDecimal getPurchaseTotalPrice() {
        String totalPrice = $("[class$='cart-summary-totals'] [class='value']").getText();
        return parseAmountWithCurrencyToBigDecimal(totalPrice);
    }

    public CheckoutPage goToCheckOutPage() {
        $("[class$='btn-primary']").click();
        return page(CheckoutPage.class);
    }

    public AccessoriesPage goBackToSortedAccessoriesPage() {
        back();
        back();
        switchTo().frame("framelive");
        return page(AccessoriesPage.class);
    }

    public BigDecimal getAllItemTotalPrice() {
        int itemQuantity = getAllItemsInACart().size();
        BigDecimal sumOfTotalPrices = ZERO;
        for (int i = 0; i < itemQuantity; i++) {
            sumOfTotalPrices = sumOfTotalPrices.add(getItemTotalPrice(i));
        }
        return sumOfTotalPrices;
    }

}
