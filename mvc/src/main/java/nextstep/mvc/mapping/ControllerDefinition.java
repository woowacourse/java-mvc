package nextstep.mvc.mapping;

import nextstep.mvc.exception.ControllerScanException;

public class ControllerDefinition {
    private final Class<?> clazz;
    private final Object controller;

    public ControllerDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.controller = classToBean(clazz);
    }

    private Object classToBean(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ControllerScanException("어노테이션 기반 Controller를 찾는데 실패하였습니다.");
        }
    }

    public Object getController() {
        return controller;
    }

    @Override
    public String toString() {
        return "clazz=" + clazz;
    }
}
