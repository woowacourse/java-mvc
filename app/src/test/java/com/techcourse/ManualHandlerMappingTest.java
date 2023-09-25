package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.handlerMapping.ManualHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.ForwardController;

class ManualHandlerMappingTest {

    private final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

    @Test
    void 초기화시에_생성되는_객체들은_모두_Controller_인터페이스를_구현한다() throws NoSuchFieldException, IllegalAccessException {
        // given
        Class<? extends ManualHandlerMapping> clazz = manualHandlerMapping.getClass();
        Field field = clazz.getDeclaredField("controllers");
        field.setAccessible(true);

        // when
        manualHandlerMapping.initialize();

        // then
        Map<String, Controller> controllers = (Map<String, Controller>) field.get(manualHandlerMapping);
        for (Controller controller : controllers.values()) {
            assertThat(controller).isInstanceOf(Controller.class);
        }
    }

    @Test
    void Request객체로_적절한_핸들러를_반환한다() {
        // given
        manualHandlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        // when
        Optional<Object> result = manualHandlerMapping.getHandler(request);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isInstanceOf(ForwardController.class);
    }
}
