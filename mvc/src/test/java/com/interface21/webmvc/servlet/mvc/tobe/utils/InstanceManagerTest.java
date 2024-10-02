package com.interface21.webmvc.servlet.mvc.tobe.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.InstanceManagerTestController;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InstanceManagerTest {

    @Test
    @DisplayName("클래스에 맞는 생성자로 객체를 생성한다.")
    void scanControllers() {
        //given
        String[] basePackages = new String[]{"samples"};

        //when, then
        assertDoesNotThrow(() -> new InstanceManager(basePackages));
    }

    @Test
    @DisplayName("Controller 어노테이션이 붙어있는 모든 클래스를 등록한다.")
    void getControllers() {
        //given
        String[] basePackages = new String[]{"samples"};
        InstanceManager instanceManager = new InstanceManager(basePackages);

        //when
        Map<Class<?>, Object> controllers = instanceManager.getControllers();

        //then
        assertAll(
                () -> assertThat(controllers).hasSize(2),
                () -> assertThat(controllers).containsKey(TestController.class),
                () -> assertThat(controllers).containsKey(InstanceManagerTestController.class));
    }
}
