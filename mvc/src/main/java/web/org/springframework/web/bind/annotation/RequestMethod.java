package web.org.springframework.web.bind.annotation;

import web.org.springframework.web.exception.RequestMethodNotFoundException;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod get(String value) {
        try {
            return RequestMethod.valueOf(value);
        } catch (IllegalArgumentException e){
            throw new RequestMethodNotFoundException();
        }
    }
}
