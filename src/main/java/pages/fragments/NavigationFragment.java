package pages.fragments;

import pages.AccessoriesPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class NavigationFragment {

    private static final String ACCOUNT_BUTTON_LOCATOR = ".account span";

    public LoginPage goToLoginPage() {
        $(".user-info").click();
        return page(LoginPage.class);
    }

    public AccessoriesPage goToAccessoriesPage() {
        $("#category-6").click();
        return page(AccessoriesPage.class);
    }

    public LoginPage logout() {
        $(".logout").click();
        return page(LoginPage.class);
    }

    public boolean isLogOutButtonVisible() {
        return $(".logout").isDisplayed();
    }

    public boolean isAccountButtonVisible() {
        return $(ACCOUNT_BUTTON_LOCATOR).isDisplayed();
    }

    public String getLoggedUserNameAndSurname() {
        return $(ACCOUNT_BUTTON_LOCATOR).getText();
    }
}
