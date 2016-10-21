package cn.jxzhang.hibernate.entities;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-20 21:04
 */
@Entity
public class Programmer {
    private long id;
    private String name;
    private int age;
    private Date hiredate;
    private Salary salary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Programmer(long id, String name, int age, Date hiredate, Salary salary) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.hiredate = hiredate;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Programmer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", hiredate=" + hiredate +
                ", salary=" + salary +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public Programmer() {

    }

    public Programmer(String name, int age, Date hiredate, Salary salary) {

        this.name = name;
        this.age = age;
        this.hiredate = hiredate;
        this.salary = salary;
    }
}
