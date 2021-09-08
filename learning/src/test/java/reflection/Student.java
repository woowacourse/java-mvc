package reflection;

import annotation.Controller;

@Controller
public class Student {

    private String name;

    private int age;

    private int score;

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Student{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}