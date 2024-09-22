package com.interface21.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import samples.TestController;

class BeanFactoryTest {

    @Test
    void bean을_반환한다() {
        // given
        BeanFactory beanFactory = BeanFactory.getInstance();

        // when
        Object bean = beanFactory.getBean(TestController.class);

        // then
        assertThat(bean).isExactlyInstanceOf(TestController.class);
    }

    @Test
    void 동일한_bean을_반환한다() {
        // given
        BeanFactory beanFactory = BeanFactory.getInstance();

        // when
        Object bean1 = beanFactory.getBean(TestController.class);
        Object bean2 = beanFactory.getBean(TestController.class);

        // then
        assertThat(bean1).isSameAs(bean2);
    }

    @Test
    void bean을_생성할_수_없는_경우_BeanCreationException이_발생한다() {
        // given
        BeanFactory beanFactory = BeanFactory.getInstance();
        Class<?> invalidBeanClass = InvalidBean.class;

        // when & then
        assertThatThrownBy(() -> beanFactory.getBean(invalidBeanClass))
                .isExactlyInstanceOf(BeanCreationException.class)
                .hasCauseExactlyInstanceOf(InstantiationException.class)
                .hasMessage("Error creating bean with name '%s'".formatted(invalidBeanClass.getName()));
    }

    abstract static class InvalidBean {
    }
}
