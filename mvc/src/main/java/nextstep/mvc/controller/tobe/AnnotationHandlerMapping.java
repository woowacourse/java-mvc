package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        // basePackage를 스캔해서 @Controller가 달린 클래스(=컨트롤러)들을 찾아온다.
        // 컨트롤러에서 public 메서드들을 불러온다.
        // 메서드에서 @RequestMapping의 속성을 확인해 new HandlerKey(url, method) 와 해당 메서드를 실행하는 HandlerExecution을 생성한다.
        // handlerExecutions에 추가한다.
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        // request에서 url과 method를 가지고 HandlerKey를 생성한다.
        // handlerExecutions에서 HandlerKey로 HandlerExecution을 찾아와 리턴한다.
        return null;
    }
}
