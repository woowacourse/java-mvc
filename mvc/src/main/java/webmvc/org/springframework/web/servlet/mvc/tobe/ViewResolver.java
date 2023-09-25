package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.View;

public interface ViewResolver {

    boolean supports(Object view);

    View resolve(Object view);
}
