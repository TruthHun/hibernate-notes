package cn.jxzhang.hibernate;

import cn.jxzhang.hibernate.entities.Department;
import cn.jxzhang.hibernate.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by J.X.Zhang on 2016-10-01.
 */
public class SessionTest {
    private SessionFactory ourSessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        Configuration configuration = new Configuration().configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = ourSessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy() {
        transaction.commit();
        session.close();
    }

    @Test
    public void testInit(){
        Employee employee = (Employee) session.get(Employee.class,198l);
        System.out.println(employee);
        Department department = (Department) session.get(Department.class,10l);
        System.out.println(department);
    }

}
