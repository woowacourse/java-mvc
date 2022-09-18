package nextstep.web.support;

public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private String methodName;

    RequestMethod(String methodName) {
        this.methodName = methodName;
    }
}
