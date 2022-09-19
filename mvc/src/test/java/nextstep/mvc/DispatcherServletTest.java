package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.ManualHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

@DisplayName("DispatcherServlet의")
class DispatcherServletTest {

    @Nested
    @DisplayName("service 메서드는")
    class Service {

        @Test
        @DisplayName("GET /get-manual 요청을 레거시 컨트롤러로 처리한다.")
        void service_legacy_success() {
            // given
            final DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.addHandlerMapping(new TestManualHandlerMapping());
            dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());
            dispatcherServlet.init();

            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            willReturn("/get-manual")
                    .given(request)
                    .getRequestURI();

            // when & then
            assertThatCode(() -> dispatcherServlet.service(request, response))
                    .doesNotThrowAnyException();
        }
    }
}
