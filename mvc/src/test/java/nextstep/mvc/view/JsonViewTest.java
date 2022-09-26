package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.DataNotExistException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    void render시_response_outputStream에_json형태로_write된다() throws Exception {
        // given
        final UserTest userTest = new UserTest(1, "corinne", "1234", "corinne@gmail.com");
        final JsonView jsonView = new JsonView();

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final MockOutputStream outputStream = new MockOutputStream();
        when(response.getOutputStream())
                .thenReturn(outputStream);
        final String expected = "{\"user\":{\"id\":1,\"account\":\"corinne\",\"password\":\"1234\",\"email\":\"corinne@gmail.com\"}}";

        // when
        jsonView.render(Map.of("user", userTest), mock(HttpServletRequest.class), response);

        // then
        assertThat(outputStream.getValue()).isEqualTo(expected);
    }

    @Test
    void 지정된_데이터가_없다면_빈_값을_write_한다() throws Exception {
        // given
        final UserTest userTest = new UserTest(1, "corinne", "1234", "corinne@gmail.com");
        final JsonView jsonView = new JsonView();

        final HttpServletResponse response = mock(HttpServletResponse.class);
        final MockOutputStream outputStream = new MockOutputStream();
        when(response.getOutputStream())
                .thenReturn(outputStream);
        final String expected = "{}";

        // when
        jsonView.render(Collections.emptyMap(), mock(HttpServletRequest.class), response);

        // then
        assertThat(outputStream.getValue()).isEqualTo(expected);
    }
    
    @Test
    void model이_null이라면_예외를_반환한다() {
        // given. when. then
        final JsonView jsonView = new JsonView();
        assertThatThrownBy(() -> jsonView.render(null, mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isInstanceOf(DataNotExistException.class);
    }
}
