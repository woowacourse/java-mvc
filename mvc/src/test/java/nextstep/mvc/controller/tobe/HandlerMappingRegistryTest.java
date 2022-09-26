package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import support.CustomReflectionUtils;

class HandlerMappingRegistryTest {

    @DisplayName("HandlerMappingRegistry의 findHandler 메서드는")
    @Nested
    class FindHandler {
        private HandlerMappingRegistry handlerMappingRegistry;

        @BeforeEach
        void setUp() {
            this.handlerMappingRegistry = new HandlerMappingRegistry();
            handlerMappingRegistry.add(new RequestMappingHandlerMapping("samples"));
            handlerMappingRegistry.initialize();
        }

        @DisplayName("매핑 정보에 따른 핸들러를 반환한다 - GET")
        @CsvSource(value = {"/get-test,test-get,GET", "/post-test,test-post,POST"})
        @ParameterizedTest(name = "{0} 요청 - id {1} - HttpMethod {2}")
        void handlerMappingRegistry_should_return_proper_handler_get(
                final String uri, final String value, final String method) throws Exception {
            // given
            final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
            given(httpServletRequest.getRequestURI()).willReturn(uri);
            given(httpServletRequest.getMethod()).willReturn(method);
            given(httpServletRequest.getAttribute("id")).willReturn(value);

            // when
            final HandlerExecution handler = (HandlerExecution) handlerMappingRegistry.findHandler(httpServletRequest);
            final ModelAndView modelAndView = (ModelAndView) handler.handle(httpServletRequest, null);
            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");
            final String id = (String) modelAndView.getModel().get("id");

            // then
            assertAll(
                    () -> assertThat(handler).isInstanceOf(HandlerExecution.class),
                    () -> assertThat(viewName).isEqualTo(""),
                    () -> assertThat(id).isEqualTo(value)
            );
        }

        @DisplayName("매핑 정보를 찾지 못한다면 예외를 던진다")
        @NullAndEmptySource
        @ParameterizedTest(name = "URI : {0}")
        void testMethodNameHere(final String uri) {
            // given
            final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
            given(httpServletRequest.getRequestURI()).willReturn(uri);
            given(httpServletRequest.getMethod()).willReturn("GET");

            // when & then
            assertThatThrownBy(() -> handlerMappingRegistry.findHandler(httpServletRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No Mapped Handler Found");
        }
    }
}
