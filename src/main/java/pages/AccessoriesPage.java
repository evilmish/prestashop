package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.EnumCategories;
import enums.FilterCategories;
import utils.Utils;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.waitOverlayToDisappear;

public class AccessoriesPage {

    public ItemPage choseItem(int itemIndex) {
        getAllListedItems().get(itemIndex).click();
        return page(ItemPage.class);
    }

    public int returnAmountOfListedItems() {
        return getAllListedItems().size();
    }

    private ElementsCollection getAllListedItems() {
        return $$("[class^='product-miniature']");
    }

    private SelenideElement getFilterCategory(FilterCategories category) {
        ElementsCollection allElements = $$("section[class='facet clearfix']");
        return allElements.stream()
                .filter(el -> el.find("p").getText().contentEquals(category.getCategoryText()))
                .findFirst().orElse(allElements.get(0));
    }

    public AccessoriesPage selectSpecificCheckBox(FilterCategories category, EnumCategories checkBox) {
        waitOverlayToDisappear();
        ElementsCollection checkBoxes = getFilterCategory(category).findAll("li");
        SelenideElement checkBoxElement = checkBoxes.stream()
                .filter(el -> el.find("a").getText().contains(checkBox.returnEnumValue()))
                .findFirst().orElse(checkBoxes.get(0));

        checkBoxElement.find(".custom-checkbox").scrollTo().click();

        return this;
    }

    public AccessoriesPage changePriceRange(int newMinPrice, int newMaxPrice) {
        int[] pixelOffsetsToMoveBy = getPixelOffsetForNewMinMaxPrice(newMinPrice, newMaxPrice);

        SelenideElement leftHandle = $("[class^='ui-slider-handle']");
        actions()
                .dragAndDropBy(leftHandle, pixelOffsetsToMoveBy[0], 0)
                .release()
                .perform();

        waitOverlayToDisappear();

        SelenideElement rightHandle = $("[class^='ui-slider-handle']:last-of-type");
        actions()
                .dragAndDropBy(rightHandle, pixelOffsetsToMoveBy[1], 0)
                .release()
                .perform();

        return this;
    }

    public boolean isAllListedItemPriceLessThanProvided(BigDecimal providedMinPrice, BigDecimal providedMaxPrice) {
        return getAllListedItems().stream()
                .map(item -> item.find(".price").getText())
                .collect(Collectors.toList())
                .stream()
                .map(Utils::parseAmountWithCurrencyToBigDecimal)
                .map(value -> value.compareTo(providedMinPrice) >= 0 && value.compareTo(providedMaxPrice) <= 0)
                .filter(result -> !result)
                .findFirst()
                .orElse(true);
    }

    private int[] getListWithActualMinAndMaxPrice() {
        SelenideElement priceElement = getFilterCategory(FilterCategories.PRICE);
        priceElement.scrollIntoView(true);
        String[] priceRange = priceElement.find("li p").getText().split(" - ");
        int initialMinPrice = Utils.parseAmountWithCurrencyToBigDecimal(priceRange[0]).intValue();
        int initialMaxPrice = Utils.parseAmountWithCurrencyToBigDecimal(priceRange[1]).intValue();

        return new int[]{initialMinPrice, initialMaxPrice};
    }

    private int[] getPixelOffsetForNewMinMaxPrice(int newMinPrice, int newMaxPrice) {
        double slideWidthInPixels = $("[class^='ui-slider-range']").getSize().getWidth();
        int[] minAndMaxPrices = getListWithActualMinAndMaxPrice();
        int initialMinPrice = minAndMaxPrices[0];
        int initialMaxPrice = minAndMaxPrices[1];

        int difBetweenMinAndMax = initialMaxPrice - initialMinPrice;
        double pixelPerOneEuro = slideWidthInPixels / difBetweenMinAndMax;

        double moveByMin = (newMinPrice - initialMinPrice) * pixelPerOneEuro;
        double moveByMax = (newMaxPrice - initialMaxPrice) * pixelPerOneEuro;

        return new int[]{(int) moveByMin, (int) moveByMax};

    }


}
