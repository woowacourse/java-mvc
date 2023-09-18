package com.techcourse.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerHandlerMappingAdapterTest {

    @Test
    void supports_메서드는_컨트롤러_타입인_경우_true_반환한다() {
        ControllerHandlerMappingAdapter controllerHandlerMappingAdapter = new ControllerHandlerMappingAdapter();

        boolean supports = controllerHandlerMappingAdapter.supports(new LoginController());

        assertThat(supports).isTrue();
    }

    @Test
    void supports_메서드는_컨트롤러_타입이_아닌_경우_false_반환한다() {
        ControllerHandlerMappingAdapter controllerHandlerMappingAdapter = new ControllerHandlerMappingAdapter();

        boolean supports = controllerHandlerMappingAdapter.supports(new Object());

        assertThat(supports).isFalse();
    }

    @Test
    void 컨트롤러의_execute_메서드를_실행하고_ModelAndView_반환한다() throws Exception {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        RegisterViewController registerViewController = new RegisterViewController();
        ControllerHandlerMappingAdapter controllerHandlerMappingAdapter = new ControllerHandlerMappingAdapter();

        ModelAndView modelAndView = controllerHandlerMappingAdapter.handle(httpServletRequest, httpServletResponse,
                registerViewController);

        assertThat(modelAndView).isNotNull();
    }
}
