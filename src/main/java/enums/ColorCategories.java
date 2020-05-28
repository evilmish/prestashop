package enums;

import lombok.Getter;

@Getter
public enum ColorCategories implements EnumCategories {
    WHITE("White"),
    BLACK("Black");
    private final String color;
    ColorCategories(String color){
        this.color = color;
    }

    @Override
    public String returnEnumValue() {
        return this.color;
    }
}

