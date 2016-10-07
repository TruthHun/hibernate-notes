package cn.jxzhang.hibernate.query;

import cn.jxzhang.hibernate.entities.Department;
import cn.jxzhang.hibernate.entities.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by J.X.Zhang on 2016-10-03.
 */
public class HibernateQueryLanguageTest {
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
        logger = LogManager.getLogger(HibernateQueryLanguageTest.class);
    }

    @AfterClass
    public static void destroy() {
        transaction.commit();
        session.close();
    }

    /**
     * HQL连接查询
     */
    @Test
    public void testLeftJoinFetch(){
        String sql = "FROM Department d, Employee e where d.departmentId = e.departmentId";
        Query query = session.createQuery(sql);
        List<Object[]> results = query.list();
        logger.info("TOTAL RESULT:" + results.size());
        for (Object[] result : results){
            logger.info(Arrays.asList(result));
        }
    }

    /**
     * HQL报表查询（分组查询）
     */
    @Test
    public void testGroupQuery(){
        String sql = "SELECT min(e.salary) as minSal, max(e.salary), round(avg(nvl(e.salary, 0)),2) FROM Employee as e GROUP BY e.departmentId having min(e.salary) > :minSal";
        Department dept = new Department();
        dept.setDepartmentId(80L);
        Query query = session.createQuery(sql);
        List<Object[]> emps = query.setLong("minSal",6500L).list();
        for (Object[] objs: emps) {
            logger.info(Arrays.asList(objs));
        }

    }

    /**
     * HQL投影查询2
     *  也可以直接使用new关键字创建对象，这样返回的集合中默认为该对象
     *  注意：对象必须具备指定的构造函数才能返回正确的结果
     */
    @Test
    public void testFieldQuery2(){
        String sql = "SELECT new Employee(e.employeeId, e.firstName, e.email) FROM Employee e WHERE e.departmentId = :deptID";
        Query query = session.createQuery(sql);
        Department dept = new Department();
        dept.setDepartmentId(80L);
        List<Employee> emps = query.setEntity("deptID", dept).list();
        for (Employee employee: emps) {
            logger.info(employee);
        }
    }

    /**
     * HQL投影查询
     *  在select语句后指定要查询的列，默认返回值为Object[] 类型
     */
    @Test
    public void testFieldQuery(){
        String sql = "SELECT e.firstName || ' ' || e.lastName, e.phoneNumber, e.departmentId FROM Employee e WHERE e.departmentId = :deptID";
        Query query = session.createQuery(sql);
        Department dept = new Department();
        dept.setDepartmentId(80L);
        List<Object[]> emps = query.setEntity("deptID", dept).list();
        for (Object[] args: emps) {
            logger.info(Arrays.asList(args));
        }
    }

    /**
     * 在配置文件中指定查询语句，使用session的getNamedQuery方法来执行查询语句
     */
    @Test
    public void testNamedQuery(){
        Query query = session.getNamedQuery("salaryEmps");
        List list = query.setLong("minSal",8000L).setLong("maxSal",12000L).list();
        logger.info(list);
    }

    /**
     * HQL 分页查询
     */
    @Test
    public void testHQLPageQuery() {
        String sql = "FROM Employee";
        Query query = session.createQuery(sql);
        int pageNo = 3;         //第几页的数据
        int pageSize = 5;       //每页有多大
        //(pageNo - 1) * pageSize() : 从第 (3 - 1) * 5 = 10 条记录开始查，查5条 即为第三页所有内容
        List<Employee> employees = query.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize).list();
        System.out.println("Total : " + employees.size());
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }


    /**
     * 使用命名参数指定查询条件
     * <p>
     * Query对象的query方法支持方法链操作（返回的是Query对象）
     */
    @Test
    public void testHQLNamedParameter() {
        String hql = "FROM Employee e where e.salary > :sal and e.email like :email ORDER BY e.salary DESC ";
        Query query = session.createQuery(hql);
        query.setLong("sal", 6000)
                .setString("email", "%B%");
        List<Employee> emps = query.list();
        logger.info("TOTAL:" + emps.size() + "---" + emps);
    }

    /**
     * HQL同样支持设置实体类作为查询条件
     */
    @Test
    public void testHQL2() {
        String hql = "FROM Employee e where e.salary > ? and e.email like ? and e.departmentId = ?";
        Query query = session.createQuery(hql);
        Department department = new Department();
        department.setDepartmentId(80L);        //该实体类可以是数据库中查询出来的结果，也可以是手动new出来的对象

        query.setLong(0, 6000).setString(1, "%A%").setEntity(2, department);
        List<Employee> emps = query.list();
        logger.info("TOTAL:" + emps.size() + "---" + emps);
    }

    /**
     * 使用HQL查询步骤：
     * 1. 使用SQL语句创建Query对象
     * 2. 绑定参数
     * 3. 执行查询
     */
    @Test
    public void testHQL() {
        String hql = "FROM Employee e where e.salary > ? and e.email like ?";
        Query query = session.createQuery(hql);
        query.setLong(0, 6000).setString(1, "%A%");
        List<Employee> emps = query.list();
        logger.info("TOTAL:" + emps.size() + "---" + emps);
    }
}
