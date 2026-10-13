package com.techcourse;


import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void 기본_설정으로_서블릿을_초기화한다() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        assertThatCode(dispatcherServlet::init).doesNotThrowAnyException();
    }
}
