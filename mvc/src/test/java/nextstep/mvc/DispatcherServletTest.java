package nextstep.mvc;

import static nextstep.test.MockRequestBuilder.get;
import static nextstep.test.MockRequestBuilder.post;

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
        mockMvc.perform(get("/index"))
                .forwardTo("/index.jsp");
    }

    @Test
    void redirectIndexView() throws Exception {
        mockMvc.perform(get("/redirect-index"))
                .redirectTo("/index.jsp");
    }

    @Test
    void getTestViewWithModel() throws Exception {
        mockMvc.perform(get("/get-test").attribute("id", "gugu"))
                .forwardTo("/get-test.jsp", Map.of("id", "gugu"));
    }

    @Test
    void postTestViewWithModel() throws Exception {
        mockMvc.perform(post("/post-test").attribute("id", "gugu"))
                .forwardTo("/post-test.jsp", Map.of("id", "gugu"));
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/api/user"))
                .jsonBody(new User("verus", 28));
    }

    @Test
    void getUsers() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("user1", new User("verus", 28));
        body.put("user2", new User("gugu", 30));

        mockMvc.perform(get("/api/users"))
                .jsonBody(body);
    }

    @Test
    void emptyBody() throws Exception {
        mockMvc.perform(get("/api/empty"))
                .jsonBody(Map.of());
    }
}
