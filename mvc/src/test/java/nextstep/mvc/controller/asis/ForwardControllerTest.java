package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ForwardControllerTest {

    @DisplayName("기본경로를_반환한다")
    @Test
    void 기본경로를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        String baseUrl = "/";
        ForwardController forward = new ForwardController(baseUrl);

        String actual = forward.execute(request, response);

        assertThat(actual).isEqualTo(baseUrl);
    }
}
