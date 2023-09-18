package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private static ManualHandlerMapping handlerMapping = new ManualHandlerMapping();

    @BeforeAll
    static void setUp() {
        handlerMapping.initialize();
    }

    @Nested
    class SupportsTest {
        @Test
        @DisplayName("적절한 핸들러가 있다면 true를 리턴한다 - post")
        void supports() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);

            //when
            when(request.getRequestURI()).thenReturn("/");

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

            //then
            assertThat(handlerMapping.supports(request)).isFalse();
        }
    }
}
