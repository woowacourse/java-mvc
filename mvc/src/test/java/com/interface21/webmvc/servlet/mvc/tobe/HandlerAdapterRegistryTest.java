package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    private final HandlerAdapter testHandlerAdapter = new HandlerAdapter() {
        @Override
        public boolean support(Object handler) {
            return true;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            ModelAndView modelAndView = mock(ModelAndView.class);
            when(modelAndView.getObject("test")).thenReturn("test");
            return modelAndView;
        }
    };

    @Test
    @DisplayName("어댑터 반환 성공")
    void getHandler() throws Exception {
        // given
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(testHandlerAdapter);

        // when
        HandlerAdapter handlerAdapter = registry.getHandlerAdapter(testHandlerAdapter);

        //then
        assertThat(
                handlerAdapter.handle(null, null, null)
                        .getObject("test"))
                .isEqualTo("test");
    }

    @Test
    @DisplayName("어댑터 반환 실패 : 존재하지 않을 경우")
    void getHandler_notExistException() {
        // given
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();

        // when & then
        assertThatThrownBy(() -> registry.getHandlerAdapter(testHandlerAdapter))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
