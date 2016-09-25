package cn.jxzhang.test;

import cn.jxzhang.hibernate.EmpEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by J.X.Zhang on 2016-09-25.
 */
public class Main {

    /** 1. 创建一个SessionFactory对象 */
    private static final SessionFactory sessionFactory;

    /** 2. 创建一个ServiceRegistry对象:hibernate的任何服务和配置都需要在该对象中注册后才能有效 */
    private static final ServiceRegistry serviceRegistry;

    /** 3. 加载配置文件（默认加载资源路径下的hibernate.cfg.xml文件） */
    private static final Configuration configuration;

    static {
        try {
            configuration = new Configuration().configure();
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * 使用SessionFactory的openSession方法获取连接
     *
     * @return  Session对象
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public void getEmpById(int id){
        final Session session = getSession();
        try {
            EmpEntity emp = (EmpEntity) session.get(EmpEntity.class,id);
            System.out.println(emp);
        } finally {
            session.close();
        }
    }
}
