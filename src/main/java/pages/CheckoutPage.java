package pages;

import com.codeborne.selenide.SelenideElement;
import utils.Utils;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static utils.Utils.waitOverlayToDisappear;

public class CheckoutPage {

    public CheckoutPage enterAddress(String address) {
        $("[name='address1']").scrollTo().setValue(address);
        return this;
    }

    public CheckoutPage enterPostCode(String postcode) {
        $("[name='postcode']").scrollTo().setValue(postcode);
        return this;
    }

    public CheckoutPage enterCity(String city) {
        $("[name='city']").scrollTo().setValue(city);
        return this;
    }

    public CheckoutPage choseFirstDeliveryOption() {
        SelenideElement firstDeliveryOption = $(".delivery-options [type='radio']");
        if (!firstDeliveryOption.isSelected()) {
            firstDeliveryOption.click();
        }
        return this;
    }

    public CheckoutPage chosePayByCheckPaymentMethod() {
        $("#payment-option-1").click();
        return this;
    }

    public CheckoutPage continueToShippingMethod() {
        $("[name=confirm-addresses]").click();
        return this;
    }

    public CheckoutPage continueToPaymentMethod() {
        $("[name=confirmDeliveryOption]").click();
        return this;
    }

    public CheckoutPage agreeWithTerms() {
        $("#conditions-to-approve input").click();
        return this;
    }

    public OrderConfirmationPage goToOrderConfirmationPage() {
        $("#payment-confirmation").click();
        waitOverlayToDisappear();
        return page(OrderConfirmationPage.class);
    }

    public BigDecimal getTotalPrice() {
        String totalPrice = $("#payment-option-1-additional-information dd:first-of-type").getText();
        return Utils.parsePriceToBigDecimal(totalPrice);
    }
}
