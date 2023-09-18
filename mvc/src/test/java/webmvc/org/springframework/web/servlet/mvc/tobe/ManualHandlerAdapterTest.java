package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.view.JspView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("ManualHandlerAdapter 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerAdapterTest {

    @Test
    void 컨트롤러_인터페이스를_구현한_핸들러만_지원한다() {
        // given
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        final boolean isSupport = manualHandlerAdapter.isSupport(new ForwardController("/"));

        // then
        assertThat(isSupport).isTrue();
    }

    @Test
    void 컨트롤러_인터페이스를_구현하지_않은_핸들러는_지원하지_않는다() {
        // given
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        final boolean isSupport = manualHandlerAdapter.isSupport(new Object());

        // then
        assertThat(isSupport).isFalse();
    }

    @Test
    void 컨트롤러_인터페이스를_구현한_핸들러를_실행한다() throws Exception {
        // given
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        final ForwardController forwardController = new ForwardController("/index.jsp");

        // when
        final ModelAndView modelAndView = manualHandlerAdapter.handle(null, null, forwardController);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        JspView jspView = (JspView) modelAndView.getView();
        assertThat(jspView).extracting("viewName").isEqualTo("/index.jsp");
    }
}
