package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(String name){
        for (RequestMethod rm : RequestMethod.values()){
            if(rm.toString().equals(name)){
                return rm;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 메서드입니다.");
    }
}
