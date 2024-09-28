package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Json 뷰")
class JsonViewTest {

    @DisplayName("Json 뷰는 모델에 데이터가 한 개일 때는 값만 반환한다.")
    @Test
    void returnOnlyValue() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("user", "test");

        // when
        Method parseBody = jsonView.getClass().getDeclaredMethod("parseBody", Map.class);
        parseBody.setAccessible(true);
        String actual = (String) parseBody.invoke(jsonView, model);

        // then
        assertThat(actual).isEqualTo("test");
    }

    @DisplayName("Json 뷰는 모델에 데이터가 여러 개일 때 값을 Map 형태를 그대로 반환한다.")
    @Test
    void returnMapValue() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("user", "test", "email", "test@gmail.com");

        // when
        Method parseBody = jsonView.getClass().getDeclaredMethod("parseBody", Map.class);
        parseBody.setAccessible(true);
        String actual = (String) parseBody.invoke(jsonView, model);

        // then
        assertThat(actual).isEqualTo("{\"user\":\"test\",\"email\":\"test@gmail.com\"}");
    }
}
