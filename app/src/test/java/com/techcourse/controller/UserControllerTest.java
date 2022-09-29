package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void show() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final TestWriter testWriter = new TestWriter();
        final PrintWriter printWriter = new PrintWriter(testWriter);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter()).thenReturn(printWriter);

        // when
        final ModelAndView modelAndView = userController.show(request, response);
        modelAndView.render(request, response);

        // then
        final User user = (User) modelAndView.getObject("user");
        assertThat(user.getAccount()).isEqualTo("gugu");
        assertThat(testWriter.getWriteBuffer()).isEqualTo("{\"id\":1,\"account\":\"gugu\",\"email\":\"hkkang@woowahan.com\"}");
    }

    private static class TestWriter extends Writer {

        private char[] writeBuffer = new char[0];

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
            final StringBuilder stringBuilder = new StringBuilder(String.valueOf(writeBuffer));
            stringBuilder.append(cbuf, off, len);
            writeBuffer = stringBuilder.toString()
                    .toCharArray();
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        public String getWriteBuffer() {
            return String.valueOf(writeBuffer);
        }
    }
}
