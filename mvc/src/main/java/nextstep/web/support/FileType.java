package nextstep.web.support;

import java.util.Arrays;

public enum FileType {
    JSP(".jsp"),
    html(".html"),
    JS(".js"),
    CSS(".css"),
    SVG(".svg"),
    JSPF(".jspf"),
    ;

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public static boolean matches(String text) {
        return Arrays.stream(values())
                .anyMatch(it -> text.endsWith(it.value));
    }
}
