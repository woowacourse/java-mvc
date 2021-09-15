package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("JsonView 테스트")
class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ModelAndView modelAndView;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        modelAndView = new ModelAndView();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    void stringValueReturnWhenJsonDataSet() throws Exception {
        //given
        JsonView jsonView = new JsonView("hihi?");
        //when
        when(response.getWriter()).thenReturn(writer);

        jsonView.render(modelAndView.getModel(), request, response);
        //then
        assertThat(stringWriter.toString()).isEqualTo("hihi?");
    }


    @Test
    void mapToJsonSingleValue() throws Exception {
        //given
        JsonView jsonView = new JsonView();
        //when
        modelAndView.addObject("hihi", new User(2, "hihiAccount", "hihiPassword", "hihiEmail"));
        when(response.getWriter()).thenReturn(writer);

        jsonView.render(modelAndView.getModel(), request, response);
        //then
        assertThat(stringWriter.toString()).isEqualTo(
                "{\"id\":2,\"account\":\"hihiAccount\",\"password\":\"hihiPassword\",\"email\":\"hihiEmail\"}"
        );
    }

    @Test
    void mapToJsonMultiValue() throws Exception {
        //given
        JsonView jsonView = new JsonView();
        //when
        modelAndView.addObject("hihi", new User(2, "hihiAccount", "hihiPassword", "hihiEmail"));
        modelAndView.addObject("bibi", new User(3, "bibiAccount", "bibiPassword", "bibiEmail"));
        when(response.getWriter()).thenReturn(writer);

        jsonView.render(modelAndView.getModel(), request, response);
        //then
        assertThat(stringWriter.toString()).isEqualTo(
                "{\"bibi\":{\"id\":3,\"account\":\"bibiAccount\",\"password\":\"bibiPassword\",\"email\":\"bibiEmail\"}," +
                "\"hihi\":{\"id\":2,\"account\":\"hihiAccount\",\"password\":\"hihiPassword\",\"email\":\"hihiEmail\"}}"
        );
    }

    static class User {
        private final long id;
        private final String account;
        private final String password;
        private final String email;

        User(long id, String account, String password, String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }
    }
}