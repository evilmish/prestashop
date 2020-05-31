package pages;

import com.codeborne.selenide.ElementsCollection;
import lombok.Getter;
import pages.fragments.NavigationFragment;
import pages.order_info.OrderedItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.parsePriceToBigDecimal;

public class OrderConfirmationPage {

    @Getter
    private NavigationFragment navigationBar = page(NavigationFragment.class);

    public String getConfirmationMessage() {
        return $(".h1.card-title").getText().replace("\uE876", "");
    }

    public BigDecimal getTotalPrice() {
        return parsePriceToBigDecimal($(".total-value").getText());
    }

    public List<OrderedItem> getOrderedItems() {

        ElementsCollection orderedItems = $$(".order-line.row");
        ArrayList<OrderedItem> orderedItemList = new ArrayList<>();

        orderedItems.forEach(item -> {
            ElementsCollection itemInfo = item.findAll("[class^=col-sm]");
            String productDescription = itemInfo.get(1).find("span").getText();
            Map<String, String> parsedNameAndColor = parseItemNameAndColor(productDescription);

            ElementsCollection itemPriceAndQuantity = itemInfo.get(2).findAll("[class^=col-xs]");
            BigDecimal itemPrice = parsePriceToBigDecimal(itemPriceAndQuantity.get(0).getText());
            BigDecimal itemTotalPrice = parsePriceToBigDecimal(itemPriceAndQuantity.get(2).getText());
            int itemQuantity = Integer.parseInt(itemPriceAndQuantity.get(1).getText());

            orderedItemList.add(new OrderedItem(
                    parsedNameAndColor.get("name"),
                    parsedNameAndColor.get("color"),
                    itemPrice,
                    itemQuantity,
                    itemTotalPrice)
            );
        });

        return orderedItemList;
    }

    private Map<String, String> parseItemNameAndColor(String itemDescription) {
        itemDescription = itemDescription.replace(" - Color : ", ":");
        Pattern pattern = Pattern.compile("([a-zA-Z\\s]+)");
        Matcher matcher = pattern.matcher(itemDescription);
        ArrayList<String> parsedNameAndColor = new ArrayList<>();

        while (matcher.find()) {
            parsedNameAndColor.add(matcher.group());
        }
        if (parsedNameAndColor.size() == 1) {
            parsedNameAndColor.add("");
        }
        return Map.of(
                "name", parsedNameAndColor.get(0),
                "color", parsedNameAndColor.get(1)
        );
    }
}
