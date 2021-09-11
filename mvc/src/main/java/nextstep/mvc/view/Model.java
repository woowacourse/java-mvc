package nextstep.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model {

    private final Map<String, Object> attributes;

    public Model() {
        this.attributes = new HashMap<>();
    }

    public Model(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Set<String> keySet() {
        return attributes.keySet();
    }

    public Map<String, Object> asMap(){
        return Collections.unmodifiableMap(attributes);
    }
}
