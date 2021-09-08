package nextstep.mvc;

import nextstep.mvc.exception.GenerateBeanException;

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
            throw new GenerateBeanException("빈 생성을 실패하였습니다.");
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
