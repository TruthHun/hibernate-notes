package cn.jxzhang.hibernate.query;

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
 * @author J.X.Zhang
 * @version 1.0
 * @date 2016-10-03 23:23
 * @Description
 */
public class NatvieQueryTest {
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
        logger = LogManager.getLogger(NatvieQueryTest.class);
    }

    @AfterClass
    public static void destroy() {
        transaction.commit();
        session.close();
    }

    /**
     * 使用原生SQL语句查询
     */
    @Test
    public void testNativeQuery(){
        String sql = "select " +
                "        employee0_.EMPLOYEE_ID as EMPLOYEE_ID1_2_0_," +
                "        employee0_.FIRST_NAME as FIRST_NAME2_2_0_," +
                "        employee0_.LAST_NAME as LAST_NAME3_2_0_," +
                "        employee0_.EMAIL as EMAIL4_2_0_,\n" +
                "        employee0_.PHONE_NUMBER as PHONE_NUMBER5_2_0_," +
                "        employee0_.HIRE_DATE as HIRE_DATE6_2_0_," +
                "        employee0_.JOB_ID as JOB_ID7_2_0_," +
                "        employee0_.SALARY as SALARY8_2_0_," +
                "        employee0_.COMMISSION_PCT as COMMISSION_PCT9_2_0_," +
                "        employee0_.MANAGER_ID as MANAGER_ID10_2_0_," +
                "        employee0_.DEPARTMENT_ID as DEPARTMENT_ID11_2_0_ " +
                "    from" +
                "        SCOTT.EMPLOYEE employee0_ ";
        Query query = session.createSQLQuery(sql);
        List<Object[]> results = query.list();
        for (Object[] result : results){
            logger.info(Arrays.asList(result));
        }
    }

    public static void main(String[] args) {
        System.out.println("zhangSan");
        
    }
}
