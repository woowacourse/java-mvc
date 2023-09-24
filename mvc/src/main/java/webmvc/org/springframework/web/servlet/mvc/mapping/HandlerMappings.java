package webmvc.org.springframework.web.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void initialize() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        mappings.add(annotationHandlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("요청을 처리할 수 있는 컨트롤러가 존재하지 않습니다."));
    }

}
