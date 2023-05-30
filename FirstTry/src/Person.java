import java.text.MessageFormat;

public class Person {
    int height;
    int weight;
    String name;

    Person(int height, int weight, String name) {
        this.height = height;
        this.weight = weight;
        this.name = name;
    }

    Person(String name) {
        this.height = 0;
        this.weight = 0;
        this.name = name;
    }

    void greet() {
        System.out.printf("Hello, I'm %s with stats: %dh, %dw", this.name, this.height, this.weight);
    }
}
