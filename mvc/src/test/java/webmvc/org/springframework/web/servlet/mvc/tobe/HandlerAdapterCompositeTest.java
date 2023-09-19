package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static webmvc.org.springframework.web.servlet.mock.view.MockModelAndViews.HERB_MODEL_AND_VIEW;
import static webmvc.org.springframework.web.servlet.mock.view.MockModelAndViews.HYENA_MODEL_AND_VIEW;

class HandlerAdapterCompositeTest {

    private static final HandlerAdapter HYENA_HANDLER_ADAPTER = new HandlerAdapter() {
        @Override
        public boolean support(final Object handleExecution) {
            return handleExecution.equals("hyena");
        }

        @Override
        public ModelAndView doInternalService(final HttpServletRequest request, final HttpServletResponse response, final Object method) {
            return HYENA_MODEL_AND_VIEW;
        }
    };

    private static final HandlerAdapter HERB_HANDLER_ADAPTER = new HandlerAdapter() {
        @Override
        public boolean support(final Object handleExecution) {
            return handleExecution.equals("herb");
        }

        @Override
        public ModelAndView doInternalService(final HttpServletRequest request, final HttpServletResponse response, final Object method) {
            return HERB_MODEL_AND_VIEW;
        }
    };

    private static final HandlerAdapterComposite handlerAdapterComposite = new HandlerAdapterComposite(List.of(
            HYENA_HANDLER_ADAPTER, HERB_HANDLER_ADAPTER
    ));

    @Test
    void 핸들러_어댑터_목록에서_지원하는_핸들러_어댑터를_찾아_결과값을_반환한다() {
        // given
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // when
        final ModelAndView hyenaMv = handlerAdapterComposite.doService(request, response, "hyena");
        final ModelAndView herbMv = handlerAdapterComposite.doService(request, response, "herb");

        // then
        assertAll(
                () -> assertThat(hyenaMv).isEqualTo(HYENA_MODEL_AND_VIEW),
                () -> assertThat(herbMv).isEqualTo(HERB_MODEL_AND_VIEW)
        );
    }
}
