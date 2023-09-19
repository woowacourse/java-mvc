package com.techcourse.support.web.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterViewController;
import com.techcourse.support.web.resolver.ViewResolvers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingAdapterTest {

    @Test
    void 생성자는_유효한_viewResolver를_전달하면_ManualHandlerMappingAdapter를_초기화한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();

        assertDoesNotThrow(() -> new ManualHandlerMappingAdapter(viewResolvers));
    }

    @Test
    void 생성자는_빈_viewResolver를_전달하면_ManualHandlerMappingAdapter를_초기화한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();

        assertThatThrownBy(() -> new ManualHandlerMappingAdapter(viewResolvers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("viewResolver를 지정해주세요.");
    }

    @Test
    void supports_메서드는_처리할_수_있는_핸들러를_전달하면_true를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();
        final ManualHandlerMappingAdapter adapter = new ManualHandlerMappingAdapter(viewResolvers);

        final boolean actual = adapter.supports(new LoginController());

        assertThat(actual).isTrue();
    }

    @Test
    void supports_메서드는_처리할_수_없는_핸들러를_전달하면_false를_반환한다() {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();
        final ManualHandlerMappingAdapter adapter = new ManualHandlerMappingAdapter(viewResolvers);
        final Object invalidHandler = new TestController();

        final boolean actual = adapter.supports(invalidHandler);

        assertThat(actual).isFalse();
    }

    @Test
    void execute_메서드는_Request_Response_Handler를_전달하면_해당_요청을_처리하고_ModelAndView를_반환한다() throws Exception {
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.initialize();
        final ManualHandlerMappingAdapter adapter = new ManualHandlerMappingAdapter(viewResolvers);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpSession session = mock(HttpSession.class);
        given(session.getAttributeNames()).willReturn(new Vector<String>().elements());
        given(request.getSession()).willReturn(session);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView actual = adapter.execute(request, response, new RegisterViewController());

        assertThat(actual).isNotNull();
    }
}
