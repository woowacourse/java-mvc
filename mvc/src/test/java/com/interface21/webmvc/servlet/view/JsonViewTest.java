package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    class TestUser {
        private String name;

        public TestUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void model_데이터가_1개라면_데이터를_구분하지_않는다() throws Exception {
        // Given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // 실제 바이트 출력을 캡처할 OutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public boolean isReady() { return true; }

            @Override
            public void setWriteListener(WriteListener writeListener) {}
        };

        when(request.getAttributeNames()).thenReturn(Collections.enumeration(List.of("user")));
        when(request.getAttribute("user")).thenReturn(new TestUser("test"));
        when(response.getOutputStream()).thenReturn(sos);

        JsonView view = new JsonView();

        // When
        view.render(request, response);

        // Then
        String jsonOutput = baos.toString("UTF-8");
        assertThat(jsonOutput).isEqualTo("{\"name\":\"test\"}"); // 단일 값이므로 직접 JSON 문자열

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void model_데이터가_1개이상이면_데이터를_구분한다() throws Exception {
        // Given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // 실제 바이트 출력을 캡처할 OutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public boolean isReady() { return true; }

            @Override
            public void setWriteListener(WriteListener writeListener) {}
        };

        when(request.getAttributeNames()).thenReturn(Collections.enumeration(List.of("user1", "user2")));
        when(request.getAttribute("user1")).thenReturn(new TestUser("test1"));
        when(request.getAttribute("user2")).thenReturn(new TestUser("test2"));
        when(response.getOutputStream()).thenReturn(sos);

        JsonView view = new JsonView();

        // When
        view.render(request, response);

        // Then
        String jsonOutput = baos.toString("UTF-8");
        String ans = "{\"user1\":{\"name\":\"test1\"},\"user2\":{\"name\":\"test2\"}}";
        assertThat(jsonOutput).isEqualTo(ans); // 단일 값이므로 직접 JSON 문자열

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

}
