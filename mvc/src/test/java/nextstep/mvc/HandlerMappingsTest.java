package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.mvc.handler.mapping.HandlerMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    @DisplayName("핸들러 매핑은 적절한 핸들러를 반환한다.")
    @Test
    void get() {
        HandlerMappings handlerMappings = new HandlerMappings(List.of(
            new TestHandlerMapping1(),
            new TestHandlerMapping2()
        ));

        Object handlerMapping = handlerMappings.get(null).get();
        assertThat(handlerMapping).isInstanceOf(TestHandlerMapping2.class);
    }

    @DisplayName("핸들러 매핑을 저장한다.")
    @Test
    void add() throws NoSuchFieldException, IllegalAccessException {
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.add(new TestHandlerMapping1());
        handlerMappings.add(new TestHandlerMapping2());

        Field values = HandlerMappings.class.getDeclaredField("values");
        values.trySetAccessible();
        List o = (List) values.get(handlerMappings);
        assertThat(o).hasSize(2);
    }

    @DisplayName("handlerMapping들을 초기화 한다.")
    @Test
    void initialize() {
        TestHandlerMapping1 testHandlerMapping1 = new TestHandlerMapping1();
        TestHandlerMapping2 testHandlerMapping2 = new TestHandlerMapping2();
        HandlerMappings handlerMappings = new HandlerMappings(List.of(
            testHandlerMapping1,
            testHandlerMapping2
        ));

        handlerMappings.initialize();

        assertThat(testHandlerMapping1.isInit).isTrue();
        assertThat(testHandlerMapping2.isInit).isTrue();
    }

    public static class TestHandlerMapping1 implements HandlerMapping, init {

        private boolean isInit = false;

        @Override
        public void initialize() {
            this.isInit = true;
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return null;
        }

        @Override
        public boolean isInit() {
            return isInit;
        }
    }

    public static class TestHandlerMapping2 implements HandlerMapping, init {

        private boolean isInit = false;

        @Override
        public void initialize() {
            this.isInit = true;
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return this;
        }

        @Override
        public boolean isInit() {
            return isInit;
        }
    }

    interface init {

        boolean isInit();
    }
}
