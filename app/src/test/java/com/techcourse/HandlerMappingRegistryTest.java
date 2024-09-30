package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
    }

    @DisplayName("AnnotationHandlerMapping을 찾을 수 있다.")
    @Test
    void getHandler_Annotation() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn("/test-get")
                .when(request).getRequestURI();
        doReturn("GET")
                .when(request).getMethod();

        assertThat(handlerMappingRegistry.getHandler(request))
                .containsInstanceOf(HandlerExecution.class);
    }

    @DisplayName("ManualHandlerMapping 매핑되지 않은 URI로 요청하면 empty 반환")
    @Test
    void getHandler_Manual_Empty() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn("/not-exist")
                .when(request).getRequestURI();
        doReturn("GET")
                .when(request).getMethod();

        assertThat(handlerMappingRegistry.getHandler(request))
                .isEmpty();
    }

    @DisplayName("AnnotationHandlerMapping 매핑되지 않은 URI로 요청하면 empty 반환")
    @Test
    void getHandler_Annotatioan() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn("/test-post")
                .when(request).getRequestURI();
        doReturn("GET")
                .when(request).getMethod();

        assertThat(handlerMappingRegistry.getHandler(request))
                .isEmpty();
    }

    @SuppressWarnings("unused") // 리플렉션을 통해 사용되고 있는 코드
    @Controller
    static public class AnnotatedController {

        public AnnotatedController() {
        }

        @RequestMapping(value = "/test-get", method = RequestMethod.GET)
        public ModelAndView get() {
            return null;
        }
    }
}
