package com.interface21.web.bind.annotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.SampleController;
import samples.TestController;

public class RequestMappingTest {

    @Test
    void 메서드에_URL과_GET_메서드를_매핑해_둘_수_있다() throws NoSuchMethodException {
        // Given
        Method getMethod = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // When
        RequestMapping requestMapping = getMethod.getAnnotation(RequestMapping.class);

        // Then
        assertEquals("/get-test", requestMapping.value());
        assertArrayEquals(new RequestMethod[]{RequestMethod.GET}, requestMapping.method());
    }

    @Test
    void 메서드에_URL과_POST_메서드를_매핑해_둘_수_있다() throws NoSuchMethodException {
        // Given
        Method postMethod = TestController.class.getMethod(
                "save", HttpServletRequest.class, HttpServletResponse.class);

        // When
        RequestMapping requestMapping = postMethod.getAnnotation(RequestMapping.class);

        // Then
        assertEquals("/post-test", requestMapping.value());
        assertArrayEquals(new RequestMethod[]{RequestMethod.POST}, requestMapping.method());
    }

    @Test
    void 메서드를_지정하지_않았을_땐_모든_메서드를_매핑해둔다() throws NoSuchMethodException {
        // given
        Method dummyMethod = SampleController.class.getMethod("dummy");

        // When
        RequestMapping requestMapping = dummyMethod.getAnnotation(RequestMapping.class);

        // Then
        assertEquals("/dummy", requestMapping.value());
        assertArrayEquals(RequestMethod.values(), requestMapping.method());
    }
}
