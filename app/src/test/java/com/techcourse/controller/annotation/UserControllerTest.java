package com.techcourse.controller.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    @DisplayName("유저가 존재하면 유저 account정보를 json형태로 출력한다.")
    void show() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        PrintWriter printWriter = new PrintWriter(testWriter);
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(printWriter);
        UserController controller = new UserController();

        ModelAndView modelAndView = controller.show(request, response);
        modelAndView.render(request, response);

        assertThat(testWriter.getBuffer()).isEqualTo("{\"account\":\"gugu\"}");
    }

    static class TestWriter extends Writer {

        private String buffer;

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = off; i < len; ++i) {
                stringBuilder.append(cbuf[i]);
            }
            buffer = stringBuilder.toString();
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        public String getBuffer() {
            return buffer;
        }
    }
}
