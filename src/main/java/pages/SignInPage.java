package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SignInPage{

    public NewAccountCreationPage goToAccountCreationPage(){
        $("[data-link-action='display-register-form']").click();
        return page(NewAccountCreationPage.class);
    }
}
