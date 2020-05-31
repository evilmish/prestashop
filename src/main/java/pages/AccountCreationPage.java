package pages;

import enums.Title;

import static com.codeborne.selenide.Selenide.$;

public class AccountCreationPage {

    public AccountCreationPage choseTitle(Title title) {
        if (title == Title.MR) {
            $("[name='id_gender'][value='1']").click();
        } else {
            $("[name='id_gender'][value='2']").click();
        }
        return this;
    }

    public AccountCreationPage enterFirstName(String firstName) {
        $("[name='firstname']").scrollTo().setValue(firstName);
        return this;
    }

    public AccountCreationPage enterLastName(String lastName) {
        $("[name='lastname']").scrollTo().setValue(lastName);
        return this;
    }

    public AccountCreationPage enterEmail(String email) {
        $("[name='email']").scrollTo().setValue(email);
        return this;
    }

    public AccountCreationPage enterPassword(String password) {
        $("[name='password']").scrollTo().setValue(password);
        return this;
    }

    public AccountCreationPage agreeWithTerms() {
        $("[name='psgdpr']").click();
        return this;
    }

    public void submitRegistrationForm() {
        $("[class*=form-control-submit]").click();
    }
}
