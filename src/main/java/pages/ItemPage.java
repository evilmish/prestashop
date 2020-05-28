package pages;

import static com.codeborne.selenide.Selenide.*;

public class ItemPage {

    public ItemPage increaseQuantityBy(int increaseBy){
        // As alternative we can just change total quantity but here will check that clicking on arrow is possible
        for(int i = 0; i < increaseBy; i++){
            $("[class$=touchspin-up]").click();
        }
        return  this;
    }

    public ItemPage addToCart (){
        $("[data-button-action='add-to-cart']").click();
        return this;
    }

    public CartPage proceedToCheckout (){
        $("[class$='btn-primary']").scrollTo().click();
        return page(CartPage.class);
    }

}
