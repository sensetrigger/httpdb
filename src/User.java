import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public int age;
    public double salary;

    public User(String name, int age, double salary)

    {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }
}
