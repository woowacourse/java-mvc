package com.interface21.container;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanContainerTest {

    @DisplayName("컨테이너는 객체를 싱글톤으로 관리한다.")
    @Test
    void singleTon() throws Exception {
        BeanContainer beanContainer = BeanContainer.getInstance();
        Object bean1 = beanContainer.getBean(BeanContainerTest.class);
        Object bean2 = beanContainer.getBean(BeanContainerTest.class);

        assertThat(bean1).isEqualTo(bean2);
    }
}
