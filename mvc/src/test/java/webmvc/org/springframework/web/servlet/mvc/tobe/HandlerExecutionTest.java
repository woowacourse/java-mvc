package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestUser;
import web.org.springframework.http.CachedBodyInputStream;

import java.io.IOException;
import java.lang.reflect.Method;

class HandlerExecutionTest {

    private static final String requestBody = "{\n" +
            "    \"account\" : \"dy\",\n" +
            "    \"password\" : \"1\",\n" +
            "    \"email\" : \"dy@gmail.com\"\n" +
            "}";
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private HttpServletRequest request;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getContentType()).thenReturn("application/json");
        when(request.getContentLength()).thenReturn(requestBody.getBytes().length);
        when(request.getInputStream()).thenReturn(new CachedBodyInputStream(requestBody.getBytes()));
    }

    @Test
    @DisplayName("메서드 파라미터에 정의된 타입으로 Json 형식의 RequestBody 데이터를 역직렬화한다.")
    void deserializeJsonToRequestBody() throws Exception {
        final var method = mock(Method.class);
        final var controller = new TestController();
        when(method.getParameterTypes()).thenReturn(new Class[]{HttpServletRequest.class, HttpServletResponse.class, TestUser.class});
        final var handlerExecution = new HandlerExecution(controller, method);

        handlerExecution.handle(request, response);

        verify(method).invoke(controller, request, response, new TestUser("dy", "1", "dy@gmail.com"));
    }

}
