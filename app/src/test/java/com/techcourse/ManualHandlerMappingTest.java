package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ManualHandlerMappingTest {

    ManualHandlerMapping manualHandlerMapping;

    @Mock
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
        manualHandlerMapping = new ManualHandlerMapping();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/login", "/login/view", "/logout"})
    void findEnrolledControllers(String uri) {
        // given
        manualHandlerMapping.initialize();
        given(httpServletRequest.getRequestURI()).willReturn(uri);

        // when
        Optional<Object> handler = manualHandlerMapping.getHandler(httpServletRequest);

        // then
        assertThat(handler).isPresent();
    }
}
