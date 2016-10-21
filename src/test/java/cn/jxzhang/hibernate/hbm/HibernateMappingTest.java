package cn.jxzhang.hibernate.hbm;

import cn.jxzhang.hibernate.entities.Emp;
import cn.jxzhang.hibernate.entities.Manager;
import cn.jxzhang.hibernate.entities.Programmer;
import cn.jxzhang.hibernate.entities.Salary;
import cn.jxzhang.hibernate.query.HibernateQueryLanguageTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-14 16:09
 */
public class HibernateMappingTest {
    private static Session session;
    private static Transaction transaction;
    private static ServiceRegistry serviceRegistry;
    private static SessionFactory ourSessionFactory;

    private static Logger logger;

    @BeforeClass
    public static void init() {
        Configuration configuration = new Configuration().configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = ourSessionFactory.openSession();
        transaction = session.beginTransaction();
        logger = LogManager.getLogger(HibernateMappingTest.class);
    }

    @AfterClass
    public static void destroy() {
        transaction.commit();
        session.close();
    }

    @Test
    public void testComponent2(){
        Manager manager = new Manager();
        manager.setName("testComponent2");
        manager.setBirthday(new Date());
        session.save(manager);
    }

    /**
     * 测试映射组成关系
     */
    @Test
    public void testComponent(){
        Programmer programmer = new Programmer();
        programmer.setAge(11);
        programmer.setHiredate(new Date());
        programmer.setName("zhangSan");
        Salary salary = new Salary(1000.00F,100000.00F);
        programmer.setSalary(salary);
        session.save(programmer);
    }

    /**
     * 测试Formula派生属性
     */
    @Test
    public void testFormula(){
        Emp emp = (Emp) session.get(Emp.class,8001L);
        logger.info(emp);
    }

    /**
     * 测试Properties元素的Update属性
     * update属性默认为true，意为可以修改，可以手动修改为false，意为不能被修改
     */
    @Test
    public void testUpdateProperty(){
        Emp emp = (Emp) session.get(Emp.class,8001L);
        logger.info(emp);
    }

    /**
     * 测试Native主键生成方式（在generator元素中指定主键生成方式为native）
     */
    @Test
    public void testNativeGenerator(){
        Emp emp = new Emp();
        emp.setEname("native");
        session.save(emp);
    }

    /**
     * 测试主键生成器：Hilo（在generator元素中指定主键生成方式为Hilo）
     */
    @Test
    public void tsetHiloGenerator(){
        Emp emp = new Emp();
        emp.setEname("hilo");
        session.save(emp);
    }

    /**
     * 测试主键生成器：Sequence（在generator元素中指定主键生成方式为Sequence）
     * 采用sequence方式生成主键时Hibernate将会发送如下SQL语句查找出一个序列
     * select
     *  hibernate_sequence.nextval
     * from
     *  dual
     * 然后再根据查询出的序列作为主键插入到数据库中
     */
    @Test
    public void testSequenceGenerator(){
        Emp emp = new Emp();
        emp.setEname("erMazi");
        session.save(emp);
    }

    /**
     * 测试主键生成器：Identity（在generator元素中指定主键生成方式为Identity）
     */
    @Test
    public void testIdentityGenerator(){
        Emp emp = new Emp();
        emp.setEname("Identity");
        session.save(emp);
    }

    /**
     * 测试主键生成器：Increment（在generator元素中指定主键生成方式为INCREMENT）
     * 采用Increment方式生成主键首先会向数据库中发送如下查询语句查询出主键的最大值
     * select
     *  max(EMPNO)
     * from
     *  EMP
     * 然后再发送Insert语句将最大值+1的主键插入到数据库中
     */
    @Test
    public void testIncrementGenerator(){
        Emp emp = new Emp();
        emp.setEname("erMazi");
        session.save(emp);
    }

    /**
     * 测试Hibernage配置文件中class元素的dynamic-insert属性（默认值为false）
     *
     * 若设置为 true, 表示当保存一个对象时, 会动态生成 insert 语句,
     *  insert 语句中仅包含所有取值不为 null 的字段.
     */
    @Test
    public void testDynamicInsert(){
        Emp emp = new Emp();
        emp.setEname("zhangSan");
        session.save(emp);
    }

    /**
     * 测试Hibernage配置文件中class元素的dynamic-update属性(默认值为false)
     *
     * dynamic-update 属性默认为false，将dynamic-update属性
     *  修改为true时，当对象属性发生改变时，只会更新改变状态的对象列
     */
    @Test
    public void tsetDynamicUpdate(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        emp.setJob("FUCKER");
    }
}
