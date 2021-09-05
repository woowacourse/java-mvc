package nextstep.web.support;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(String method) {
        return valueOf(method.toUpperCase());
    }
}
