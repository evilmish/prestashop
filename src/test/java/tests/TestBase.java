package tests;

import com.codeborne.selenide.Condition;
import lombok.Getter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.MainShopPage;

import static com.codeborne.selenide.Selenide.*;

public class TestBase {

    @Getter
    private MainShopPage mainPage;

    @BeforeClass
    protected void openShopInitialPage() {
        // Go to http://demo.prestashop.com
        this.mainPage = open("http://demo.prestashop.com/", MainShopPage.class);
        switchTo().frame("framelive");
        $("[alt='PrestaShop']").waitUntil(Condition.visible, 12000);
    }

    @AfterClass
    protected void afterClass() {
        closeWebDriver();
    }

}
