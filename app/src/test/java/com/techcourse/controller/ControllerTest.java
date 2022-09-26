package com.techcourse.controller;

import static nextstep.test.MockRequestBuilder.get;
import static nextstep.test.MockRequestBuilder.post;
import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.ManualHandlerMapping;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import java.util.Optional;
import nextstep.test.MockMvc;
import org.junit.jupiter.api.Test;

public class ControllerTest {

    @Test
    void forwardIndexPage() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(get("/"))
                .forwardTo("/index.jsp");
    }

    @Test
    void loginSuccessfully() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(post("/login")
                .param("account", "gugu")
                .param("password", "password")
        )
                .redirectTo("/index.jsp");
    }

    @Test
    void loginFailed() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(post("/login")
                .param("account", "gugu")
                .param("password", "invalid")
        )
                .redirectTo("/401.jsp");
    }

    @Test
    void loginView() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(get("/login/view"))
                .forwardTo("/login.jsp");
    }

    @Test
    void logout() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(post("/logout"))
                .redirectTo("/");
    }

    @Test
    void register() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(post("/register")
                .param("account", "verus")
                .param("password", "password")
                .param("email", "verus@email.com")
        )
                .redirectTo("/index.jsp");

        final Optional<User> verus = InMemoryUserRepository.findByAccount("verus");
        assertThat(verus).isPresent();
    }

    @Test
    void registerView() throws Exception {
        MockMvc mockMvc = new MockMvc(new ManualHandlerMapping());

        mockMvc.perform(get("/register/view"))
                .forwardTo("/register.jsp");
    }
}
