package pages.fragments;

import pages.AccessoriesPage;
import pages.SignInPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class HeaderNavigationFragment {
    public SignInPage goToSignInPage() {
        $(".user-info").click();
        return page(SignInPage.class);
    }

    public AccessoriesPage goToAccessoriesPage() {
        $("#category-6").click();
        return page(AccessoriesPage.class);
    }

    public SignInPage logout() {
        $(".logout").click();
        return page(SignInPage.class);
    }

    public boolean isLogOutButtonVisible() {
        return $(".logout").isDisplayed();
    }

    public boolean isAccountButtonVisible() {
        return $(".account span").isDisplayed();
    }

    public String getLoggedUserNameAndSurname() {
        return $(".account span").getText();
    }


}
