package pages;

import lombok.Getter;
import pages.fragments.HeaderNavigationFragment;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SignInPage {

    @Getter
    private HeaderNavigationFragment navigationBar = page(HeaderNavigationFragment.class);

    public NewAccountCreationPage goToAccountCreationPage() {
        $("[data-link-action='display-register-form']").click();
        return page(NewAccountCreationPage.class);
    }

    public boolean isLoginButtonAvailable() {
        return $("#submit-login").isDisplayed();
    }
}
