package webmvc.org.springframework.web.servlet.view;

import java.util.Arrays;

public enum ViewType {

    JSP,
    JSON;

    public static ViewType from(String type) {
        return Arrays.stream(ViewType.values())
                .filter(viewType -> viewType.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow();
    }
}
