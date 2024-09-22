package com.interface21.web.bind.annotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class RequestMappingTest {

    @Test
    void testGetTestMethodMapping() throws NoSuchMethodException {
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
    void testPostTestMethodMapping() throws NoSuchMethodException {
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
    void testAllMethodSupportWhenNoMethodSpecified() throws NoSuchMethodException {
        // given
        Method dummyMethod = TestController.class.getMethod("dummy");

        // When
        RequestMapping requestMapping = dummyMethod.getAnnotation(RequestMapping.class);

        // Then
        assertEquals("/dummy", requestMapping.value());
        assertArrayEquals(new RequestMethod[]{
                RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.TRACE
        }, requestMapping.method());
    }
}
