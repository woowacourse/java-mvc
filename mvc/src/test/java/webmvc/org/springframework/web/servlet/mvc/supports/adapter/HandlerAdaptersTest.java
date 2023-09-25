package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.supports.mapping.HandlerExecution;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdaptersTest {

    @Test
    void 생성자는_호출하면_HandlerAdapters를_초기화한다() {
        assertDoesNotThrow(HandlerAdapters::new);
    }

    @Test
    void getHandlerAdapter_메서드는_해당_핸들러를_처리할_수_있는_HandlerAdapter가_있다면_해당_HandlerAdapter를_반환한다()
            throws NoSuchMethodException {
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        final TestController handler = new TestController();
        final HandlerAdapter actual = handlerAdapters.getHandlerAdapter(new HandlerExecution(
                handler,
                handler.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class)
        ));

        assertThat(actual).isNotNull();
    }
}
