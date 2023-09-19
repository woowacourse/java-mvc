package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.Mapper;

import java.util.List;

public class HandlerMapping {

    List<Mapper> mappers;

    public HandlerMapping(final List<Mapper> mappers) {
        this.mappers = mappers;
    }

    public void initialize() {
        for(Mapper mapper : mappers) {
            mapper.initialize();
        }
    }

    public Object getHandler(HttpServletRequest httpServletRequest) throws ServletException {
        for(Mapper mapper : mappers) {
            mapper.initialize();
            Object handler = mapper.getHandler(httpServletRequest);
            if(handler!=null) {
                return handler;
            }
        }
        throw new ServletException("uri에 해당하는 핸들러가 없습니다");
    }
}
