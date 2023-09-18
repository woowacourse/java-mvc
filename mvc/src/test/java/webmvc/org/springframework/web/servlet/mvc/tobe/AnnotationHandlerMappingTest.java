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
        @Test
        @DisplayName("적절한 핸들러가 있다면 true를 리턴한다 - get")
        void supports_get() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);

            //when
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            //then
            assertThat(handlerMapping.supports(request)).isTrue();
        }

        @Test
        @DisplayName("적절한 핸들러가 있다면 true를 리턴한다 - post")
        void supports_post() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);

            //when
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");

            //then
            assertThat(handlerMapping.supports(request)).isTrue();
        }

        @Test
        @DisplayName("적절한 핸들러가 없다면 false를 리턴한다")
        void supports_false() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);

            //when
            when(request.getRequestURI()).thenReturn("/nothing");
            when(request.getMethod()).thenReturn("POST");

            //then
            assertThat(handlerMapping.supports(request)).isFalse();
        }
    }
}
