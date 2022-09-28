package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.adapter.RequestMappingHandlerAdapter;
import nextstep.mvc.mapping.RequestMappingHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import support.DummyPrintWriter;

class DispatcherServletTest {

    @DisplayName("DispatcherServlet을")
    @Nested
    class CallServiceBeforeInit {

        @DisplayName("초기화 하지 않고 service 메서드를 호출하면 예외가 발생한다")
        @Test
        void throws_exception_when_service_is_called_before_init() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            given(request.getMethod()).willReturn("GET");
            given(request.getRequestURI()).willReturn("/get-json-test");

            // when
            final DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.addHandlerMapping(new RequestMappingHandlerMapping("samples"));
            dispatcherServlet.addHandlerAdapter(new RequestMappingHandlerAdapter());

            // then
            assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                    .isInstanceOf(ServletException.class)
                    .hasMessageContaining("No Mapped Handler Found");
        }

        @DisplayName("초기화 후 service 메서드를 호출하면 정상 처리된다")
        @Test
        void process_properly_when_service_is_called_after_init() throws ServletException, IOException {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getMethod()).willReturn("GET");
            given(request.getRequestURI()).willReturn("/get-json-test");

            final HttpServletResponse response = mock(HttpServletResponse.class);
            final DummyPrintWriter writer = new DummyPrintWriter();
            given(response.getWriter()).willReturn(writer);

            // when
            final DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.addHandlerMapping(new RequestMappingHandlerMapping("samples"));
            dispatcherServlet.addHandlerAdapter(new RequestMappingHandlerAdapter());
            dispatcherServlet.init();
            dispatcherServlet.service(request, response);

            // then
            assertThat(writer.written).isEqualTo("{\"id\":1,\"account\":\"gugu\",\"email\":\"hkkang@woowahan.com\"}");
        }
    }
}
