package nextstep.core;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final BeanContainer beanContainer;

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this.beanContainer = new BeanContainer();
        this.beanContainer.addBeans(componentScan(configurationClass.getPackageName()));
    }

    private List<BeanDefinition> componentScan(String basePackage) {
        return ComponentLoader.load(basePackage);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return beanContainer.getBean(type);
    }

    @Override
    public List<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanContainer.getBeansWithAnnotation(annotation);
    }

    @Override
    public void insertBean(Object bean) {
        final Class<?> beanClass = bean.getClass();
        beanContainer.addBeans(new BeanDefinition(beanClass, bean));
    }
}
