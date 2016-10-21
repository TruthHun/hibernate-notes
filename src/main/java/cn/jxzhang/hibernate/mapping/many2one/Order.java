package cn.jxzhang.hibernate.mapping.many2one;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-21 09:51
 */
public class Order {
    private String oid;
    private String oname;
    private Customer customer;

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", oname='" + oname + '\'' +
                ", customer=" + customer +
                '}';
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
