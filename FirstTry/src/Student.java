public class Student extends Person {
    int course;

    Student(int height, int weight, String name, int course) {
        super(height, weight, name);

        this.course = course;
    }

    void work() {
        System.out.printf("Student %s is working...\n", this.name);
    }

    int makeSomeCalculation(int another) {
        return this.height + this.weight + another;
    }
}
