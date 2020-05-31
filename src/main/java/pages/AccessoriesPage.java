package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.ColorCategories;
import enums.EnumCategories;
import enums.FilterCategories;
import utils.Utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.waitOverlayToDisappear;

public class AccessoriesPage {

    public ItemPage choseItem(int itemIndex) {
        getAllListedItems().get(itemIndex).click();
        return page(ItemPage.class);
    }

    public int getListedItemAmount() {
        return getAllListedItems().size();
    }

    public AccessoriesPage selectCheckBox(FilterCategories category, EnumCategories checkBox) {
        waitOverlayToDisappear();
        ElementsCollection checkBoxes = getFilterCategory(category).findAll("li");
        SelenideElement checkBoxElement = checkBoxes.stream()
                .filter(el -> el.find("a").getText().contains(checkBox.getValue()))
                .findFirst().orElse(checkBoxes.get(0));

        checkBoxElement.find(".custom-checkbox").scrollTo().click();
        return this;
    }

    public AccessoriesPage changePriceRange(BigDecimal newMinPrice, BigDecimal newMaxPrice) {
        Map<String, Integer> pixelOffsetsToMoveBy = getPixelOffsetForNewMinMaxPrice(newMinPrice, newMaxPrice);

        SelenideElement leftHandle = $("[class^='ui-slider-handle']");
        actions()
                .dragAndDropBy(leftHandle, pixelOffsetsToMoveBy.get("moveByMin"), 0)
                .release()
                .perform();

        waitOverlayToDisappear();

        SelenideElement rightHandle = $("[class^='ui-slider-handle']:last-of-type");
        actions()
                .dragAndDropBy(rightHandle, pixelOffsetsToMoveBy.get("moveByMax"), 0)
                .release()
                .perform();

        return this;
    }

    public boolean isListedItemPricesBetweenRange(BigDecimal minPrice, BigDecimal maxPrice) {
        waitOverlayToDisappear();
        return getAllListedItems().stream()
                .map(item -> item.find(".price").getText())
                .map(Utils::parsePriceToBigDecimal)
                .map(value -> value.compareTo(minPrice) >= 0 && value.compareTo(maxPrice) <= 0)
                .filter(result -> !result)
                .findFirst()
                .orElse(true);
    }

    public boolean isListedItemColorsMatch(ColorCategories color) {
        waitOverlayToDisappear();
        return getAllListedItems().stream()
                .map(item -> item.findAll(".variant-links a"))
                .map(variants -> variants.stream().map(SelenideElement::getText).collect(Collectors.toList()))
                .map(colors -> colors.contains(color.getValue()))
                .filter(result -> !result)
                .findFirst()
                .orElse(true);
    }

    private ElementsCollection getAllListedItems() {
        return $$(".product-miniature");
    }

    private SelenideElement getFilterCategory(FilterCategories category) {
        ElementsCollection allCategories = $$("section.facet.clearfix");
        return allCategories.stream()
                .filter(el -> el.find("p").getText().contentEquals(category.getCategoryText()))
                .findFirst().orElse(allCategories.get(0));
    }

    private Map<String, Integer> getListWithActualMinAndMaxPrice() {
        SelenideElement priceElement = getFilterCategory(FilterCategories.PRICE)
                .scrollIntoView(true);
        String[] priceRange = priceElement.find("li p").getText().split(" - ");
        int initialMinPrice = Utils.parsePriceToBigDecimal(priceRange[0]).intValue();
        int initialMaxPrice = Utils.parsePriceToBigDecimal(priceRange[1]).intValue();

        return Map.of(
                "minPrice", initialMinPrice,
                "maxPrice", initialMaxPrice
        );
    }

    private Map<String, Integer> getPixelOffsetForNewMinMaxPrice(BigDecimal newMinPrice, BigDecimal newMaxPrice) {
        double slideWidthInPixels = $(".ui-slider-range").getSize().getWidth();
        Map<String, Integer> minAndMaxPrices = getListWithActualMinAndMaxPrice();
        int initialMinPrice = minAndMaxPrices.get("minPrice");
        int initialMaxPrice = minAndMaxPrices.get("maxPrice");

        int difBetweenMinAndMax = initialMaxPrice - initialMinPrice;
        double pixelsPerCurrencyUnit = slideWidthInPixels / difBetweenMinAndMax;

        double moveByMin = (newMinPrice.intValue() - initialMinPrice) * pixelsPerCurrencyUnit;
        double moveByMax = (newMaxPrice.intValue() - initialMaxPrice) * pixelsPerCurrencyUnit;

        return Map.of(
                "moveByMin", (int) moveByMin,
                "moveByMax", (int) moveByMax
        );
    }
}
