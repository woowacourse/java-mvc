package study;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.HashMap;
import java.util.Map;

public class ExtendableBean {
    public String name;
    private Map<String, String> properties;

    public ExtendableBean(String name) {
        this.name = name;
        this.properties = new HashMap<>();
    }

    public void add(String attr, String val) {
        properties.put(attr, val);
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
