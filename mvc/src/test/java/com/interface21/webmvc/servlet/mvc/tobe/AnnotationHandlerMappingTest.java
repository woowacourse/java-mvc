package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AnnotationHandlerMappingTest {

    @Nested
    class handler_등록_성공 {

        private HandlerMapping handlerMapping;

        @BeforeEach
        void setUp() {
            handlerMapping = new AnnotationHandlerMapping("samples");
            handlerMapping.initialize();
        }

        @Test
        void get() throws Exception {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            final var modelAndView = handlerExecution.handle(request, response);

            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }

        @Test
        void post() throws Exception {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");

            final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            final var modelAndView = handlerExecution.handle(request, response);

            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }

        @Test
        void 경로가_없으면_루트_경로로_handler를_등록한다() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request, response);

            // then
            assertThat(modelAndView.getView()).extracting("viewName").isEqualTo("/");
        }

        @ParameterizedTest
        @EnumSource(RequestMethod.class)
        void method가_없으면_모든_HTTP_method로_handler를_등록한다(RequestMethod requestMethod) throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/all-method");
            when(request.getMethod()).thenReturn(requestMethod.name());

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request, response);

            // then
            assertThat(modelAndView.getView()).extracting("viewName").isEqualTo("/all-method");
        }

        @Test
        void 클래스에만_경로가_존재하면_해당_경로로_handler를_등록한다() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/hello");
            when(request.getMethod()).thenReturn("GET");

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request, response);

            // then
            assertThat(modelAndView.getView()).extracting("viewName").isEqualTo("/hello");
        }

        @Test
        void 클래스와_메소드에_경로가_존재하면_두_경로를_결합해서_handler를_등록한다() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/hello/world");
            when(request.getMethod()).thenReturn("GET");

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request, response);

            // then
            assertThat(modelAndView.getView()).extracting("viewName").isEqualTo("/hello/world");
        }
    }

    @Nested
    class 핸들러_등록_실패 {

        @Test
        void handler가_추상클래스일_경우_HandlerCreationException이_발생한다() {
            // given
            HandlerMapping handlerMapping = new AnnotationHandlerMapping("exception.instantiation");

            // when & then
            assertThatThrownBy(handlerMapping::initialize)
                    .isExactlyInstanceOf(HandlerCreationException.class)
                    .hasCauseExactlyInstanceOf(InstantiationException.class)
                    .hasMessage("Error creating handler with name 'exception.instantiation.InstantiationExceptionController'");
        }

        @Test
        void handler에_기본_생성자가_없을_경우_HandlerCreationException이_발생한다() {
            // given
            HandlerMapping handlerMapping = new AnnotationHandlerMapping("exception.nosuchmethod");

            // when & then
            assertThatThrownBy(handlerMapping::initialize)
                    .isExactlyInstanceOf(HandlerCreationException.class)
                    .hasCauseExactlyInstanceOf(NoSuchMethodException.class)
                    .hasMessage("Error creating handler with name 'exception.nosuchmethod.NoSuchMethodExceptionController'");
        }

        @Test
        void handler_생성자의_접근제어자가_public이_아닐_경우_HandlerCreationException이_발생한다() {
            // given
            HandlerMapping handlerMapping = new AnnotationHandlerMapping("exception.illegalaccess");

            // when & then
            assertThatThrownBy(handlerMapping::initialize)
                    .isExactlyInstanceOf(HandlerCreationException.class)
                    .hasCauseExactlyInstanceOf(IllegalAccessException.class)
                    .hasMessage("Error creating handler with name 'exception.illegalaccess.IllegalAccessExceptionController'");
        }

        @Test
        void handler_생성자에서_예외가_발생할_경우_HandlerCreationException이_발생한다() {
            // given
            HandlerMapping handlerMapping = new AnnotationHandlerMapping("exception.invocationtarget");

            // when & then
            assertThatThrownBy(handlerMapping::initialize)
                    .isExactlyInstanceOf(HandlerCreationException.class)
                    .hasCauseExactlyInstanceOf(InvocationTargetException.class)
                    .hasMessage("Error creating handler with name 'exception.invocationtarget.InvocationTargetExceptionController'");
        }
    }
}
