package com.interface21.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.context.stereotype.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanRegistrarTest {

    @Component
    static class Dummy {
        public Dummy() {
        }
    }

    static class DummyWithoutAnnotation {
        public DummyWithoutAnnotation() {
        }
    }

    @Test
    @DisplayName("Component 어노테이션의 빈을 등록한다.")
    void registerBeans() {
        SingletonBeanContainer container = SingletonBeanContainer.getInstance();
        BeanRegistrar.registerBeans(getClass());
        Object bean = container.getBean(Dummy.class);
        assertThat(bean).isNotNull();
    }

    @Test
    @DisplayName("Component 어노테이션이 없는 클래스는 빈으로 등록하지 않는다.")
    void registerBeansWithoutAnnotation() {
        SingletonBeanContainer container = SingletonBeanContainer.getInstance();
        BeanRegistrar.registerBeans(getClass());
        assertThatThrownBy(() -> container.getBean(DummyWithoutAnnotation.class))
                .isInstanceOf(BeanNotFoundException.class);
    }
}
