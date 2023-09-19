package web.org.springframework.web.bind.annotation;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE
    ;

    public static RequestMethod from(String input) {
        try {
            return valueOf(input);
        } catch (Exception exception) {
            throw new IllegalArgumentException("잘못된 Method 입니다.");
        }
    }

}
