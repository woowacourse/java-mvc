package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("ControllerHandlerAdapter 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerHandlerAdapterTest {

//    @Test
//    void 컨트롤러_인터페이스를_구현한_핸들러만_지원한다() {
//        // given
//        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
//
//        // when
//        final boolean isSupport = controllerHandlerAdapter.isSupport(new ForwardController("/"));
//
//        // then
//        assertThat(isSupport).isTrue();
//    }
//
//    @Test
//    void 컨트롤러_인터페이스를_구현하지_않은_핸들러는_지원하지_않는다() {
//        // given
//        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
//
//        // when
//        final boolean isSupport = controllerHandlerAdapter.isSupport(new Object());
//
//        // then
//        assertThat(isSupport).isFalse();
//    }
//
//    @Test
//    void 컨트롤러_인터페이스를_구현한_핸들러를_실행한다() throws Exception {
//        // given
//        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
//        final ForwardController forwardController = new ForwardController("/index.jsp");
//
//        // when
//        final ModelAndView modelAndView = controllerHandlerAdapter.handle(null, null, forwardController);
//
//        // then
//        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
//        JspView jspView = (JspView) modelAndView.getView();
//        assertThat(jspView).extracting("viewName").isEqualTo("/index.jsp");
//    }
}
