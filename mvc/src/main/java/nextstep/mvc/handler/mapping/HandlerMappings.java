package nextstep.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private static final Logger LOG = LoggerFactory.getLogger(HandlerMappings.class);

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        LOG.debug("Request Mapping Uri : {}", request.getRequestURI());
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("request에 해당하는 handler를 찾을 수 없습니다."));
    }
}
