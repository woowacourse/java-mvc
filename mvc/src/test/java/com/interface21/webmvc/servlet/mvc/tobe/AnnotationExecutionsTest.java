package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationExecutionsTest {

    @Test
    void executor를_추가한다()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        AnnotationExecutions annotationExecutions = new AnnotationExecutions();
        TestController controller = TestController.class.newInstance();
        Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // when
        annotationExecutions.addExecutor(controller, method);

        // then
        assertThat(annotationExecutions.hasHandler("/get-test", RequestMethod.GET)).isTrue();
    }
}
