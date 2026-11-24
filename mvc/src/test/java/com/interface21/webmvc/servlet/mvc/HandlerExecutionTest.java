package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    private final TestController controller = new TestController();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    private HandlerExecution execution(String methodName, Class<?>... parameterTypes) throws Exception {
        return new HandlerExecution(controller, TestController.class.getMethod(methodName, parameterTypes));
    }

    @Nested
    @DisplayName("컨트롤러 메서드 인자 구성")
    class ArgumentResolution {

        @Test
        @DisplayName("파라미터가 없으면 인자 없이 호출한다")
        void invokeWithoutArguments() throws Exception {
            final var result = execution("noParameters").handle(request, response);

            assertThat(result).isNotNull();
            assertThat(result.getModel()).isEmpty();
        }

        @Test
        @DisplayName("request만 선언하면 request만 전달한다")
        void resolveRequestOnly() throws Exception {
            final var result = execution("requestOnly", HttpServletRequest.class).handle(request, response);

            assertThat(result.getObject("request")).isSameAs(request);
        }

        @Test
        @DisplayName("response만 선언하면 response만 전달한다")
        void resolveResponseOnly() throws Exception {
            final var result = execution("responseOnly", HttpServletResponse.class).handle(request, response);

            assertThat(result.getObject("response")).isSameAs(response);
        }

        @Test
        @DisplayName("request와 response를 선언한 순서대로 전달한다")
        void resolveRequestAndResponse() throws Exception {
            final var result = execution("requestAndResponse", HttpServletRequest.class, HttpServletResponse.class)
                    .handle(request, response);

            assertThat(result.getObject("request")).isSameAs(request);
            assertThat(result.getObject("response")).isSameAs(response);
        }

        @Test
        @DisplayName("response와 request의 순서를 바꿔도 선언한 순서대로 전달한다")
        void resolveResponseAndRequest() throws Exception {
            final var result = execution("reversed", HttpServletResponse.class, HttpServletRequest.class)
                    .handle(request, response);

            assertThat(result.getObject("request")).isSameAs(request);
            assertThat(result.getObject("response")).isSameAs(response);
        }

        @Test
        @DisplayName("지원하지 않는 파라미터 타입은 메서드와 타입을 알리는 예외를 발생시킨다")
        void rejectUnsupportedParameterType() throws Exception {
            final var execution = execution("unsupportedParameter", String.class);

            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("unsupportedParameter")
                    .hasMessageContaining("java.lang.String");
        }
    }

    @Nested
    @DisplayName("컨트롤러 메서드 반환값 처리")
    class ReturnValueHandling {

        @Test
        @DisplayName("String 반환값은 해당 JSP 경로를 렌더링하는 ModelAndView로 변환한다")
        void convertStringToModelAndView() throws Exception {
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

            final var result = execution("stringViewName").handle(request, response);

            assertThat(result.getView()).isInstanceOf(JspView.class);
            result.getView().render(result.getModel(), request, response);
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("ModelAndView 반환값은 동일한 객체를 그대로 반환한다")
        void preserveModelAndView() throws Exception {
            final var expected = controller.modelAndView();

            final var result = execution("modelAndView").handle(request, response);

            assertThat(result).isSameAs(expected);
        }

        @Test
        @DisplayName("지원하지 않는 반환값은 메서드와 타입을 알리는 예외를 발생시킨다")
        void rejectUnsupportedReturnValue() throws Exception {
            final var execution = execution("unsupportedReturnValue");

            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("unsupportedReturnValue")
                    .hasMessageContaining("java.lang.Integer");
        }

        @Test
        @DisplayName("null 반환값은 메서드와 null을 알리는 예외를 발생시킨다")
        void rejectNullReturnValue() throws Exception {
            final var execution = execution("nullReturnValue");

            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nullReturnValue")
                    .hasMessageContaining("null");
        }
    }
}
