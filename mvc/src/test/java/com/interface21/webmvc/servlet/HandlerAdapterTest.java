package com.interface21.webmvc.servlet;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new HandlerAdapter();
    }

    @DisplayName("컨트롤러의 실행결과가 문자면 viwName인 ModelAndView를 반환한다.")
    @Test
    void givenString_thenReturnModelAndView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("viewName");

        ModelAndView modelAndView = handlerAdapter.handle(controller, request, response);

        assertThat(modelAndView.getViewName()).isEqualTo("viewName");
    }

    @DisplayName("attribute값을 ModelAndView 객체에 넣어준다.")
    @Test
    void addAttributesToModelAndView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        Vector<String> attributes = getRequestAttributes();
        when(request.getAttributeNames()).thenReturn(attributes.elements());
        for (int i = 1; i <= 3; i++) {
            when(request.getAttribute("attribute" + i)).thenReturn(i);
        }
        when(controller.execute(request, response)).thenReturn("viewName");

        ModelAndView modelAndView = handlerAdapter.handle(controller, request, response);

        assertThat(modelAndView.getModel()).isEqualTo(
                Map.of("attribute1", 1, "attribute2", 2, "attribute3", 3)
        );
    }

    private Vector<String> getRequestAttributes() {
        Vector<String> vector = new Vector<>();
        vector.add("attribute1");
        vector.add("attribute2");
        vector.add("attribute3");
        return vector;
    }

    @DisplayName("attribute가 없으면 빈 모델을 반환한다.")
    @Test
    void givenEmptyAttribute_thenEmptyModel() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("viewName");

        ModelAndView modelAndView = handlerAdapter.handle(controller, request, response);

        assertThat(modelAndView.getModel()).isEqualTo(EMPTY_MAP);
    }

}
