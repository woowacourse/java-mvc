package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import samples.TestAnnotationController;
import samples.TestManualController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ManualHandlerAdapter;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerAdapterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void Controller를_구현한_handler는_지원한다() {
        // given
        TestManualController handler = new TestManualController();
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        boolean expected = manualHandlerAdapter.supports(handler);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void Controller를_구현하지_않은_handler는_지원하지_않는다() {
        // given
        TestAnnotationController handler = new TestAnnotationController();
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        boolean expected = manualHandlerAdapter.supports(handler);

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void handler를_다룬다() {
        // given
        TestManualController handler = new TestManualController();
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        ModelAndView modelAndView = manualHandlerAdapter.handle(handler, request, response);

        // then
        assertThat(modelAndView.getView().getViewName()).isEqualTo("/tests");
    }
}
