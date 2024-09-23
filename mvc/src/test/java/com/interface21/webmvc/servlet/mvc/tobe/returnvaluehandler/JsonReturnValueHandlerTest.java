package com.interface21.webmvc.servlet.mvc.tobe.returnvaluehandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonReturnValueHandlerTest {

    @DisplayName("String, ModelAndView가 아닌 객체를 지원한다.")
    @Test
    void supportTrue() {
        JsonReturnValueHandler handler = new JsonReturnValueHandler();
        User user = new User("gugu", "account");

        boolean support = handler.support(user);

        assertThat(support).isTrue();
    }

    @DisplayName("String은 지원하지 않는다.")
    @Test
    void supportFalseString() {
        JsonReturnValueHandler handler = new JsonReturnValueHandler();

        boolean support = handler.support("/index.jsp");

        assertThat(support).isFalse();
    }

    @DisplayName("ModelAndView는 지원하지 않는다.")
    @Test
    void supportFalseModelAndView() {
        JsonReturnValueHandler handler = new JsonReturnValueHandler();
        ModelAndView modelAndView = new ModelAndView(new JspView("/index.jsp"));

        boolean support = handler.support(modelAndView);

        assertThat(support).isFalse();
    }

    @DisplayName("객체를 JsonView와 Model에 담는다.")
    @Test
    void handle() {
        JsonReturnValueHandler handler = new JsonReturnValueHandler();
        User user = new User("gugu", "account");

        ModelAndView modelAndView = handler.handle(user);

        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();

        assertAll(
                () -> assertThat(view).isInstanceOf(JsonView.class),
                () -> assertThat(model).isEqualTo(Map.of("value", user))
        );
    }

    static class User {
        private final String name;
        private final String account;

        public User(String name, String account) {
            this.name = name;
            this.account = account;
        }
    }
}
