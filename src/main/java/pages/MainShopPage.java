package pages;

import lombok.Getter;
import pages.fragments.HeaderNavigationFragment;

import static com.codeborne.selenide.Selenide.page;

public class MainShopPage {

    @Getter
    private HeaderNavigationFragment navigationBar = page(HeaderNavigationFragment.class);

}
