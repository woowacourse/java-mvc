package web.org.springframework.web.bind;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;
import web.org.springframework.http.CachedBodyInputStream;

import java.io.IOException;

class MappingJackson2HttpMessageConverterTest {

    @Test
    @DisplayName("request 객체로부터 body의 내용을 역직렬화한다.")
    void readRequestBody() throws IOException {
        final var requestBody = "{\n" +
                "    \"account\" : \"dy\",\n" +
                "    \"password\" : \"1\",\n" +
                "    \"email\" : \"dy@gmail.com\"\n" +
                "}";
        final var request = mock(HttpServletRequest.class);

        when(request.getInputStream()).thenReturn(new CachedBodyInputStream(requestBody.getBytes()));
        when(request.getContentLength()).thenReturn(requestBody.getBytes().length);

        assertThat(MappingJackson2HttpMessageConverter.readRequestBody(request, TestUser.class))
                .usingRecursiveComparison()
                .isEqualTo(new TestUser("dy", "1", "dy@gmail.com"));
    }

}
