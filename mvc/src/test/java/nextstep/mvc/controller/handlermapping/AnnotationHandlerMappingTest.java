package nextstep.mvc.controller.handlermapping;

import fixture.RequestFixture;
import fixture.ResponseFixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("GET 요청에 대한 handler 조회")
    @Test
    void get() throws Exception {
        final HttpServletRequest request = RequestFixture.getRequest();
        final HttpServletResponse response = ResponseFixture.response();

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("POST 요청에 대한 handler 조회")
    @Test
    void post() throws Exception {
        final HttpServletRequest request = RequestFixture.postRequest();
        final HttpServletResponse response = ResponseFixture.response();

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
