package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import web.org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerKeyGeneratorTest {

    private final HandlerKeyGenerator handlerKeyGenerator = new HandlerKeyGenerator(new HttpMappingAnnotationParser());

    @Test
    void prefix와_메서드를_입력받아_HandlerKey의_리스트를_반환한다() throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod(
                "find",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        // when
        final List<HandlerKey> handlerKeys = handlerKeyGenerator.generate("/hello", method);

        // then
        assertThat(handlerKeys).containsExactly(new HandlerKey("/hello/get", RequestMethod.GET));
    }
}
