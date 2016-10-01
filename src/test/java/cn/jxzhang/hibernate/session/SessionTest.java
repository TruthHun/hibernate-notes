package cn.jxzhang.hibernate.session;

import cn.jxzhang.hibernate.entities.Department;
import cn.jxzhang.hibernate.entities.Emp;
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

import java.util.Date;

/**
 * Created by J.X.Zhang on 2016-10-01.
 */
public class SessionTest {
    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = ourSessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy() {
        transaction.commit();
        session.close();
    }

    /**
     * session.clear()  清空缓存
     */
    @Test
    public void testClear() {
        Emp emp1 = (Emp) session.get(Emp.class, 8000L);
        session.clear();        //正常情况下第二次查询重复的值会直接从数据库缓存中取值，使用clear方法清空缓存
        Emp emp2 = (Emp) session.get(Emp.class, 8000L);      //此时会再次发送select语句
    }

    /**
     * session.refresh()：会强制发送SELECT语句，以使Session缓存中的数据与数据库中的数据一致
     */
    @Test
    public void testRefresh() {
        Emp emp = (Emp) session.get(Emp.class, 8000L);
        System.out.println(emp);
        //可以在此处设置断点，修改数据库记录，验证是否去读取数据库中的最新数据
        session.refresh(emp);       //得到实体类的最新状态：发送select语句更新对象属性信息（重新发送select语句）
        System.out.println(emp);

    }

    /**
     * session.flush(): 使数据表中的记录和SESSION缓存中的对象的状态保持一致。为了保持一致，则可能会发送对应的SQL语句：
     * 1. 调用Transaction的commit()方法中，先调用SESSION对象的flush方法，再提交事务
     * 2. 显式调用session.flush()方法可能会发送SQL语句(此时数据库不会变化，因为没有提交事务)，但是不会提交事务
     * 3. 注意：
     * 1) 若在未提交事务或者显示的调用session.flush方法之前也有可能会进行flush操作：
     * a. 执行HQL或者QBC查询，会先进行flush操作，以保证数据表中的数据是最新的
     * b. 若记录的ID是由底层数据库使用递增的方式生成的(配置文件中主键生成方式指定为native)，则在调用save方法
     * 后就会调用flush立即发送INSERT语句（得到对象的OID）。因为save方法后，必须保证ID是存在的！
     * 如果主键生成方式指定为seqhilo（由Hibernate生成主键），则不会立即发送insert语句（不会调用flush），
     * 而是先发送查询语句（从序列中获取主键，此时主键已经存在），并在commit方法之前调用flush()方法，发送insert语句
     */
    @Test
    public void testSessionFlush2() {
        Emp emp = new Emp("xiaoErHei", "FUCKER", null, new Date(), 10000L, null, 10L);
        session.save(emp);      //由数据库自动生成主键：主键生成方式为seqhilo
    }

    /**
     * 测试session.flush();
     * 执行transaction.commit();之前会调用的session.flush()方法
     */
    @Test
    public void testSessionFlush() {
        Emp emp = (Emp) session.get(Emp.class, 8000L);
        System.out.println(emp);
        emp.setEname("liSi");       //此处改变了对象的属性
        session.flush();            //显式调用flush()方法（未提交事务，只发送了update语句）
    }

    /**
     * 测试SESSION的一级缓存
     * 第一次从数据库中查询出的信息会保存在session缓存中，如果第二次发出同样的查询请求，session将会从缓存中取出数据，整个过程只向数据库发送一次请求
     */
    @Test
    public void testSessionCatch() {
        Employee employee1 = (Employee) session.get(Employee.class, 198L);      //数据将会保存在session缓存中
        System.out.println(employee1);
        Employee employee2 = (Employee) session.get(Employee.class, 198L);      //直接从缓存中取出数据（没有查询数据库）
        System.out.println(employee2);
    }

    /**
     * 初始化测试
     */
    @Test
    public void testInit() {
        Employee employee = (Employee) session.get(Employee.class, 198L);
        System.out.println(employee);
        Department department = (Department) session.get(Department.class, 10L);
        System.out.println(department);
    }
}
