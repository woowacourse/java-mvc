package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    @Test
    void 매개변수_개수가_다르면_예외가_발생한다() throws Exception {
        // given
        Method handlerMethod = ValidationController.class.getMethod(
                "missingResponse", HttpServletRequest.class);

        // when
        Throwable thrown = catchThrowable(
                () -> new HandlerExecution(new ValidationController(), handlerMethod));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ValidationController.class.getName())
                .hasMessageContaining("missingResponse")
                .hasMessageContaining("매개변수는 (HttpServletRequest, HttpServletResponse)");
    }

    @Test
    void 매개변수_타입이_다르면_예외가_발생한다() throws Exception {
        // given
        Method handlerMethod = ValidationController.class.getMethod(
                "wrongParameterType", String.class, HttpServletResponse.class);

        // when
        Throwable thrown = catchThrowable(
                () -> new HandlerExecution(new ValidationController(), handlerMethod));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ValidationController.class.getName())
                .hasMessageContaining("wrongParameterType")
                .hasMessageContaining("매개변수는 (HttpServletRequest, HttpServletResponse)");
    }

    @Test
    void 매개변수_순서가_다르면_예외가_발생한다() throws Exception {
        // given
        Method handlerMethod = ValidationController.class.getMethod(
                "reversedParameters", HttpServletResponse.class, HttpServletRequest.class);

        // when
        Throwable thrown = catchThrowable(
                () -> new HandlerExecution(new ValidationController(), handlerMethod));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ValidationController.class.getName())
                .hasMessageContaining("reversedParameters")
                .hasMessageContaining("매개변수는 (HttpServletRequest, HttpServletResponse)");
    }

    @Test
    void 반환_타입이_ModelAndView가_아니면_예외가_발생한다() throws Exception {
        // given
        Method handlerMethod = ValidationController.class.getMethod(
                "wrongReturnType", HttpServletRequest.class, HttpServletResponse.class);

        // when
        Throwable thrown = catchThrowable(
                () -> new HandlerExecution(new ValidationController(), handlerMethod));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ValidationController.class.getName())
                .hasMessageContaining("wrongReturnType")
                .hasMessageContaining("반환 타입은 ModelAndView");
    }

    @Test
    void ModelAndView_하위_타입을_반환하는_메서드를_실행한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Method handlerMethod = ValidationController.class.getMethod(
                "subtypeReturn", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution execution = new HandlerExecution(new ValidationController(), handlerMethod);

        // when
        ModelAndView modelAndView = execution.handle(request, response);

        // then
        assertThat(modelAndView).isInstanceOf(CustomModelAndView.class);
        assertThat(modelAndView.getObject("id")).isEqualTo("juni");
    }

    @Test
    void 등록된_컨트롤러_메서드를_실행한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("juni");
        Method handlerMethod = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution execution = new HandlerExecution(new TestController(), handlerMethod);

        // when
        ModelAndView modelAndView = execution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("juni");
    }
    public static class ValidationController {

        public ModelAndView missingResponse(HttpServletRequest request) {
            return new ModelAndView(mock(View.class));
        }

        public ModelAndView wrongParameterType(String request, HttpServletResponse response) {
            return new ModelAndView(mock(View.class));
        }

        public ModelAndView reversedParameters(HttpServletResponse response, HttpServletRequest request) {
            return new ModelAndView(mock(View.class));
        }

        public String wrongReturnType(HttpServletRequest request, HttpServletResponse response) {
            return "users.jsp";
        }

        public CustomModelAndView subtypeReturn(HttpServletRequest request, HttpServletResponse response) {
            CustomModelAndView modelAndView = new CustomModelAndView(mock(View.class));
            modelAndView.addObject("id", "juni");
            return modelAndView;
        }
    }

    public static class CustomModelAndView extends ModelAndView {

        public CustomModelAndView(View view) {
            super(view);
        }
    }
}
