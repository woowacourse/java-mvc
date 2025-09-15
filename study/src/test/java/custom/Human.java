package custom;

class Being {
    public String publicSuperField = "Super"; // 상속될 public 필드
    private String privateSuperField = "Super Private";

    public void publicSuperMethod() { // 상속될 public 메서드
    }
}

public class Human {
    public static class Person extends Being {
        // Fields
        public String publicField = "Public";
        private String name;
        private int age;

        // Constructors
        private Person(final String name, final int age) { // private 생성자
            this.name = name;
            this.age = age;
        }

        public Person() { // public 생성자
        }

        // Methods
        public static Person of(final String name, final int age) {
            return new Person(name, age);
        }

        public void sayHello() {
            System.out.println("Hello World!");
        }

        private void whisper() {
            System.out.println("secret...");
        }
    }
}
