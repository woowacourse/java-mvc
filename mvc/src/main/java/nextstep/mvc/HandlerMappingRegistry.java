package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import nextstep.mvc.controller.asis.Controller;

public class HandlerMappingRegistry {

    private ArrayList<HandlerMapping> handlerMappings;

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Controller getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .map(Controller.class::cast)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("요청을 처리할 핸들러를 찾을 수 없습니다."));
    }
}
