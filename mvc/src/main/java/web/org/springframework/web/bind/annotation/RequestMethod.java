package web.org.springframework.web.bind.annotation;

public enum RequestMethod {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public boolean hasValue(String value) {
        return this.value.equals(value);
    }
}
