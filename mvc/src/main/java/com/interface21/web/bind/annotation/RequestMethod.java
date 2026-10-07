package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method){
        try {
            return RequestMethod.valueOf(method);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다 :" + method, e);
        }
    }
}
