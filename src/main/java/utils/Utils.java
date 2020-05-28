package utils;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static BigDecimal parseEuroToBigDecimal(String euro){
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(euro);
        if(matcher.find()){
            return new BigDecimal(matcher.group(1));
        }
        return BigDecimal.ZERO;
    }

    public static int returnNewRandomNumberIfItsNotProvidedNumber(int providedNumber, int maxNumber){
        int newNumber = providedNumber;
        while(providedNumber == newNumber){
            newNumber = RandomUtils.nextInt(0, maxNumber);
        }
        return newNumber;
    }

}
