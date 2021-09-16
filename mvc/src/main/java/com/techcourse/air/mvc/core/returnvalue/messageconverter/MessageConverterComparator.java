package com.techcourse.air.mvc.core.returnvalue.messageconverter;

import java.util.Comparator;

import com.techcourse.air.core.annotation.Order;

public class MessageConverterComparator implements Comparator<HttpMessageConverter<?>> {

    @Override
    public int compare(HttpMessageConverter<?> converter1, HttpMessageConverter<?> converter2) {
        Order converter1Order = converter1.getClass().getDeclaredAnnotation(Order.class);
        Order converter2Order = converter2.getClass().getDeclaredAnnotation(Order.class);
        Integer value1 = converter1Order.value();
        Integer value2 = converter2Order.value();
        return value1.compareTo(value2);
    }
}
