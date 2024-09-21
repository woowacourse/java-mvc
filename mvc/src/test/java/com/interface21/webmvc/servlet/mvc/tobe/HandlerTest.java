package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import java.lang.reflect.Method;

class HandlerTest {

    @DisplayName("핸들러의 URL 반환 성공")
    @Test
    void getUrl() throws Exception {
        // given
        TestController newInstance = TestController.class.getConstructor().newInstance();
        Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        Handler handler = new Handler(newInstance, method);

        // when & then
        Assertions.assertThat(handler.getUrl()).isEqualTo(TestController.GET_URL);
    }

    @DisplayName("핸들러의 RequestMethod 반환 성공")
    @Test
    void getRequestMethods() throws Exception {
        // given
        RequestMethod[] expectedRequestMethod = {RequestMethod.GET};
        TestController newInstance = TestController.class.getConstructor().newInstance();
        Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        Handler handler = new Handler(newInstance, method);

        // when & then
        Assertions.assertThat(handler.getRequestMethods()).isEqualTo(expectedRequestMethod);
    }
}
