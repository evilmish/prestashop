package pages;

import lombok.Getter;
import pages.fragments.NavigationFragment;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static utils.Utils.waitOverlayToDisappear;

public class LoginPage {

    @Getter
    private NavigationFragment navigationBar = page(NavigationFragment.class);

    public AccountCreationPage goToAccountCreationPage() {
        $(".no-account a").click();
        waitOverlayToDisappear();
        return page(AccountCreationPage.class);
    }

    public boolean isLoginButtonAvailable() {
        return $("#submit-login").isDisplayed();
    }
}
