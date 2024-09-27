package com.interface21.webmvc.servlet.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;
import samples.TestController;

@DisplayName("어노테이션 어댑터")
class RequestMappingHandlerAdapterTest {

    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @BeforeEach
    void setUp() {
        this.requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
    }

    @DisplayName("RequestMapping 어노테이션이 달린 HandlerExecution 의 지원이 가능하다.")
    @Test
    void supportsValidController() {
        // given
        TestAnnotationController controller = new TestAnnotationController();
        HandlerExecution handler = new HandlerExecution(
                controller,
                controller.getClass().getDeclaredMethods()[0]
        );

        // when
        boolean actual = requestMappingHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("RequestMapping 어노테이션이 달리지 않은 핸들러의 지원이 불가능하다.")
    @Test
    void supportsInvalidController() {
        // given
        Controller controller = new TestController();

        // when
        boolean actual = requestMappingHandlerAdapter.supports(controller);

        // then
        assertThat(actual).isFalse();
    }
}
