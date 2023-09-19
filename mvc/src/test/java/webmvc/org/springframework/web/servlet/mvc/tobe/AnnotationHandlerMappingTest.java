package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Nested
    class SupportsTest {

        private final HttpServletRequest request = mock(HttpServletRequest.class);

        @Test
        @DisplayName("적절한 핸들러가 있다면 true를 리턴한다 - get")
        void supports_get() {
            //given
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            //when, then
            assertThat(handlerMapping.supports(request)).isTrue();
        }

        @Test
        @DisplayName("적절한 핸들러가 있다면 true를 리턴한다 - post")
        void supports_post() {
            //given
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");

            //when, then
            assertThat(handlerMapping.supports(request)).isTrue();
        }

        @Test
        @DisplayName("적절한 핸들러가 없다면 false를 리턴한다")
        void supports_false() {
            //given
            when(request.getRequestURI()).thenReturn("/nothing");
            when(request.getMethod()).thenReturn("POST");

            //when, then
            assertThat(handlerMapping.supports(request)).isFalse();
        }
    }
}
