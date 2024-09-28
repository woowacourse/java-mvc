package com.interface21.webmvc.servlet;

import com.interface21.core.util.ReflectionMethodUtils;
import com.interface21.webmvc.servlet.mvc.tobe.NotFoundMethodException;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.NotSupportViewException;

import java.lang.reflect.Method;

public class ViewConverter {
    private static final String VIEW_METHOD_NAME = "getView";

    public static View convert(final Object result) {
        if (result.getClass() == String.class) {
            return new JspView((String) result);
        }
        return findViewWithClass(result);
    }

    private static View findViewWithClass(final Object result) {
        try {
            final Class<?> clazz = result.getClass();
            final Method getViewMethod = ReflectionMethodUtils.getMethodWithOptional(clazz, VIEW_METHOD_NAME)
                    .orElseThrow(() -> new NotFoundMethodException(String.format("%s 에 해당하는 메소드를 발견할 수 없습니다.", VIEW_METHOD_NAME)));
            return (View) getViewMethod.invoke(result);
        } catch (final Exception e) {
            throw new NotSupportViewException(String.format("%s에 해당하는 객체는 추출할 수 없습니다.", result),e);
        }
    }

    private ViewConverter() {}
}
