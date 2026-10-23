package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappingfixtures.collisions.defaults.DefaultMethodController;
import mappingfixtures.collisions.overloaded.DuplicateController;
import mappingfixtures.collisions.separate.FirstController;
import mappingfixtures.collisions.separate.SecondController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingRegistrationTest {

    @Test
    @DisplayName("다른 컨트롤러의 URL과 HTTP method가 겹치면 양쪽 메서드를 포함한 예외를 전달한다")
    void reject_duplicate_handlers_from_different_controllers() throws Exception {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.collisions.separate");
        final var first = FirstController.class.getDeclaredMethod("handle", HttpServletRequest.class,
                HttpServletResponse.class);
        final var second = SecondController.class.getDeclaredMethod("handle", HttpServletRequest.class,
                HttpServletResponse.class);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(new HandlerKey("/duplicate", RequestMethod.GET).toString())
                .hasMessageContaining(first.toGenericString())
                .hasMessageContaining(second.toGenericString());
    }

    @Test
    @DisplayName("한 컨트롤러의 매핑이 겹치면 파라미터로 구분되는 양쪽 메서드를 포함한 예외를 전달한다")
    void reject_duplicate_handlers_in_one_controller() throws Exception {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.collisions.overloaded");
        final var first = DuplicateController.class.getDeclaredMethod("handle", HttpServletRequest.class,
                HttpServletResponse.class);
        final var second = DuplicateController.class.getDeclaredMethod("handle", HttpServletResponse.class,
                HttpServletRequest.class);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(new HandlerKey("/duplicate", RequestMethod.GET).toString())
                .hasMessageContaining(first.toGenericString())
                .hasMessageContaining(second.toGenericString());
    }

    @Test
    @DisplayName("HTTP method를 생략한 매핑과 GET 매핑이 겹쳐도 초기화에 실패한다")
    void reject_overlap_with_default_http_methods() throws Exception {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.collisions.defaults");
        final var first = DefaultMethodController.class.getDeclaredMethod("handleAll", HttpServletRequest.class,
                HttpServletResponse.class);
        final var second = DefaultMethodController.class.getDeclaredMethod("handleGet", HttpServletRequest.class,
                HttpServletResponse.class);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(new HandlerKey("/duplicate", RequestMethod.GET).toString())
                .hasMessageContaining(first.toGenericString())
                .hasMessageContaining(second.toGenericString());
    }

    @Test
    @DisplayName("같은 URL의 GET과 POST는 공존하고 한 메서드에 반복한 HTTP method도 허용한다")
    void allow_distinct_methods_and_repeated_http_method_values() throws Exception {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.valid");

        mapping.initialize();

        assertHandlerResult(mapping, "GET");
        assertHandlerResult(mapping, "POST");
    }

    @Test
    @DisplayName("초기화를 반복해도 이전 등록을 중복으로 판단하지 않는다")
    void allow_reinitialization() throws Exception {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.valid");
        mapping.initialize();

        mapping.initialize();

        assertHandlerResult(mapping, "GET");
        assertHandlerResult(mapping, "POST");
    }

    @Test
    @DisplayName("중복 매핑으로 초기화가 실패하면 일부 핸들러를 공개하지 않는다")
    void do_not_publish_partial_mappings_on_failure() {
        final var mapping = new AnnotationHandlerMapping("mappingfixtures.collisions.separate");

        assertThatThrownBy(mapping::initialize).isInstanceOf(IllegalStateException.class);

        assertThat(mapping.getHandler(request("/duplicate", "GET"))).isNull();
    }

    private void assertHandlerResult(final AnnotationHandlerMapping mapping, final String method) throws Exception {
        final var request = request("/shared", method);
        final var handler = mapping.getHandler(request);

        final var modelAndView = handler.handle(request, mock(HttpServletResponse.class));

        assertThat(modelAndView.getObject("method")).isEqualTo(method);
    }

    private HttpServletRequest request(final String path, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
