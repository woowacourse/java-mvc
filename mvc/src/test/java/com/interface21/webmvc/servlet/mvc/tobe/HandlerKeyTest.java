package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @BeforeEach
    void setUp() {
        cleanCache();
    }

    @DisplayName("캐시에 존재하지 않는 HandlerKey 생성을 시도하는 경우 해당 인스턴스를 캐시에 추가 후 반환한다.")
    @Test
    void should_addInCache_when_createNewHandlerKey() {
        // given
        assertThat(getCacheSize()).isEqualTo(0);

        // when
        HandlerKey.from("/new", RequestMethod.GET);

        // then
        assertThat(getCacheSize()).isEqualTo(1);
    }

    @DisplayName("캐시에 존재하는 HandlerKey 생성을 시도하는 경우 캐시 내 인스턴스를 반환한다.")
    @Test
    void should_getInCache_when_createExistHandlerKey() {
        // given
        HandlerKey exist = HandlerKey.from("/url", RequestMethod.GET);
        assertThat(getCacheSize()).isEqualTo(1);

        // when
        HandlerKey created = HandlerKey.from("/url", RequestMethod.GET);

        // then
        assertThat(created).isSameAs(exist);
        assertThat(getCacheSize()).isEqualTo(1);
    }

    private int getCacheSize() {
        try {
            Field cacheField = HandlerKey.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            Map<String, HandlerKey> cache = (Map<String, HandlerKey>) cacheField.get(null);
            return cache.size();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("CACHE 사이즈에 접근할 수 없습니다.");
        }
    }

    private void cleanCache() {
        try {
            Field cacheField = HandlerKey.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            Map<String, HandlerKey> cache = (Map<String, HandlerKey>) cacheField.get(null);
            cache.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("CACHE를 초기화할 수 없습니다.");
        }
    }
}
