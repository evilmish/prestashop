package utils;

import com.codeborne.selenide.Condition;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility Class");
    }

    public static BigDecimal parseAmountWithCurrencyToBigDecimal(String euro) {
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(euro);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return BigDecimal.ZERO;
    }

    // Overlay that appears after switching categories or going to other pages
    public static void waitOverlayToDisappear() {
        $(".faceted-overlay").waitUntil(Condition.disappears, 1000);
    }

}
