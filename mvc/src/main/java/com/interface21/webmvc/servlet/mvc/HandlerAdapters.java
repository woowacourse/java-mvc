package com.interface21.webmvc.servlet.mvc;

import com.interface21.NotFoundException;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        List<HandlerAdapter> objects = reflections.getSubTypesOf(HandlerAdapter.class).stream()
                .map(this::createHandlerAdapter)
                .toList();
        handlerAdapters.addAll(objects);
    }

    private HandlerAdapter createHandlerAdapter(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return (HandlerAdapter) instance;
        } catch (NoSuchMethodException e) {
            throw new NotFoundException("기본 생성자가 존재하지 않습니다");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow();
        return handlerAdapter.handle(request, response, handler);
    }
}
