package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @BeforeEach
    void setUp() {
        cleanCache();
    }

    @DisplayName("캐시에 해당 viewName을 가진 객체가 존재하지 않는 경우 객체를 생성한 후 반환한다.")
    @Test
    void should_createInstance_when_NotExist() {
        // given
        assertThat(getCacheSize()).isEqualTo(0);

        // when
        JspView.from("new");

        // then
        assertThat(getCacheSize()).isEqualTo(1);
    }

    @DisplayName("캐시에 해당 viewName을 가진 객체가 존재하는 경우 캐시에서 꺼내 반환한다.")
    @Test
    void should_returnInstance_when_Exist() {
        // given
        JspView exist = JspView.from("exist");
        assertThat(getCacheSize()).isEqualTo(1);

        // when
        JspView created = JspView.from("exist");

        // then
        assertThat(created).isSameAs(exist);
        assertThat(getCacheSize()).isEqualTo(1);
    }

    private int getCacheSize() {
        try {
            Field cacheField = JspView.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            Map<String, JspView> cache = (Map<String, JspView>) cacheField.get(null);
            return cache.size();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("CACHE 사이즈에 접근할 수 없습니다.");
        }
    }

    private void cleanCache() {
        try {
            Field cacheField = JspView.class.getDeclaredField("CACHE");
            cacheField.setAccessible(true);
            Map<String, JspView> cache = (Map<String, JspView>) cacheField.get(null);
            cache.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("CACHE를 초기화할 수 없습니다.");
        }
    }
}
