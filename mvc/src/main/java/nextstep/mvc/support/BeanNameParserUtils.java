package nextstep.mvc.support;

import java.util.Locale;

public class BeanNameParserUtils {

    public static String toLowerFirstChar(String name){
        String first = name.substring(0,1);
        return name.replaceFirst(first, first.toLowerCase(Locale.ROOT));
    }
}
