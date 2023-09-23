package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.exception.HandlerKeyException;

import java.util.ArrayList;
import java.util.List;

public class HandlerKeyDuplicateChecker {

    private final List<HandlerKey> handlerKeys = new ArrayList<>();

    public void checkWithStackedHandlerKeys(final List<HandlerKey> newHandlerKeys) {
        validateDuplicatedHandlerKey(newHandlerKeys);

        handlerKeys.addAll(newHandlerKeys);
    }

    private void validateDuplicatedHandlerKey(final List<HandlerKey> newHandlerKeys) {
        newHandlerKeys.stream()
                .filter(handlerKeys::contains)
                .findFirst()
                .ifPresent(duplicatedHandlerKey -> {
                    throw new HandlerKeyException(String.format("[ERROR] %s 는 중복될 수 없습니다.", duplicatedHandlerKey));
                });
    }
}
