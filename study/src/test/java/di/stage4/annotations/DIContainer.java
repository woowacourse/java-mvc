package di.stage4.annotations;

import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Object> beans) {
        this.beans = beans;
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        final BeanInitializer beanInitializer = new BeanInitializer();
        beanInitializer.initialize(rootPackageName);
        beanInitializer.injectDependency();
        return new DIContainer(beanInitializer.getInstance());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
