package nextstep.mvc.controller.handlermapping;

import fixture.RequestFixture;
import fixture.ResponseFixture;
import nextstep.mvc.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.handlermapping.HandlerExecution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        final var request = RequestFixture.getRequest();
        final var response = ResponseFixture.response();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("POST 요청에 대한 handler 조회")
    @Test
    void post() throws Exception {
        final var request = RequestFixture.postRequest();
        final var response = ResponseFixture.response();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("추가하지 않은 handler 조회 시 예외 발생")
    @Test
    void getHandlerNotExist() {
        final var request = RequestFixture.getRequest("/not-exist-path");

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("요청한 핸들러가 존재하지 않습니다.");
    }
}
