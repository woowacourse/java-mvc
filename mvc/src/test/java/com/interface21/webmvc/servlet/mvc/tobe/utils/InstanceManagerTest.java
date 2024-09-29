package com.interface21.webmvc.servlet.mvc.tobe.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstanceManagerTest {

    static class ErrorController {
    }

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

    @Test
    @DisplayName("이미 한 번 요청한 객체를 재요청할 시 같은 인스턴스를 반환한다.")
    void get_sameObject() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //given
        Class<?> clazz = TestController.class;
        InstanceManager instanceManager = InstanceManager.getInstance();

        //when
        Object result1 = instanceManager.get(clazz);
        Object result2 = instanceManager.get(clazz);

        //then
        assertThat(result1).isSameAs(result2);
    }

    @Test
    @DisplayName("존재하지 않는 컨트롤러 인스턴스를 요구할 경우 에러를 반환한다.")
    void get_fail() {
        //given
        Class<?> clazz = ErrorController.class;
        InstanceManager instanceManager = InstanceManager.getInstance();

        //when, then
        assertThatThrownBy(() -> instanceManager.get(clazz))
                .isInstanceOf(NoSuchMethodException.class);
    }
}
