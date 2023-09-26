package webmvc.org.springframework.web.servlet.mvc.adaptor;

import java.util.Map;
import java.util.Map.Entry;
import webmvc.org.springframework.web.servlet.view.JsonView;
import webmvc.org.springframework.web.servlet.view.JspView;
import webmvc.org.springframework.web.servlet.view.ModelAndView;

public class ModelAndViewMapper {

    private ModelAndViewMapper(){
    }

    public static ModelAndView mapping(Object result){
        if(result instanceof String){
            return new ModelAndView(new JspView((String) result));
        }

        if(result instanceof ModelAndView){
            return (ModelAndView) result;
        }

        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }

        if(result instanceof Map){
            return getModelAndJsonView(((Map<String,?>) result));
        }
        throw new IllegalArgumentException();
    }

    private static ModelAndView getModelAndJsonView(Map<String, ?> result){
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        for(Entry entry: result.entrySet()){
            modelAndView.addObject((String) entry.getKey(),entry.getValue());
        }
        return new ModelAndView(new JsonView());
    }
}
