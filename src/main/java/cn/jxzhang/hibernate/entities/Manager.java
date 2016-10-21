package cn.jxzhang.hibernate.entities;

import java.util.Date;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-20 22:43
 */
public class Manager {
    private String mid;
    private String name;
    private Date birthday;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "mid='" + mid + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
