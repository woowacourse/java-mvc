package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @BeforeEach
    void setUp() {
        HandlerKey.cleanCache();
    }

    @DisplayName("캐시에 존재하지 않는 HandlerKey 생성을 시도하는 경우 해당 인스턴스를 캐시에 추가 후 반환한다.")
    @Test
    void should_addInCache_when_createNewHandlerKey() {
        // given & when
        HandlerKey.from("/new", RequestMethod.GET);

        // then
        assertThat(getCacheSize()).isEqualTo(1);
    }

    @DisplayName("캐시에 존재하는 HandlerKey 생성을 시도하는 경우 캐시 내 인스턴스를 반환한다.")
    @Test
    void should_getInCache_when_createExistHandlerKey() {
        // given
        HandlerKey exist = HandlerKey.from("/url", RequestMethod.GET);

        // when
        HandlerKey created = HandlerKey.from("/url", RequestMethod.GET);

        // then
        assertThat(created).isEqualTo(exist);
        assertThat(getCacheSize()).isEqualTo(1);
    }

    private int getCacheSize() {
        try {
            Field cacheField = HandlerKey.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            List<HandlerKey> cache = (List<HandlerKey>) cacheField.get(null);
            return cache.size();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("CACHE 사이즈에 접근할 수 없습니다.");
        }
    }
}
