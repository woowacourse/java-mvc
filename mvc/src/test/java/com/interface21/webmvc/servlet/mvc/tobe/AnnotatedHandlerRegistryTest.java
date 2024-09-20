package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

import samples.TestController;

class AnnotatedHandlerRegistryTest {

    @Test
    @DisplayName("베이스 타입 애노테이션과 메서드 애노테이션을 기반으로 관련 레지스트리를 생성한다.")
    void set_registry_via_specific_type_annotation_with_method_annotation() {
        // given
        final AnnotatedHandlerRegistry registry = new AnnotatedHandlerRegistry("samples");
        registry.initialize(Controller.class, RequestMapping.class);

        // when
        final Set<Method> methods = registry.getMethods();

        // then
        assertThat(methods).hasSize(3);
    }

    @Test
    @DisplayName("메스드로 해당 인스턴스를 불러온다.")
    void get_instance_via_method() {
        // given
        final AnnotatedHandlerRegistry registry = new AnnotatedHandlerRegistry("samples");
        registry.initialize(Controller.class, RequestMapping.class);
        final Class<TestController> testControllerClass = TestController.class;
        final Method method = Stream.of(testControllerClass.getMethods())
                .findAny()
                .orElseThrow();

        // when
        final Object instance = registry.getInstance(method);

        // then
        assertThat(instance).isInstanceOf(TestController.class);
    }

    @Test
    @DisplayName("존재하지 않은 메소드로 해당 인스턴스 요청시 예외를 발생한다.")
    void throw_exception_when_search_request_with_does_not_exist_method() {
        // given
        final AnnotatedHandlerRegistry registry = new AnnotatedHandlerRegistry("samples");
        final Class<TestController> testControllerClass = TestController.class;
        final Method method = Stream.of(testControllerClass.getMethods())
                .findAny()
                .orElseThrow();

        // when & then
        assertThatThrownBy(() -> registry.getInstance(method))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
