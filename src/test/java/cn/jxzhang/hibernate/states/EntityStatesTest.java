package cn.jxzhang.hibernate.states;

import cn.jxzhang.hibernate.entities.Emp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by J.X.Zhang on 2016-10-01.
 */
@Ignore
public class EntityStatesTest {
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
     * 获取JDBC的Connection对象
     */
    @Test
    public void testDoWork(){
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                logger.info(connection);
            }
        });
    }

    /**
     * session.evict()方法：从session缓存中将指定的对象移除
     */
    @Test
    public void testEvict(){
        Emp emp1 = (Emp) session.get(Emp.class,80L);
        Emp emp2 = (Emp) session.get(Emp.class,50L);

        emp1.setJob("FUCKER");
        emp2.setJob("MANAGER");

        session.evict(emp1);        //将emp移除，将不会发送update语句
    }

    /**
     * delete()
     * 执行删除操作：只要OID和数据库表中的一条记录对应，就会准备执行DELETE方法
     * 若OID在数据表中没有记录的数据，则抛出异常
     */
    @Test
    public void testDelete(){
        Emp emp = new Emp();
        emp.setEmpno(8011L);        //这是一个游离对象（存在OID）

        Emp emp2 = (Emp) session.get(Emp.class,8000L);      //这是一个持久化对象(从数据库记录中加载的对象)

        session.delete(emp);        //数据库表中没有与之对应的记录，抛出异常
        session.delete(emp2);
    }

    /**
     * Hibernate 的 cfg.xml 配置文件中有一个 hibernate.use_identifier_rollback 属性, 其默认值为 false, 若把它设为 true, 将改变 delete() 方法的运行
     * 行为: delete() 方法会把持久化对象或游离对象的 OID 设置为 null, 使它们变为临时对象
     */
    @Test
    public void testDelete2(){

        Emp emp = (Emp) session.get(Emp.class,40L);
        logger.info(emp);
        session.delete(emp);
        logger.info(emp);
    }

    /**
     * session.saveOrUpdate()方法：
     *  如果对象是一个游离对象，那么将会执行update(发送update语句)方法，如果是一个临时对象，将会执行save（发送insert语句）方法
     * 如何判断一个对象为临时对象？
     *  1. java对象的OID为NULL
     *  2. 映射文件中的id列的unsaved-value元素指定的值与对象的OID一致，那么也认为该对象为临时对象
     *
     * 1. 若OID不为空，但是数据表中还没有和其对应的记录，会抛出异常
     * 2. 了解：OID值为id的unsaved-value对象，也被认为是一个游离对象
     *
     */
    @Test
    public void testSaveOrUpdate(){
        Emp emp = new Emp();
        emp.setEname("AA");
        emp.setHiredate(new Date());
        emp.setComm(1000L);
        emp.setSal(100L);
        emp.setJob("MANAGER");
        emp.setMgr(null);

        session.saveOrUpdate(emp);      //没有指定OID，则认为其是一个临时对象，将会调用save方法发送insert语句
    }

    @Test
    public void testSaveOrUpdate2(){
        Emp emp = new Emp();
        emp.setEname("AA");
        emp.setHiredate(new Date());
        emp.setComm(1000L);
        emp.setSal(1000L);
        emp.setJob("MANAGER");
        emp.setMgr(null);

        emp.setEmpno(8000L);            //指定了OID，将被认为是一个游离对象，发送update语句
        session.saveOrUpdate(emp);
    }


    /**
     * UPDATE
     * 1. 若更新一个持久化对象，不需要显示的调用UPDATE方法，因为在调用transaction方法时，
     *      会先执行SESSION.flush()方法，将对象信息写到数据库中
     * 2. 更新一个游离的对象，需要显示调用UPDATE方法（可以将一个游离对象变为一个持久化对象）
     *
     * 需要注意的是：
     * 1. 无论要更新的游离对象跟数据表是否一致，都会发送UPDATE语句（正常情况下需要与缓存中的数据进行比对）
     *      如何使UPDATE方法不再盲目的触发UPDATE语句？
     *          在.hbm.xml 文件的class节点设置select-before-update=true  (默认为FALSE)，但是通常不需要设置该属性
     * 2. 若数据表中没有对应的记录（手动修改OIO值），但是还调用了UPDATE方法，就会抛出异常
     * 3. 当UPDATE方法关联一个游离对象时，如果在SESSION的缓存中已经存在相同的OID的持久化对象，会抛出异常，因为在SESSION缓存hzong
     *      不能有两个相同的OID对象
     */
    @Test
    public void testUpdate6(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        session.clear();
        Emp emp2 = (Emp) session.get(Emp.class,8000L);
        session.update(emp);        //session中存在两个相同ID的对象，此时调用update方法会抛出：NonUniqueObjectException异常
    }

    @Test
    public void testUpdate5(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        session.clear();
        emp.setEmpno(2854L);        //修改一个游离对象的OID，但是数据库中没有与之对应的主键值，则会抛出异常：StaleStateException
        session.update(emp);        //这里没有对对象进行任何操作，但是还是会发送update语句
    }

    /**
     * 无论更新的游离对象与数据表中的数据是否一致，都会发送UPDATE语句，可以通过配置文件来修改
     */
    @Test
    public void testUpdate4(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        session.clear();
        session.update(emp);        //这里没有对对象进行任何操作，但是还是会发送update语句
    }

    /**
     * 关闭并重新打开session将会清空session缓存，同样会使session从持久化状态转换为游离状态
     */
    @Test
    public void testUpdate3(){
        Emp emp = (Emp) session.get(Emp.class,8000L);

        transaction.commit();
        session.close();

        session = ourSessionFactory.openSession();
        transaction = session.beginTransaction();

        emp.setEname("zhaoLiu");
        session.update(emp);        //关闭并重新打开session同样会使对象从持久化状态转换为游离状态，这时候需要手动调用update方法
}

    /**
     * session.update():若操作的是一个若操作的是一个游离对象，需要使用update方法将对象从游离状态转换为持久化状态
     */
    @Test
    public void testUpdate2(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        session.clear();        //清空session缓存，emp对象将从持久化状态转换为游离状态
        emp.setEname("wangWu");
        session.update(emp);    //这时候需要显式的调用session.update方法来将游离状态的对象转换为持久化状态（发送update语句）
    }

    /**
     * session.update():若操作的是一个持久化对象，不需要显式调用update方法
     */
    @Test
    public void testUpdate(){
        Emp emp = (Emp) session.get(Emp.class,8000L);
        emp.setEname("liSi");
        session.update(emp);        //操作的是一个持久化对象，不需要显式调用该方法
    }

    /**
     * session.load()方法：从数据库中加载记录
     * get方法与load方法的区别：
     * 1. 执行get方法会立即加载对象，而执行load方法，若不使用该对象，则不会立即执行查询操作，而是返回一个代理对象
     *      load是延迟检索
     *      get是立即检索
     * 2. 若数据库中没有对应的记录，且SESSION也没有被关闭，同时需要使用对象时
     *      get返回null
     *      load直接抛出异常（但是如果没有使用该对象的任何属性（没有打印），没问题；若需要初始化会抛出异常）
     * 3. load方法可能会抛出LazyInitializationException异常 ：
     *      在需要初始化代理对象之前已经关闭了SESSION（在打印EMP对象之前关闭SESSION，get方法不会抛出异常
     *      （已经加载完毕），load方法会抛出异常）。
     */
    @Test
    public void testLoad(){
        //Emp emp = (Emp) session.load(Emp.class,10000L);      //使用load方法会抛出异常：数据库中不存在与该ID对应的记录
        Emp emp = (Emp) session.load(Emp.class,8000L);      //该方法不会立即检索数据库中的记录（不会发送select语句）
//        session.close();
        logger.info(emp);       //如果在打印之前关闭了session该方法将会抛出异常：LazyInitializationException
    }

    /**
     * session.save()方法：
     * 从数据库中获取对象
     */
    @Test
    public void testGet(){
        Emp emp = (Emp) session.get(Emp.class,10000L);      //数据库中不存在id为10000的记录，get方法会返回null
        session.close();
        logger.info(emp);           //在该方法执行之前已经关闭了session，但是不会抛出异常（数据已经加载完毕）
    }

    /**
     * session.persist()方法：跟save()方法一样，也会发送一条INSERT语句
     * persist方法与save方法的区别：
     *      在persist方法之前，若对象已经有id了，则不会执行INSERT方法，相反会抛出一个异常PersistentObjectException
     */
    @Test
    public void testPersist(){
        Emp emp = new Emp();
        emp.setEname("AA");
        emp.setHiredate(new Date());
        emp.setComm(10000L);
        emp.setSal(10000L);
        emp.setJob("MANAGER");
        emp.setMgr(null);

        //emp.setEmpno(10000L);       //执行该条语句会抛出一个异常：PersistentObjectException
        session.persist(emp);
    }

    /**
     * session.save()方法：
     * 1) 使一个临时对象变为一个持久化对象
     * 2) 为对象分配ID
     * 3) 在flush缓存时会发送一条INSERT语句
     * 4) 在save方法之前设置的ID是无效的
     * 5) 在save方法之后设置ID会抛出异常：持久化对象的ID是不能够修改的
     */
    @Test
    public void testSave(){
        Emp emp = new Emp();
        emp.setEname("wangWu");
        emp.setHiredate(new Date());
        emp.setComm(10000L);
        emp.setSal(10000L);
        emp.setJob("MANAGER");
        emp.setMgr(null);
        //emp.setEmpno(10000L);        //在save方法执行之前，为对象设置ID是无效的(该条语句无任何作用)
        session.save(emp);
        //emp.setEmpno(10000L);        //在save方法执行之后，持久化对象的ID是不能被修改的（该条语句会抛出一个异常）

    }
}
