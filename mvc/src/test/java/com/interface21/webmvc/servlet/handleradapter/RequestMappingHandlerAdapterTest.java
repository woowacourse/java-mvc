package com.interface21.webmvc.servlet.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

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
}
