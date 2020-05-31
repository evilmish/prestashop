package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static utils.Utils.waitOverlayToDisappear;

public class ItemPage {

    public ItemPage increaseQuantityBy(int increaseBy) {
        for (int i = 0; i < increaseBy; i++) {
            $("[class$=touchspin-up]").click();
        }
        return this;
    }

    public ItemPage addToCart() {
        $("[data-button-action='add-to-cart']")
                .scrollIntoView(true)
                .click();
        return this;
    }

    public CartPage goToCheckoutPage() {
        $("[class$='btn-primary']").scrollTo().click();
        waitOverlayToDisappear();
        return page(CartPage.class);
    }

}
