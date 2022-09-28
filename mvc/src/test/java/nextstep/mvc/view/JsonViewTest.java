package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import support.DummyPrintWriter;
import support.User;

class JsonViewTest {

    @DisplayName("render 메서드는")
    @Nested
    class ParentTestClass {

        private final JsonView jsonView = new JsonView();

        @DisplayName("Model이 비어있을 경우 {}를 작성한다")
        @Test
        void render_should_write_empty_with_curly_brackets_when_model_is_empty() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            final DummyPrintWriter writer = new DummyPrintWriter();
            given(response.getWriter()).willReturn(writer);

            final Map<String, ?> model = new HashMap<>();

            // when
            jsonView.render(model, request, response);

            // then
            assertThat(writer.written).isEqualTo("{}");
        }

        @DisplayName("Model의 길이가 1일 경우 key없이 value만 작성한다")
        @Test
        void render_should_write_only_values_when_model_size_is_one() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            final DummyPrintWriter writer = new DummyPrintWriter();
            given(response.getWriter()).willReturn(writer);

            final Map<String, Object> model = new HashMap<>();
            model.put("users", new User(1L, "gugu", "hkkang@woowahan.com"));

            // when
            jsonView.render(model, request, response);

            // then
            assertThat(writer.written).isEqualTo(
                    "{\"id\":1,\"account\":\"gugu\",\"email\":\"hkkang@woowahan.com\"}");
        }

        @DisplayName("Model의 길이가 2 이상일 경우 key, value 모두 작성한다")
        @Test
        void render_should_write_both_keys_and_values_when_model_size_is_greater_than_one() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            final DummyPrintWriter writer = new DummyPrintWriter();
            given(response.getWriter()).willReturn(writer);

            final Map<String, Object> model = new HashMap<>();
            model.put("users", List.of(
                    new User(1L, "gugu", "hkkang@woowahan.com"),
                    new User(2L, "philz", "progress0407@gmail.com"),
                    new User(3L, "richard", "ztzy1907@gmail.com")
            ));
            model.put("sample", "value");

            // when
            jsonView.render(model, request, response);

            // then
            assertThat(writer.written).isEqualTo(
                    "{"
                            + "\"sample\":\"value\","
                            + "\"users\":["
                            + "{\"id\":1,\"account\":\"gugu\",\"email\":\"hkkang@woowahan.com\"},"
                            + "{\"id\":2,\"account\":\"philz\",\"email\":\"progress0407@gmail.com\"},"
                            + "{\"id\":3,\"account\":\"richard\",\"email\":\"ztzy1907@gmail.com\"}"
                            + "]"
                            + "}");
        }
    }
}
