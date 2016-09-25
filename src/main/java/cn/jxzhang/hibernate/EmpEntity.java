package cn.jxzhang.hibernate;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by J.X.Zhang on 2016-09-25.
 */
@Entity
@Table(name = "emp", schema = "db1", catalog = "")
public class EmpEntity {
    private int empno;
    private String ename;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private Integer sal;
    private Integer comm;

    @Id
    @Column(name = "EMPNO")
    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    @Basic
    @Column(name = "ENAME")
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    @Basic
    @Column(name = "JOB")
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Basic
    @Column(name = "MGR")
    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    @Basic
    @Column(name = "HIREDATE")
    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Basic
    @Column(name = "SAL")
    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    @Basic
    @Column(name = "COMM")
    public Integer getComm() {
        return comm;
    }

    public void setComm(Integer comm) {
        this.comm = comm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmpEntity empEntity = (EmpEntity) o;

        if (empno != empEntity.empno) return false;
        if (ename != null ? !ename.equals(empEntity.ename) : empEntity.ename != null) return false;
        if (job != null ? !job.equals(empEntity.job) : empEntity.job != null) return false;
        if (mgr != null ? !mgr.equals(empEntity.mgr) : empEntity.mgr != null) return false;
        if (hiredate != null ? !hiredate.equals(empEntity.hiredate) : empEntity.hiredate != null) return false;
        if (sal != null ? !sal.equals(empEntity.sal) : empEntity.sal != null) return false;
        if (comm != null ? !comm.equals(empEntity.comm) : empEntity.comm != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = empno;
        result = 31 * result + (ename != null ? ename.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (mgr != null ? mgr.hashCode() : 0);
        result = 31 * result + (hiredate != null ? hiredate.hashCode() : 0);
        result = 31 * result + (sal != null ? sal.hashCode() : 0);
        result = 31 * result + (comm != null ? comm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmpEntity{" +
                "empno=" + empno +
                ", ename='" + ename + '\'' +
                ", job='" + job + '\'' +
                ", mgr=" + mgr +
                ", hiredate=" + hiredate +
                ", sal=" + sal +
                ", comm=" + comm +
                '}';
    }
}
