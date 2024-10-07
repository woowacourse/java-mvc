package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import reflection.annotation.Controller;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .filter(this::isBeanObject)
                .map(this::createBean)
                .collect(Collectors.toSet());
    }

    private boolean isBeanObject(Class<?> aClass) {
        System.out.println(aClass.getName());
        return aClass.isAnnotationPresent(Controller.class) || aClass.isAnnotationPresent(Service.class) ||
                aClass.isAnnotationPresent(Repository.class);
    }

    private Object createBean(Class<?> aClass) {
        Constructor<?> constructor = aClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Function<Object, Object> functionWrapper = FunctionWrapper.apply(
                function -> constructor.newInstance()
        );
        return functionWrapper.apply(constructor);
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {
        System.out.println("필드실행");
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Class<?> type = declaredField.getType();
            declaredField.setAccessible(true);
            for (Object candidateBean : beans) {
                System.out.println("빈찾기");
                System.out.println(candidateBean.getClass().getName());
                if(type.isInstance(candidateBean)){
                    System.out.println("후보찾움");
                    Consumer<Object> fieldInjector = ConsumerWrapper.accept(
                            injectableBean -> declaredField.set(bean, injectableBean)
                    );
                    fieldInjector.accept(candidateBean);
                }
            }
        }
    }

    public static DIContainer createContextForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }

}
