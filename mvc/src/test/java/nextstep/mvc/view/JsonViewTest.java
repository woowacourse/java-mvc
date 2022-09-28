package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("Model에 들어온 값을 Json으로 파싱한다.")
    @Test
    void Model에_들어온_값을_Json으로_파싱한다() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("account", "gugu");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        given(response.getWriter()).willReturn(printWriter);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("\"gugu\"");
    }
}
