package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappings.class);

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        log.info("requestURI = {}", request.getRequestURI());

        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("요청에 맞는 Handler 를 찾지 못하였습니다"));
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }
}
