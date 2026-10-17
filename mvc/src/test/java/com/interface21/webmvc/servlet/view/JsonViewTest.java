package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void ContentType을_JSON으로_설정한_뒤에_본문을_쓴다() throws Exception {
        final var view = new JsonView();

        view.render(Map.of("name", "어셔"), request, response);

        final InOrder inOrder = inOrder(response);
        inOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        inOrder.verify(response).getWriter();
    }

    @Test
    void 모델에_데이터가_1개면_값을_그대로_JSON으로_변환한다() throws Exception {
        final var view = new JsonView();

        view.render(Map.of("user", new TestUser("gugu", 20)), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\",\"age\":20}"));
    }

    @Test
    void 모델에_데이터가_2개_이상이면_Map_형태_그대로_JSON으로_변환한다() throws Exception {
        final var view = new JsonView();

        view.render(Map.of("user", new TestUser("gugu", 20), "count", 1), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\",\"age\":20},\"count\":1}"));
    }

    @Test
    void 모델이_비어_있으면_빈_JSON_객체를_반환한다() throws Exception {
        final var view = new JsonView();

        view.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    public static class TestUser {

        private final String account;
        private final int age;

        public TestUser(final String account, final int age) {
            this.account = account;
            this.age = age;
        }

        public String getAccount() {
            return account;
        }

        public int getAge() {
            return age;
        }
    }
}
