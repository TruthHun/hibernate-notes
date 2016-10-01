package cn.jxzhang.hibernate.entities;

/**
 * Created by J.X.Zhang on 2016-10-01.
 */
public class Department {
    private long departmentId;
    private String departmentName;
    private Long managerId;
    private Long locationId;

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (departmentId != that.departmentId) return false;
        if (departmentName != null ? !departmentName.equals(that.departmentName) : that.departmentName != null)
            return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (locationId != null ? !locationId.equals(that.locationId) : that.locationId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (departmentId ^ (departmentId >>> 32));
        result = 31 * result + (departmentName != null ? departmentName.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (locationId != null ? locationId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", managerId=" + managerId +
                ", locationId=" + locationId +
                '}';
    }
}
