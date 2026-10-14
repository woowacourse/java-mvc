package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("요청 URI로 수동 등록한 컨트롤러를 찾는다")
    void find_registered_controller() {
        assertThat(handlerMapping.getHandler(request("/login/view"))).isInstanceOf(LoginViewController.class);
        assertThat(handlerMapping.getHandler(request("/register/view"))).isInstanceOf(RegisterViewController.class);
    }

    @Test
    @DisplayName("미등록 경로는 찾지 못한다")
    void return_null_for_unregistered_path() {
        assertThat(handlerMapping.getHandler(request("/missing"))).isNull();
    }

    @Test
    @DisplayName("수동 매핑 인스턴스마다 등록 상태를 독립적으로 관리한다")
    void manage_registration_per_instance() {
        final var uninitializedMapping = new ManualHandlerMapping();

        assertThat(uninitializedMapping.getHandler(request("/login/view"))).isNull();
        assertThat(handlerMapping.getHandler(request("/login/view"))).isNotNull();
    }

    private HttpServletRequest request(final String uri) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
