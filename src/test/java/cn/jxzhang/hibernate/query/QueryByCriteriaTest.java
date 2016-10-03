package cn.jxzhang.hibernate.query;

import cn.jxzhang.hibernate.entities.Department;
import cn.jxzhang.hibernate.entities.Emp;
import cn.jxzhang.hibernate.entities.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.*;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author J.X.Zhang
 * @version 1.0
 */
public class QueryByCriteriaTest {
    private Session session;
    private Transaction transaction;
    private ServiceRegistry serviceRegistry;
    private SessionFactory ourSessionFactory;

    private static Logger logger;

    @Before
    public void init() {
        Configuration configuration = new Configuration().configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = ourSessionFactory.openSession();
        transaction = session.beginTransaction();
        logger = LogManager.getLogger("org.apache.log4j.xml");
    }

    @After
    public void destroy() {
        transaction.commit();
        session.close();
    }

    /**
     * 测试QBC分页查询
     */
    @Test
    public void testQBC4(){
        Criteria criteria = session.createCriteria(Employee.class);
        //1. 添加排序规则
        criteria.addOrder(Order.asc("salary"));
        criteria.addOrder(Order.desc("email"));
        //2. 添加翻页方法
        int pageSize = 5;
        int pageNo = 3;
        criteria.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    /**
     * 测试QBC查询中的统计查询
     */
    @Test
    public void testQBC3(){
        Criteria criteria = session.createCriteria(Employee.class);
        //统计查询: 使用 Projection 来表示: 可以由 Projections 的静态方法得到
        criteria.setProjection(Projections.max("salary"));
        logger.info("MAX SALARY : " + criteria.uniqueResult());
    }

    /**
     * 测试QBC查询中的条件查询表示
     */
    @Test
    public void testQBC2(){
        Criteria criteria = session.createCriteria(Employee.class);

        //1. AND: 使用 Conjunction 表示
        Conjunction conjunction = Restrictions.conjunction();
        conjunction.add(Restrictions.like("firstName", "A", MatchMode.ANYWHERE));
        conjunction.add(Restrictions.eq("departmentId", 80L));
        logger.info("conjunction : " + conjunction);

        //2. OR: 使用Disjunction表示
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.ge("salary", 6000L));
        disjunction.add(Restrictions.isNull("email"));
        logger.info("disjunction : " + disjunction);

        criteria.add(disjunction);
        criteria.add(conjunction);

        criteria.list();
    }

    /**
     * 使用Hibernate的QBC查询
     * 1. 创建一个Criteria对象：使用待查询的对象创建一个Criterion对象
     * 2. 添加查询条件：在QBC 中，查询条件使用Criterion来表示
     * 3. 执行查询
     */
    @Test
    public void testQBC(){
        Criteria criteria = session.createCriteria(Employee.class);
        criteria.add(Restrictions.eq("firstName", "Donald"));
        Employee employee = (Employee) criteria.uniqueResult();
        logger.info(employee);
    }
}
