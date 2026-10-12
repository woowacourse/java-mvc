package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("JSON 뷰 렌더링")
class JsonViewTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonView view;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        view = new JsonView();
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("응답 본문")
    class Body {

        @DisplayName("모델 항목이 하나면 바깥 이름 없이 값 자체를 JSON으로 응답한다")
        @ParameterizedTest(name = "[{index}] 단일 모델 값: {0}")
        @MethodSource("singleValueCases")
        void serializesSingleValue(final String caseName, final Map<String, ?> model,
                                   final String expectedJson) throws Exception {
            // given
            final var expected = OBJECT_MAPPER.readTree(expectedJson);

            // when
            view.render(model, request, response);

            // then
            assertThat(OBJECT_MAPPER.readTree(response.getContentAsString())).isEqualTo(expected);
        }

        static Stream<Arguments> singleValueCases() {
            return Stream.of(
                    Arguments.of("문자열", Map.of("message", "hello"), "\"hello\""),
                    Arguments.of("숫자", Map.of("count", 7), "7"),
                    Arguments.of("불리언", Map.of("success", true), "true"),
                    Arguments.of("객체의 여러 속성", Map.of("user", new Profile("gugu", "gugu@example.com")),
                            "{\"account\":\"gugu\",\"email\":\"gugu@example.com\"}"),
                    Arguments.of("목록", Map.of("accounts", List.of("gugu", "brown")), "[\"gugu\",\"brown\"]"),
                    Arguments.of("null", Collections.singletonMap("user", null), "null")
            );
        }

        @Test
        @DisplayName("모델 항목이 여러 개면 이름을 포함한 모델 전체를 JSON으로 응답한다")
        void serializesMultipleEntries() throws Exception {
            // given
            final var model = Map.of("user", new Profile("gugu", "gugu@example.com"), "count", 1);
            final var expected = OBJECT_MAPPER.readTree("""
                    {"user":{"account":"gugu","email":"gugu@example.com"},"count":1}
                    """);

            // when
            view.render(model, request, response);

            // then
            assertThat(OBJECT_MAPPER.readTree(response.getContentAsString())).isEqualTo(expected);
        }

        @Test
        @DisplayName("모델이 비어 있으면 빈 JSON 객체로 응답한다")
        void serializesEmptyModel() throws Exception {
            // given
            final Map<String, ?> model = Map.of();

            // when
            view.render(model, request, response);

            // then
            assertThat(response.getContentAsString()).isEqualTo("{}");
        }
    }

    @Nested
    @DisplayName("응답 설정과 출력")
    class ResponseOutput {

        @Test
        @DisplayName("JSON과 UTF-8을 나타내는 Content-Type으로 응답한다")
        void setsJsonContentType() throws Exception {
            // given
            final var model = Map.of("message", "hello");

            // when
            view.render(model, request, response);

            // then
            assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        @Test
        @DisplayName("한글과 이모지를 UTF-8 바이트로 응답한다")
        void writesUtf8Content() throws Exception {
            // given
            final var model = Map.of("message", "안녕하세요 👋");
            final var expected = "\"안녕하세요 👋\"".getBytes(StandardCharsets.UTF_8);

            // when
            view.render(model, request, response);

            // then
            assertThat(response.getContentAsByteArray()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Writer를 얻기 전에 Content-Type을 설정한다")
        void configuresContentTypeBeforeObtainingWriter() throws Exception {
            // given
            final var output = mock(HttpServletResponse.class);
            when(output.getWriter()).thenReturn(mock(PrintWriter.class));

            // when
            view.render(Map.of("message", "hello"), request, output);

            // then
            final var order = inOrder(output);
            order.verify(output).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            order.verify(output).getWriter();
        }

        @Test
        @DisplayName("서블릿 컨테이너가 관리하는 Writer를 닫지 않는다")
        void doesNotCloseResponseWriter() throws Exception {
            // given
            final var output = mock(HttpServletResponse.class);
            final var writer = mock(PrintWriter.class);
            when(output.getWriter()).thenReturn(writer);

            // when
            view.render(Map.of("message", "hello"), request, output);

            // then
            verify(writer, never()).close();
        }
    }

    @Nested
    @DisplayName("직렬화 실패")
    class SerializationFailure {

        @Test
        @DisplayName("직렬화 예외를 호출자에게 전달한다")
        void propagatesSerializationFailure() {
            // given
            final var model = Map.of("user", new FailingValue());

            // when & then
            assertThatThrownBy(() -> view.render(model, request, response))
                    .isInstanceOf(JsonProcessingException.class);
        }

        @Test
        @DisplayName("직렬화에 실패하면 응답을 변경하지 않는다")
        void doesNotTouchResponseOnSerializationFailure() {
            // given
            final var output = mock(HttpServletResponse.class);
            final var model = Map.of("user", new FailingValue());

            // when
            catchThrowable(() -> view.render(model, request, output));

            // then
            verifyNoInteractions(output);
        }
    }

    public record Profile(String account, String email) {
    }

    public static class FailingValue {

        public String getAccount() {
            throw new IllegalStateException("account cannot be read");
        }
    }
}
