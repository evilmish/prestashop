package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class ItemPage {

    public ItemPage increaseQuantityBy(int increaseBy) {
        for (int i = 0; i < increaseBy; i++) {
            $("[class$=touchspin-up]").click();
        }
        return this;
    }

    public ItemPage addToCart() {
        $("[data-button-action='add-to-cart']").click();
        return this;
    }

    public CartPage proceedToCheckout() {
        $("[class$='btn-primary']").scrollTo().click();
        return page(CartPage.class);
    }

}
