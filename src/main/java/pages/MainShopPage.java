package pages;

import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MainShopPage {

    @Getter
    private HeaderNavigationFragment navigationBar = page(HeaderNavigationFragment.class);

    public String getLoggedUserNameAndSurname() {
        return $(".account span").getText();
    }
}
