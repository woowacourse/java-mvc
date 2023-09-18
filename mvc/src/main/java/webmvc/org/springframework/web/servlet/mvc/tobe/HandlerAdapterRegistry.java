package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters = Arrays.stream(handlerAdapters).collect(Collectors.toList());
    }

    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.supports(handler))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 URL입니다."))
                              .handle(handler, request, response);
    }
}
