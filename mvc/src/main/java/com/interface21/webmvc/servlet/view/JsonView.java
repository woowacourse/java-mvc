package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        // JSON으로, 직렬화를 하라는 것이군 왜냐면 render가 화면에 뿌리는거니까
        // 그라믄, 응답에 뭘 담아야하지?
        // request가 필요 없겠네, model에 데이터가 들어 있으니꼐
        response.setContentType(JSON_CONTENT_TYPE); //이것을 해줘야 바디가 인식이 된다고 함!

        //model에 데이터가 1개면 값을 그대로 반환하고, 2개 이상이면 MAP 형태로 없으면? {} 그냥 빈거 보내면되나
        //데이터가 1개면 key가 필요없다는 것이군
        Object data = parseModelToObject(model);
        String json = objectMapper.writeValueAsString(data);
        log.info("json : {}", json);
        try (final var writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }

    private Object parseModelToObject(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().stream().findFirst().get();
        }

        return model;
    }
}
