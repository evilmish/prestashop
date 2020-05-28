package pages;

import utils.SocialTitle;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class NewAccountCreationPage{

    public NewAccountCreationPage choseSocialTitle(SocialTitle title) {
        if (title == SocialTitle.MR) {
            $("[name='id_gender'][value='1']").click();
        } else {
            $("[name='id_gender'][value='2']").click();
        }
        return this;
    }

    public NewAccountCreationPage enterFirstName(String firstName) {
        $("[name='firstname']").scrollTo().setValue(firstName);
        return this;
    }

    public NewAccountCreationPage enterLastName(String lastName) {
        $("[name='lastname']").scrollTo().setValue(lastName);
        return this;
    }

    public NewAccountCreationPage enterEmail(String email) {
        $("[name='email']").scrollTo().setValue(email);
        return this;
    }

    public NewAccountCreationPage enterPassword(String password) {
        $("[name='password']").scrollTo().setValue(password);
        return this;
    }

    public NewAccountCreationPage agreeWithTerms() {
        $("[name='psgdpr']").click();
        return this;
    }

    public MainShopPage submitRegistrationForm() {
        $("[class*=form-control-submit]").click();
        return page(MainShopPage.class);
    }
}
