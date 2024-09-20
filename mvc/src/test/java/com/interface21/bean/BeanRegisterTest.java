package com.interface21.bean;

import com.interface21.bean.container.BeanContainer;
import com.interface21.bean.scanner.BeanScannerTest;
import com.interface21.context.stereotype.Component;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanRegisterTest {

    @BeforeEach
    void setUp() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.clear();
    }

    @DisplayName("특정 클래스 기준으로 @Component가 붙은 class를 빈 컨테이너에 등록한다.")
    @Test
    void run() {
        BeanRegister.run(BeanScannerTest.class);

        BeanContainer beanContainer = BeanContainer.getInstance();
        List<Object> annotatedBeans = beanContainer.getAnnotatedBeans(Component.class);

        Assertions.assertThat(annotatedBeans).hasSize(2)
                .extracting(bean -> bean.getClass().getSimpleName())
                .containsExactlyInAnyOrder("TestController1", "TestController2");
    }
}
