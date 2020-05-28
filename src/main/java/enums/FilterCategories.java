package enums;

import lombok.Getter;

@Getter
public enum FilterCategories {
    CATEGORY("Categories"),
    COLOR("Color"),
    PROPERTY("Property"),
    COMPOSITION("Composition"),
    BRAND("Brand"),
    PRICE("Price"),
    PAPER_TYPE("");

    private final String categoryText;

    FilterCategories(String categoryText) {
        this.categoryText = categoryText;
    }

}

