package cn.jxzhang.hibernate.mapping;

import cn.jxzhang.hibernate.hbm.HibernateMappingTest;
import cn.jxzhang.hibernate.mapping.many2one.Customer;
import cn.jxzhang.hibernate.mapping.many2one.Order;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.BatchUpdateException;

/**
 * Maven-Hibernate
 *
 * @author J.X.Zhang
 *         2016-10-21 10:30
 */
@Ignore
public class AssociationMappingsTest {
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
        logger = LogManager.getLogger(AssociationMappingsTest.class);
    }

    @AfterClass
    public static void destroy() {
        transaction.commit();
        session.close();
    }

    /**
     * 测试Delete
     */
    @Test
    public void testMany2OneDelete2(){
        Order order = (Order) session.get(Order.class,"8a8a99bf57e6bdff0157e6be36c40001");
        session.delete(order);
    }

    /**
     * 测试Delete
     *
     * 在不设定级联关系的情况下，且1这一端有n的一端在引用，该测试试图删除1的那一端，会抛出如下异常：
     * java.sql.BatchUpdateException: ORA-02292: integrity constraint (SCOTT.FK_FNAWBUB4A39G8RGMYCC00T7WM) violated
     * 违反了外键约束
     */
    @Test(expected = ConstraintViolationException.class)
    public void testMany2OneDelete(){
        Customer customer = (Customer) session.get(Customer.class, "8a8a99bf57e6bdff0157e6be36b80000");
        session.delete(customer);
    }

    /**
     * 测试Update方法
     */
    @Test
    public void testMany2OneUpdate(){
        Order order = (Order) session.get(Order.class,"8a8a99bf57e6bdff0157e6be36c40001");
        order.getCustomer().setCname("erMazi");     //在这里对其关联的属性进行操作
    }

    /**
     * 由于1的一端没有关联多的那端的属性（列），所以只会查询出指定ID的Customer对象
     */
    @Test
    public void testMany2OneGet3(){
        Customer customer = (Customer) session.get(Customer.class, "8a8a99bf57e6bdff0157e6be36b80000");
        logger.info(customer);
    }

    /**
     * 测试多对一关联关系get方法
     *
     * 默认情况下，获取Order对象时，其关联的Customer对象是一个代理对象，只有在该代理对象使用的时候才会被初始化，
     * 但是此时将session清空或者关闭，就会抛出异常
     *
     */
    @Test(expected = LazyInitializationException.class)
    public void testMany2OneGet2(){
        //在需要使用到关联对象的时候才会查询（发送两条SELECT语句）
        Order order = (Order) session.get(Order.class,"8a8a99bf57e6bdff0157e6be36c40001");
        session.clear();            //该条语句导致LazyInitializationException异常（session已经被清空）
        logger.info(order);
        //由多的一端导航到1的一端时，可能会抛出：LazyInitializationException:could not initialize proxy - no Session
    }

    /**
     * 测试多对一关联关系get方法
     */
    @Test
    public void testMany2OneGet(){
        //若查询多的一端的一个对象，则默认情况下，只查询了多的一端的对象
        //而没有查询相关联的1的那一端的对象（延迟加载）
        Order order = (Order) session.get(Order.class,"8a8a99bf57e6bdff0157e6be36c40001");
    }

    /**
     * 测试多对一关联关系get方法2（多个订单对应一个用户）
     */
    @Test
    public void testMany2OneSave2(){
        Customer customer = new Customer();
        customer.setCname("liSi");

        Order order1 = new Order();
        order1.setOname("微博");
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setOname("QQ");
        order2.setCustomer(customer);

        //执行save操作，先插入Order（多的那端），再插入Order（一的那端），Hibrenate会发送三条Insert，两条Update
        //插入多的那端还不能确定外键值（因为外键还没有插入），这样当customer插入之后，需要发送两条Update语句来添加外键值
        //推荐先插入一的一端，再插入多的一端
        session.save(order1);
        session.save(order2);
        session.save(customer);
    }

    /**
     * 测试多对一关联关系save方法（多个订单对应一个用户）
     */
    @Test
    public void testMany2OneSave(){
        Customer customer = new Customer();
        customer.setCname("zhangSan");

        Order order1 = new Order();
        order1.setOname("支付宝");
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setOname("微信");
        order2.setCustomer(customer);

        //执行save操作，先插入Customer（一的那端），再插入Order（多的那端），Hibernate会向数据库发送三条Insert语句

        session.save(customer);
        session.save(order1);
        session.save(order2);
    }
}
