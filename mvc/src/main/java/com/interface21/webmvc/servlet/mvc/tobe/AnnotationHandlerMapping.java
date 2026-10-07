package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
뭐가뭔지 모르겠으니 [아이큐](https://github.com/woowacourse/java-mvc/pull/1152) 의 주석을 참고할게요
-- 어노테이션을 읽어서 [경로] + [메서드] => [실행 대상] 관계를 등록/조회한다.
-- 조회된 [실행 대상], 컨트롤러 메서드의 실행 자체는 HandlerExecution 의 책임.
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // 조회의 범위. 어느 패키지들을 조회할 것인지. reflections 의 파라미터로 해당 범위에 속한 클래스를 조회한다.
    private final Object[] basePackage;
    // HandlerKey - 요청의 경로와 HTTP 메서드를 관리. 예: HandlerKey("/get-test", GET)
    // HandlerExecution - 실행할 컨트롤러 객체와 Method 정보를 관리.
    private Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        // TODO 매퍼를 초기화하며 컨트롤러들을 등록 - HandlerExecution 구현 필요?
        for (Object basePackage : basePackage) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            Map<Class<?>, Object> controllers = generateControllers(controllerClasses);
            controllers.forEach(this::registerControllers);
        }
    }

    private void registerControllers(Class<?> controllerClass, Object controller) {
    }

    private Map<Class<?>, Object> generateControllers(Set<Class<?>> controllers) {
//        return controllers.stream()
//                .collect(Collectors.toMap(
//                        controller -> controller,
//                        this::instantiateController
//                ));
        Map<Class<?>, Object> controllerMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            Object instance = instantiateController(controller);
            controllerMap.put(controller, instance);
        }
        return controllerMap;
    }

    private Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controller.getName(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
//        TODO request 에 알맞은 핸들러를 반환해야 한다.
        return new HandlerExecution();
    }
}
