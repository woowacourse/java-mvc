package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    public void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
    }

    @DisplayName("URI에 해당하는 컨트롤러 기반 핸들러어댑터를 반환한다.")
    @Test
    void getHandlerAdapter() throws NoSuchMethodException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getRequestURI()).thenReturn("/login");

        HandlerExecution handlerExecution = new HandlerExecution(this.getClass(), this.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class));
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(handlerAdapter).isInstanceOf(ManualHandlerAdapter.class);
    }
}
