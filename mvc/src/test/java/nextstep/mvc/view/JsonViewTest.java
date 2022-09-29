package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("모델 값이 없을 때 빈 값을 브라우저에 보여준다.")
    @Test
    void render_EmptyMap() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        //when
        JsonView jsonView = new JsonView();
        jsonView.render(Collections.emptyMap(), request, response);

        //then
        verify(response.getWriter()).write("");
    }

    @DisplayName("모델 값이 하나 뿐일 땐 해당 값을 브라우저에 보여준다.")
    @Test
    void render_oneMap() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        //when
        JsonView jsonView = new JsonView();
        jsonView.render(Map.of("user", new UserDto(1L, "hunch", "password", "abcd@abcd")), request, response);

        //then
        verify(response.getWriter()).write(
                "{\"id\":1,\"account\":\"hunch\",\"password\":\"password\",\"email\":\"abcd@abcd\"}");
    }

    @DisplayName("모델 값이 여러 개일 땐 Json 포맷으로 브라우저에 보여준다.")
    @Test
    void render_multiMap() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        JsonView jsonView = new JsonView();
        UserDto userDto1 = new UserDto(1L, "hunch", "password", "abcd@abcd");
        LinkedHashMap<String, UserDto> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("user1", userDto1);
        linkedHashMap.put("user2", userDto1);

        //when
        jsonView.render(linkedHashMap, request, response);

        //then
        verify(response.getWriter()).write(
                "{\"user1\":{\"id\":1,\"account\":\"hunch\",\"password\":\"password\",\"email\":\"abcd@abcd\"}"
                        + ",\"user2\":{\"id\":1,\"account\":\"hunch\",\"password\":\"password\",\"email\":\"abcd@abcd\"}}");
    }
}
