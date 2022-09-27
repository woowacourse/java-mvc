package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("model에 하나의 객체가 담겨있으면 해당 객체를 json으로 마샬링한다")
    void writeObjectStringWhenHasSingleObject() throws Exception {
        // given
        JsonView view = new JsonView();

        Map<String, Object> model = new HashMap<>();
        Dummy obj = new Dummy("tester", 10);
        model.put("obj", obj);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        view.render(model, request, response);

        // then
        String value = writeValueAsString(obj);
        verify(writer, times(1)).write(value);
    }

    @Test
    @DisplayName("하나의 객체가 담겨있지 않으면 Map 자체를 json으로 마샬링한다")
    void writeMapWhenNotSingleObject() throws Exception{
        // given
        JsonView view = new JsonView();

        Map<String, Object> model = new HashMap<>();
        model.put("obj1", new Dummy("tester1", 10));
        model.put("obj2", new Dummy("tester2", 11));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        view.render(model, request, response);

        // then
        String value = writeValueAsString(model);
        verify(writer, times(1)).write(value);
    }

    private String writeValueAsString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    static class Dummy {
        String name;
        int age;

        public Dummy(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
