package nextstep.mvc;

import nextstep.mvc.exception.ControllerScanException;

public class BeanDefinition {
    private final Class<?> clazz;
    private final Object bean;

    public BeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.bean = classToBean(clazz);
    }

    private Object classToBean(Class<?> beanType) {
        try {
            return beanType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ControllerScanException("어노테이션 기반 Controller를 찾는데 실패하였습니다.");
        }
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public String toString() {
        return "clazz=" + clazz;
    }
}
