package com.techcourse.controller;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    @Test
    void userLookupRendersJsonThroughAnnotationMapping() throws Exception {
        final var mapping = new AnnotationHandlerMapping("com.techcourse.controller");
        mapping.initialize();
        final var request = new MockHttpServletRequest("GET", "/api/user");
        request.setParameter("account", "gugu");
        final var response = new MockHttpServletResponse();

        final var handler = (HandlerExecution) mapping.getHandler(request);
        final var modelAndView = handler.handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString())
                .contains("\"id\":1", "\"account\":\"gugu\"", "\"email\":\"hkkang@woowahan.com\"")
                .doesNotContain("\"user\":", "password");
    }
}
