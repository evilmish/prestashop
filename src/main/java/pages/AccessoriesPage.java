package pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class AccessoriesPage {

    public ItemPage choseItem(int itemIndex){
        getAllListedItems().get(itemIndex).click();
        return page(ItemPage.class);
    }

    public int returnAmountOfListedItems(){
        return getAllListedItems().size();
    }

    private ElementsCollection getAllListedItems(){
        return $$("[class^='product-miniature']");
    }
}
