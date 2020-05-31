package pages.order_info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderedItem {
    private String itemName;
    private String itemColor;
    private BigDecimal itemPrice;
    private BigDecimal itemTotalPrice;
    private int itemQuantity;

    public OrderedItem(String itemName,
                       String itemColor,
                       BigDecimal itemPrice,
                       int itemQuantity,
                       BigDecimal itemTotalPrice) {
        this.itemName = itemName;
        this.itemColor = itemColor;
        this.itemPrice = itemPrice;
        this.itemTotalPrice = itemTotalPrice;
        this.itemQuantity = itemQuantity;
    }

    @Override
    public String toString() {
        return  "Name: " + itemName +
                ", Color: " + itemColor +
                ", Price: " + itemPrice +
                ", Quantity: " + itemQuantity +
                ", Total Price: " + itemTotalPrice;
    }
}
