package com.interface21.webmvc.servlet.mvc.tobe;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class InstancePoolTest {

    private final InstancePool instancePool = InstancePool.getSingleton();

    @DisplayName("한번 생성한 인스턴스는 동일한 인스턴스를 반환한다.")
    @Test
    void getInstance() {
        Class<?> clazz = TestController.class;

        Object instance1 = instancePool.getInstance(clazz);
        Object instance2 = instancePool.getInstance(clazz);

        assertThat(instance1).isEqualTo(instance2);
    }
}
