package com.interface21.webmvc.servlet.mvc.tobe.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceManagerTest {

    @Test
    @DisplayName("클래스에 맞는 생성자로 객체를 생성해 반환한다.")
    void get() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //given
        Class<?> clazz = TestController.class;
        InstanceManager instanceManager = InstanceManager.getInstance();

        //when
        Object result = instanceManager.get(clazz);

        //then
        assertThat(result).isExactlyInstanceOf(clazz);
    }
}
