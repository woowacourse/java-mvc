package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();
    }

    @DisplayName("디스패처 서블릿은 처리 가능한 어댑터를 찾지 못할 경우 예외를 반환한다.")
    @Test
    void findNotMatchHandlerAdapter() throws NoSuchMethodException {
        // given
        Object invalidObject = new Object();

        // when
        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandlerAdapter", Object.class);
        method.setAccessible(true);

        // then
        Assertions.assertThatThrownBy(() -> method.invoke(dispatcherServlet, invalidObject))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("처리할 수 없는 요청입니다.");
    }
}
