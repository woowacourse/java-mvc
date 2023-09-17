package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class CompositeHandlerMapping implements HandlerMapping {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public CompositeHandlerMapping(HandlerMapping... args) {
        handlerMappings.addAll(Arrays.asList(args));
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(HandlerMappingNotFoundException::new);
    }
}
