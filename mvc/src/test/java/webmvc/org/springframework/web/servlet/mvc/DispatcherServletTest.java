package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;


    class TestHandlerMapping implements HandlerMapping {
        @Override
        public HandlerExecution getHandler(HttpServletRequest request) {
            return null;
        }
    }

    @Test
    void 알맞은_핸들러매핑을_가져올_수_있다() {
        //given

        //when

        //then
    }
}
