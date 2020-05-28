package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class HeaderNavigationFragment {
    public SignInPage goToSignInPage(){
        $(".user-info").click();
        return page(SignInPage.class);
    }

    public AccessoriesPage goToAccessoriesPage(){
        $("#category-6").click();
        return page(AccessoriesPage.class);
    }

    public CartPage goToCartPage(){
        $("#_mobile_cart").click();
        return page(CartPage.class);
    }
}
