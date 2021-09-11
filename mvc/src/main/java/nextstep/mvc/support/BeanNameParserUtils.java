package nextstep.mvc.support;

import java.util.Locale;

public class BeanNameParserUtils {

    private BeanNameParserUtils() {
    }

    public static String toLowerFirstChar(String name) {
        String firstChar = name.substring(0, 1);
        return name.replaceFirst(firstChar, firstChar.toLowerCase(Locale.ROOT));
    }
}
