package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void model의_기본타입_데이터를_JSON으로_변환한다() throws Exception {
        // given
        final var view = new JsonView();
        final var model = Map.of("user", "박채영");

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        // when
        view.render(model, request, response);

        // then
        verify(writer).write("{\"user\":\"박채영\"}");
    }

    @Test
    void model의_커스텀_객체_데이터를_JSON으로_변환한다() throws Exception {
        // given
        final var view = new JsonView();
        final var model = Map.of(
                "user", new TestClass(1L, "라리사", new TestInnerClass(true, 10_000))
        );

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        // when
        view.render(model, request, response);

        // then
        verify(writer).write("{\"user\":{\"id\":1,\"name\":\"라리사\",\"innerClass\":{\"payed\":true,\"amount\":10000}}}");
    }

    @Test
    void model에_데이터가_여러개면_Map_그대로_반환한다() throws IOException {
        // given
        final var view = new JsonView();
        final var model = new LinkedHashMap<String, Object>();
        model.put("user1", "김지수");
        model.put("user2", new TestClass(2L, "김제니"));

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        // when
        view.render(model, request, response);

        // then
        verify(writer).write("{\"user1\":\"김지수\",\"user2\":{\"id\":2,\"name\":\"김제니\",\"innerClass\":null}}");
    }

    private static class TestClass {

        private Long id;
        private String name;
        private TestInnerClass innerClass;

        public TestClass(final Long id, final String name) {
            this(id, name, null);
        }

        public TestClass(final Long id, final String name, final TestInnerClass innerClass) {
            this.id = id;
            this.name = name;
            this.innerClass = innerClass;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public TestInnerClass getInnerClass() {
            return innerClass;
        }
    }

    private static class TestInnerClass {

        private boolean payed;
        private int amount;

        public TestInnerClass(final boolean payed, final int amount) {
            this.payed = payed;
            this.amount = amount;
        }

        public boolean isPayed() {
            return payed;
        }

        public int getAmount() {
            return amount;
        }
    }
}
