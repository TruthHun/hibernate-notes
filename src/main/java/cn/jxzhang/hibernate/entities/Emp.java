package cn.jxzhang.hibernate.entities;

import java.sql.Time;
import java.util.Date;

/**
 * Created by J.X.Zhang on 2016-10-01.
 */
public class Emp {
    private long empno;
    private String ename;
    private String job;
    private Long mgr;
    private Date hiredate;
    private Long sal;
    private Long comm;
    private Long deptno;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Emp() {
    }

    public Emp(long empno, String ename, String job, Long mgr, Date hiredate, Long sal, Long comm, Long deptno) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
    }

    public Emp(String ename, String job, Long mgr, Date hiredate, Long sal, Long comm, Long deptno) {
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
    }

    public long getEmpno() {
        return empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getMgr() {
        return mgr;
    }

    public void setMgr(Long mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Long getSal() {
        return sal;
    }

    public void setSal(Long sal) {
        this.sal = sal;
    }

    public Long getComm() {
        return comm;
    }

    public void setComm(Long comm) {
        this.comm = comm;
    }

    public Long getDeptno() {
        return deptno;
    }

    public void setDeptno(Long deptno) {
        this.deptno = deptno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Emp emp = (Emp) o;

        if (empno != emp.empno) return false;
        if (ename != null ? !ename.equals(emp.ename) : emp.ename != null) return false;
        if (job != null ? !job.equals(emp.job) : emp.job != null) return false;
        if (mgr != null ? !mgr.equals(emp.mgr) : emp.mgr != null) return false;
        if (hiredate != null ? !hiredate.equals(emp.hiredate) : emp.hiredate != null) return false;
        if (sal != null ? !sal.equals(emp.sal) : emp.sal != null) return false;
        if (comm != null ? !comm.equals(emp.comm) : emp.comm != null) return false;
        if (deptno != null ? !deptno.equals(emp.deptno) : emp.deptno != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (empno ^ (empno >>> 32));
        result = 31 * result + (ename != null ? ename.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (mgr != null ? mgr.hashCode() : 0);
        result = 31 * result + (hiredate != null ? hiredate.hashCode() : 0);
        result = 31 * result + (sal != null ? sal.hashCode() : 0);
        result = 31 * result + (comm != null ? comm.hashCode() : 0);
        result = 31 * result + (deptno != null ? deptno.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "empno=" + empno +
                ", ename='" + ename + '\'' +
                ", job='" + job + '\'' +
                ", mgr=" + mgr +
                ", hiredate=" + hiredate +
                ", sal=" + sal +
                ", comm=" + comm +
                ", deptno=" + deptno +
                ", desc='" + desc + '\'' +
                '}';
    }
}
