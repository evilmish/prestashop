package utils;

import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility Class");
    }

    public static BigDecimal parseEuroToBigDecimal(String euro) {
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(euro);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return BigDecimal.ZERO;
    }

    public static int returnNewRandomNumberIfItsNotProvidedNumber(int providedNumber, int maxNumber) {
        int newNumber = providedNumber;
        while (providedNumber == newNumber) {
            newNumber = RandomUtils.nextInt(0, maxNumber);
        }
        return newNumber;
    }

    public static void waitOverlayDisappears() {
        $(".faceted-overlay").waitUntil(Condition.disappears, 1000);
    }

}
