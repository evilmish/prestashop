package pages;

import lombok.Getter;
import pages.fragments.NavigationFragment;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {

    @Getter
    private NavigationFragment navigationBar = page(NavigationFragment.class);

    public AccountCreationPage goToAccountCreationPage() {
        $(".no-account a").click();
        return page(AccountCreationPage.class);
    }

    public boolean isLoginButtonAvailable() {
        return $("#submit-login").isDisplayed();
    }
}
