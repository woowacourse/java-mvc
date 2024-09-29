package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

public class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter;

    static Stream<Arguments> testDataForTestSupports() {
        return Stream.of(
                Arguments.of(mock(Controller.class), false),
                Arguments.of(mock(HandlerExecution.class), true)
        );
    }

    @BeforeEach
    void setup() {
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("핸들러가 HandlerExecution 인스턴스인지 확인한다.")
    @MethodSource("testDataForTestSupports")
    @ParameterizedTest
    void testSupports(final Object handler, final boolean expected) {
        // given & when
        final boolean actual = annotationHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("핸들러를 실행할 수 있다.")
    @Test()
    void testHandle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerExecution = mock(HandlerExecution.class);
        when(handlerExecution.handle(request, response)).thenReturn(new ModelAndView(new JspView("sample")));

        // when
        annotationHandlerAdapter.handle(request, response, handlerExecution);

        // then
        verify(handlerExecution).handle(request, response);
    }
}
