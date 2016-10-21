package cn.jxzhang.hibernate.entities;

import javax.persistence.Entity;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-20 21:06
 */
public class Salary {
    private float monthlySalary;
    private float yearSalary;

    public Salary() {
    }

    public Salary(float monthlySalary, float yearSalary) {

        this.monthlySalary = monthlySalary;
        this.yearSalary = yearSalary;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "monthlySalary=" + monthlySalary +
                ", yearSalary=" + yearSalary +
                '}';
    }

    public float getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(float monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public float getYearSalary() {
        return yearSalary;
    }

    public void setYearSalary(float yearSalary) {
        this.yearSalary = yearSalary;
    }
}
