package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner of(Object basePath) {
//            해당 경로의 리플렉션을 생성하고
        return new ControllerScanner(new Reflections(basePath));
    }

    Map<Class<?>, Object> getControllers() {
//            리플렉션으로 @Controller 어노테이션이 붙은 모든 클래스를 Set 으로 가져오고
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
//            그 `컨트롤러 클래스`로 `컨트롤러 인스턴스 객체`를 생성해 Key-Value 로 매핑한다
        return generateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> generateControllers(Set<Class<?>> controllers) {
//        깔끔은 한데 난해함
//        return controllers.stream()
//                .collect(Collectors.toMap(
//                        controller -> controller,
//                        this::instantiateController
//                ));
        Map<Class<?>, Object> controllerMap = new HashMap<>();
//        컨트롤러 클래스들을 순회하며
        for (Class<?> controller : controllers) {
//            해당 클래스로 컨트롤러 인스턴스 객체를 생성하고
            Object instance = instantiateController(controller);
//            클래스를 Key, 인스턴스 객체를 Value 로 저장
            controllerMap.put(controller, instance);
        }
//        모든 컨트롤러 클래스를 순회하고 맵을 반환
        return controllerMap;
    }

    private Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
//            만약 해당 클래스에 NoArgsConstructor 가 없으면 예외
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controller.getName(), e);
        }
    }

}
