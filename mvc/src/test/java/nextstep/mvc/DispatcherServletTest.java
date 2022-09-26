package nextstep.mvc;

import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.test.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;
import samples.User;

class DispatcherServletTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = new MockMvc(
                new TestManualHandlerMapping(),
                new AnnotationHandlerMapping("samples")
        );
    }

    @Test
    void forwardIndexView() throws Exception {
        mockMvc.perform("/index", "GET")
                .forwardTo("/index.jsp");
    }

    @Test
    void redirectIndexView() throws Exception {
        mockMvc.perform("/redirect-index", "GET")
                .redirectTo("/index.jsp");
    }

    @Test
    void getTestViewWithModel() throws Exception {
        mockMvc.perform("/get-test", "GET", Map.of("id", "gugu"))
                .forwardTo("/get-test.jsp", Map.of("id", "gugu"));
    }

    @Test
    void postTestViewWithModel() throws Exception {
        mockMvc.perform("/post-test", "POST", Map.of("id", "gugu"))
                .forwardTo("/post-test.jsp", Map.of("id", "gugu"));
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform("/api/user", "GET")
                .jsonBody(new User("verus", 28));
    }

    @Test
    void getUsers() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("user1", new User("verus", 28));
        body.put("user2", new User("gugu", 30));

        mockMvc.perform("/api/users", "GET")
                .jsonBody(body);
    }

    @Test
    void emptyBody() throws Exception {
        mockMvc.perform("/api/empty", "GET")
                .jsonBody(Map.of());
    }
}
