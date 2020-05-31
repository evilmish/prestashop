package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import pages.order_info.OrderedItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static java.math.BigDecimal.ZERO;
import static utils.Utils.parseAmountWithCurrencyToBigDecimal;

public class CartPage {

    public String getItemName(int item) {
        return getAllItemsInACart().get(item).find(".product-line-info a").getText();
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

    public String getItemAdditionalInfo(int item) {
        return getAllItemsInACart().get(item).find(".value").getText();
    }

    public BigDecimal getPurchaseTotalPrice() {
        String totalPrice = $("[class$='cart-summary-totals'] [class='value']")
                .shouldBe(Condition.visible)
                .getText();
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

    public List<OrderedItem> getOrderedItemsList() {
        ElementsCollection itemQuantity = getAllItemsInACart();
        ArrayList<OrderedItem> itemList = new ArrayList<>();

        for (int i = 0; i < itemQuantity.size(); i++) {
            itemList.add(new OrderedItem(
                    getItemName(i),
                    getItemAdditionalInfo(i),
                    getItemPrice(i),
                    getItemAmount(i),
                    getItemTotalPrice(i)));
        }

        return itemList;
    }

    private ElementsCollection getAllItemsInACart() {
        return $$(".cart-item");
    }


}
