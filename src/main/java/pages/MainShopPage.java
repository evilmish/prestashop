package pages;

import lombok.Getter;
import pages.fragments.NavigationFragment;

import static com.codeborne.selenide.Selenide.page;

public class MainShopPage {

    @Getter
    private NavigationFragment navigationBar = page(NavigationFragment.class);

}
