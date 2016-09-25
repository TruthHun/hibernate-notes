#Hibernate
@(我的第一个笔记本)[Java, ORM, JDBC]
##一、Hibernate初步
###1. Hibernate概述
**Hibernate**是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库。 Hibernate可以应用在任何使用JDBC的场合，既可以在Java的客户端程序使用，也可以在Servlet/JSP的Web应用中使用，最具革命意义的是，Hibernate可以在应用EJB的J2EE架构中取代CMP，完成数据持久化的重任。
- **中文名称**：
对象关系映射框架，又称ORM框架
- **开发环境**：
IntelliJ IDEA 2016.1.2
hibernate-release-5.1.0.Final
- **Jar包**        ：
**HibernateJar包**      ：\hibernate-release-5.1.0.Final\lib\required	
**MySQL数据库驱动**：mysql-connector-java-5.1.37-bin.jar
###2. 对象的持久化
- **狭义的理解**：“持久化”仅仅只把对象保存在数据库中
- **广义的理解**：“持久化”包括和数据库相关的各种操作
- **保存**：把对象永久保存在数据库中
- **更新**：更新数据库中对象（记录）的状态
- **删除**：从数据库中删除一个对象
- **查询**：根据特定的查询条件，吧符合查询条件的一个或多个对象从数据库加载到内存中
- **加载**：根据特定的OID，把一个对象从数据库加载到内存中

>为了在系统中能够找到所需要的对象，需要为每一个对象创建一个唯一的标识号。在关系型数据库中称之为主键，而在对象术语中则叫做对象的标识OID（Object-Identifier）

###3. ORM概述
**对象关系映射**（英语：Object Relational Mapping，简称ORM，或O/RM，或O/R mapping）
ORM的主要解决对象-关系的映射
| 面向对象概念|面向关系概念|
| 面向对象概念      |面向关系概念|
| :-------- | ----------:    |
| 类   		| 表 		     |
| 对象       |  	表的行（记录） |
| 属性       |  表的列（字段）  |

**ORM的思想**：将关系数据库中表中的记录映射成为对象，以对象的形式展现，程序员可以把对数据库的操作转化为对对象的操作。
**ORM 采用元数据来描述对象-关系映射细节**, 元数据通常采用 XML 格式, 并且存放在专门的对象-关系映射文件中。
**ORM结构框架图**：
![Alt text](./1464100572864.png)
##二、使用Maven构建Hibernate项目
###1. 使用Maven Archetype构建一个Java项目
在构建时加入如下信息：
``` xml
<archetype>
      <groupId>org.apache.maven.archetypes</groupId>
      <artifactId>maven-archetype-quickstart</artifactId>
      <version>1.1</version>
      <description>An archetype which contains a sample Maven project.</description>
</archetype>
```
这样会在项目中生成如下目录结构：
```
|-- pom.xml
`-- src
    `-- main
        `-- resources
            |-- META-INF
            |   `-- maven
            |       `--archetype.xml
            `-- archetype-resources
                |-- pom.xml
                `-- src
                    |-- main
                    |   `-- java
                    |       `-- App.java
                    `-- test
                        `-- java
                            `-- AppTest.java
```
###2. 在pom.xml文件中添加依赖及配置
~/pom.xml
``` xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>cn.jxzhang.hibernate</groupId>
  <artifactId>hibernate-helloworld</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>hibernate-helloworld</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>

    <!-- hibernate-core -->
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-core</artifactId>
      <version>4.3.2.Final</version>
    </dependency>


    <!-- MySQL 驱动 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.39</version>
    </dependency>
    
    <!-- junit测试类 -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <!-- 指定要编译到target目录下的资源文件位置 -->
    <resources>
      <!-- 将src/main/java路径下的所有xml、properties文件添加到target中 -->
      <resource>
        <directory>src/main/java</directory>
        <includes>
          <include>**/*.xml</include>
          <include>**/*.properties</include>
        </includes>
        <filtering>false</filtering>
      </resource>
      <!-- 将src/main/resources路径下的所有xml、properties文件添加到target中 -->
      <resource>
        <directory>src/main/resources</directory>
        <includes>
          <include>**/*.xml</include>
          <include>**/*.properties</include>
        </includes>
        <filtering>false</filtering>
      </resource>
    </resources>

    <plugins>
      <!-- 指定Maven编译条件 否则会出现：
        Error:java: javacTask: source release 8 requires target release 1.8
       -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>

```

###3. 创建hibernate.cfg.xml文件
~/src/main/resources/hibernate.cfg.xml
``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- Database connection settings -->
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://localhost:3306</property>
        <property name="connection.username">root</property>
        <property name="connection.password">123</property>

        <!-- JDBC connection pool (use the built-in) -->
        <property name="connection.pool_size">1</property>

        <!-- SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLInnoDBDialect</property>

        <!-- Echo all executed SQL to stdout -->
        <property name="show_sql">true</property>

        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">update</property>
        
        <!-- 指定实体类位置（实体类通过注解指定映射关系），不用指定配置文件位置 -->
        <mapping class="cn.jxzhang.hibernate.EmpEntity"/>
    </session-factory>
</hibernate-configuration>
```

###4. 使用Intellij Idea中的 Persistent 自动生成实体类
~/src/main/java/cn/jxzhang/hibernate/EmpEntity.java
``` java
@Entity
@Table(name = "emp", schema = "db1", catalog = "")
public class EmpEntity {
    private int empno;
    private String ename;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private Integer sal;
    private Integer comm;

    @Id
    @Column(name = "EMPNO")
    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    @Basic
    @Column(name = "ENAME")
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    @Basic
    @Column(name = "JOB")
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Basic
    @Column(name = "MGR")
    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    @Basic
    @Column(name = "HIREDATE")
    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Basic
    @Column(name = "SAL")
    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    @Basic
    @Column(name = "COMM")
    public Integer getComm() {
        return comm;
    }

    public void setComm(Integer comm) {
        this.comm = comm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmpEntity empEntity = (EmpEntity) o;

        if (empno != empEntity.empno) return false;
        if (ename != null ? !ename.equals(empEntity.ename) : empEntity.ename != null) return false;
        if (job != null ? !job.equals(empEntity.job) : empEntity.job != null) return false;
        if (mgr != null ? !mgr.equals(empEntity.mgr) : empEntity.mgr != null) return false;
        if (hiredate != null ? !hiredate.equals(empEntity.hiredate) : empEntity.hiredate != null) return false;
        if (sal != null ? !sal.equals(empEntity.sal) : empEntity.sal != null) return false;
        if (comm != null ? !comm.equals(empEntity.comm) : empEntity.comm != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = empno;
        result = 31 * result + (ename != null ? ename.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (mgr != null ? mgr.hashCode() : 0);
        result = 31 * result + (hiredate != null ? hiredate.hashCode() : 0);
        result = 31 * result + (sal != null ? sal.hashCode() : 0);
        result = 31 * result + (comm != null ? comm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmpEntity{" +
                "empno=" + empno +
                ", ename='" + ename + '\'' +
                ", job='" + job + '\'' +
                ", mgr=" + mgr +
                ", hiredate=" + hiredate +
                ", sal=" + sal +
                ", comm=" + comm +
                '}';
    }
}
```
### 5. 创建测试类
``` java
public class Main {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
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
```

##三、Hibernate-HelloWorld
###1. HelloWorld
1. 配置hibernate_cfg.xml
>**Tips**:In most cases, Hibernate is able to properly determine which dialect to use. See Section 27.3, “Dialect resolution” for more information.
``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>

        <!-- Database connection settings -->
        <property name="connection.url">jdbc:mysql://localhost:3306/hibernate1</property>
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="connection.username">root</property>
        <property name="connection.password">123</property>

        <!-- JDBC connection pool (use the built-in) -->
        <property name="connection.pool_size">1</property>

        <!-- SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- Enable Hibernate's automatic session context management -->
        <property name="current_session_context_class">thread</property>

        <!-- Echo all executed SQL to stdout -->
        <property name="show_sql">true</property>

        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">update</property>

        <mapping class="cn.jxzhang.hibernate.domain.UserEntity"/>
        <mapping resource="cn/jxzhang/hibernate/domain/UserEntity.hbm.xml"/>

    </session-factory>
</hibernate-configuration>
```

2. 创建持久化类
cn.jxzhang.hibernate.domain.UserEntity:

``` java
package cn.jxzhang.hibernate.domain;

import javax.persistence.*;

/**
 1. Created by J.X.Zhang on 2016/5/24.
 */
@Entity
@Table(name = "user", schema = "hibernate1", catalog = "hibernate1")
public class UserEntity {

    private String username;
    private String password;
    private String uid;


    public UserEntity() {
    }


    public UserEntity(String username, String password, String uid) {
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 100)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @Column(name = "uid", nullable = false, length = 32)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
```

3. 创建对象 - 关系映射文件
cn.jxzhang.hibernate.domain.UserEntity.hbm.xml
``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>

    <class name="cn.jxzhang.hibernate.domain.UserEntity" table="user" schema="hibernate1">
        <id name="uid">
            <column name="uid" sql-type="varchar(32)" length="32"/>
        </id>
        <property name="username">
            <column name="username" sql-type="varchar(100)" length="100" not-null="true"/>
        </property>
        <property name="password">
            <column name="password" sql-type="varchar(100)" length="100" not-null="true"/>
        </property>
    </class>
</hibernate-mapping>
```

 4. 通过JavaAPI编写访问数据库的代码
cn.jxzhang.hiberate.util.HibernateUtil
``` java
package cn.jxzhang.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by J.X.Zhang on 2016/5/24.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static{
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

```
Main
``` java
import cn.jxzhang.hibernate.domain.UserEntity;
import cn.jxzhang.hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by J.X.Zhang on 2016/5/24.
 */
public class Main {


    public static void main(final String[] args) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

       session.save(new UserEntity("LiSi","123", UUID.randomUUID().toString().replace("-", "").toUpperCase()));
       session.save(new UserEntity("WangWu","123",UUID.randomUUID().toString().replace("-", "").toUpperCase()));

        session.getTransaction().commit();
        session.close();

        session = HibernateUtil.getSessionFactory().openSession();
        // now lets pull events from the database and list them
        session.beginTransaction();
        List result = session.createQuery( "from UserEntity " ).list();
        for ( UserEntity user : (List<UserEntity>) result ) {
            System.out.println(user);
        }
        session.getTransaction().commit();
        session.close();
    }
}
```
运行结果：
`
"C:\Program Files\Java\jdk1.8.0_31\bin\java" -Didea.launcher.port=7533 -Didea.launcher.bin.path=D:\Java\IntelliJ\bin -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_31\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\rt.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_31\jre\lib\ext\zipfs.jar;D:\JavaWeb\WebProject5\Hibernate6\out\production\Hibernate6;D:\Jar\hibernate_5.1.0\antlr-2.7.7.jar;D:\Jar\hibernate_5.1.0\classmate-1.3.0.jar;D:\Jar\hibernate_5.1.0\dom4j-1.6.1.jar;D:\Jar\hibernate_5.1.0\geronimo-jta_1.1_spec-1.1.1.jar;D:\Jar\hibernate_5.1.0\hibernate-commons-annotations-5.0.1.Final.jar;D:\Jar\hibernate_5.1.0\hibernate-core-5.1.0.Final.jar;D:\Jar\hibernate_5.1.0\hibernate-jpa-2.1-api-1.0.0.Final.jar;D:\Jar\hibernate_5.1.0\jandex-2.0.0.Final.jar;D:\Jar\hibernate_5.1.0\javassist-3.20.0-GA.jar;D:\Jar\hibernate_5.1.0\jboss-logging-3.3.0.Final.jar;D:\Jar\hibernate_5.1.0\mysql-connector-java-5.1.37-bin.jar;D:\Java\IntelliJ\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain Main
五月 25, 2016 10:02:03 上午 org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.1.0.Final}
五月 25, 2016 10:02:03 上午 org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
五月 25, 2016 10:02:03 上午 org.hibernate.cfg.Environment buildBytecodeProvider
INFO: HHH000021: Bytecode provider name : javassist
五月 25, 2016 10:02:04 上午 org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
五月 25, 2016 10:02:05 上午 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using Hibernate built-in connection pool (not for production use!)
五月 25, 2016 10:02:05 上午 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: using driver [com.mysql.jdbc.Driver] at URL [jdbc:mysql://localhost:3306/hibernate1]
五月 25, 2016 10:02:05 上午 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=root, password=****}
五月 25, 2016 10:02:05 上午 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
五月 25, 2016 10:02:05 上午 org.hibernate.engine.jdbc.connections.internal.PooledConnections <init>
INFO: HHH000115: Hibernate connection pool size: 1 (min=1)
五月 25, 2016 10:02:05 上午 org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQLDialect
Hibernate: 
    select
        userentity0_.uid as uid1_0_0_,
        userentity0_.username as username2_0_0_,
        userentity0_.password as password3_0_0_ 
    from
        user userentity0_ 
    where
        userentity0_.uid=?
UserEntity{username='WangWu', password='123', uid='01FF4ECAF82941CB807F146A537B0188'}
`
###2. 创建持久化Java类注意事项
- **提供一个无参的构造器**：使Hibernate可以使用Constructor.newInstence()来实例化持久化类
- **提供一个标识属性（重要）** ：通常映射为数据库表的主键字段，如果没有该属性，一些功能将不起作用，例如Session.saveOrUpdate()
- **为类的持久化类字段声明访问方法**：Hibernate对JavaBean风格的属性进行持久化
- **使用非final类**：在运行时生成代理是Hibernate的一个重要的功能，如果持久化类没有实现任何接口，Hibernate使用CGLIB生成代理，如果使用的是final类，则无法生成CGLIB代理
- **重写 equals和hashCode方法**：如果需要把持久化类的实例放在Set中（当需要进行关联映射时），则应重写这两个方法；例如：
``` java
@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        return result;
    }
```
####2.1 创建持久化Java类
- Hibernate不要求持久化类继承任何父类或实现接口，这可以保证代码不被污染，这就是Hibernate被称为低侵入式设计的原因
####2.2 对象关系映射文件
Hibernate采用XML格式的文件来指定对象和关系数据之间的映射，在运行时Hibernate将根据这个映射文件来生成各种SQL语句，映射文件的扩展名为
`*.hbm.xml`

``` xml
<hibernate-mapping>

    <class name="cn.jxzhang.hibernate.domain.UserEntity" table="user" schema="hibernate1">
        <id name="uid">
            <column name="uid" sql-type="varchar(32)" length="32"/>
            <generator class="native"/>
        </id>
        <property name="username">
            <column name="username" sql-type="varchar(100)" length="100" not-null="true"/>
        </property>
        <property name="password">
            <column name="password" sql-type="varchar(100)" length="100" not-null="true"/>
        </property>
    </class>
</hibernate-mapping>
```
**generator标签**：用于指定主键的生成方式：使用数据库底层的生成方式
**class标签**：用于指定类和表的映射关系
**id标签**：指定持久类的OID以及表的主键
**property标签**：指定列与表之间的映射
####2.3 Hibernate配置文件
``` java
<hibernate-configuration>
    <session-factory>

        <!-- Database connection settings -->
        <property name="connection.url">jdbc:mysql://localhost:3306/hibernate1</property>
        <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="connection.username">root</property>
        <property name="connection.password">123</property>

        <!-- JDBC connection pool (use the built-in) -->
        <property name="connection.pool_size">1</property>

        <!-- SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- Enable Hibernate's automatic session context management -->
        <property name="current_session_context_class">thread</property>

        <!-- Echo all executed SQL to stdout -->
        <property name="show_sql">true</property>

        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">update</property>

        <!-- Format SQL when echo -->
        <property name="format_sql">true</property>

		<!-- 指定程序需要关联的映射文件 -->
        <mapping class="cn.jxzhang.hibernate.domain.UserEntity"/>
        <mapping resource="cn/jxzhang/hibernate/domain/UserEntity.hbm.xml"/>

    </session-factory>
</hibernate-configuration>
```
####2.4 Configuration类
- Configuration类负责管理Hibernate的配置信息。包括如下内容：
	- **Hibernate运行的底层信息**：包括数据库的URL，用户名，密码，JDBC驱动类，数据库Dialect，数据库连接池等（与hibernate.cfg.xml文件对应）
	- **持久化类与数据表的映射关系**（与*.hbm.xml文件对应）

创建Configuration的两种方式：
``` java
Configuration configuration = new Configuration().configure();      //加载默认的XML文件
Configuration configuration = new Configuration();                  //加载默认的properties文件
```
同时Configuration创建时还可以添加参数：
``` java
File file = new File(“simpleit.xml”);
Configuration cfg = new Configuration().configure(file);
```
####2.5 SessionFactory类
- 针对单个数据库映射关系经过编译后的内存镜像，是线程安全的。 
- SessionFactory 对象一旦构造完毕，即被赋予特定的配置信息
- SessionFactory是生成Session的工厂
- 构造 SessionFactory 很消耗资源，一般情况下一个应用中只初始化一个 SessionFactory 对象。
- Hibernate5 新增了一个 ServiceRegistry 接口，所有基于 Hibernate 的配置或者服务都必须统一向这个 ServiceRegistry  注册后才能生效
Hibernate5 中创建 SessionFactory 的步骤（单例模式确保整个应用程序生命周期中只存在一个SessionFactory对象）：
``` Java
class HibernateUtil{
	private static final SessionFactory sessionFactory;
    static {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
```
####2.6 Session接口
**Session** 是应用程序与数据库之间交互操作的一个单线程对象，是 Hibernate 运作的中心，所有持久化对象必须在 session 的管理下才可以进行持久化操作。此对象的生命周期很短。Session 对象有一个一级缓存，显式执行 flush 之前，所有的持久层操作的数据都缓存在 session 对象处。相当于 JDBC 中的 Connection.
Session提供的方法：
- 取得持久化对象的方法： get() load()
- 持久化对象都得保存，更新和删除：save(),update(),saveOrUpdate(),delete()
- 开启事务: beginTransaction().
- 管理 Session 的方法：isOpen(),flush(), clear(), evict(), close()等
####2.7 Transaction事务
**Transcation**代表一次原子操作，它具有数据库事务的概念。**所有持久层都应该在事务管理下进行，即使是只读操作**。 
``` java
Transaction tx = session.beginTransaction();
```
常用方法:
- commit():提交相关联的session实例
- rollback():撤销事务操作
- wasCommitted():检查事务是否提交
> **Improtant**:如果没有开启事务，就无法进行增删改查操作
####2.8  Hibernate配置文件hbm2ddl.auto属性
**hbm2ddl.auto**：该属性可帮助程序员实现正向工程, 即由 java 代码生成数据库脚本, 进而生成具体的表结构. 。取值 create | update | create-drop | validate
- **create** : 会根据 .hbm.xml  文件来生成数据表, 但是每次运行都会**删除上一次的表 ,重新生成表,** 哪怕二次没有任何改变 
- **create-drop** : 会根据 .hbm.xml 文件生成表,但是**SessionFactory一关闭, 表就自动删除** 
- **update** : **最常用的属性值**，也会根据 .hbm.xml 文件生成表, 但若 .hbm.xml  文件和数据库中对应的数据表的表结构不同, Hiberante  将更新数据表结构，但不会删除已有的行和列 
- **validate** : 会和数据库中的表进行比较, 若 .hbm.xml 文件中的列在**数据表中不存在**，则**抛出异常**
##三、 通过Session操作对象
###1.  Session概述
- Session 接口是 Hibernate 向应用程序提供的操纵数据库的最主要的接口, 它提供了基本的保存, 更新, 删除和加载 Java 对象的方法.
- Session 具有一个缓存, 位于缓存中的对象称为持久化对象, 它和数据库中的相关记录对应. Session 能够在某些时间点, 按照缓存中对象的变化来执行相关的 SQL 语句, 来同步更新数据库, 这一过程被称为刷新缓存(flush)
- 站在持久化的角度, Hibernate 把对象分为 4 种状态: **持久化状态**, **临时状态**, **游离状态**, **删除状态**. Session 的特定方法能使对象从一个状态转换到另一个状态. 
###2. Session缓存
- 在 Session 接口的实现中包含一系列的 Java 集合, 这些 Java 集合构成了 Session 缓存. 只要 Session 实例没有结束生命周期, 且没有清理缓存，则存放在它缓存中的对象也不会结束生命周期
- Session 缓存可减少 Hibernate 应用程序访问数据库的频率。
###3. flush缓存
- **flush**：Session按照缓存中对象的属性变化来同步更新数据库
- **默认情况下Session在以下时间点刷新缓存**：
	- 显式调用 Session 的 flush() 方法
	- 当应用程序调用 Transaction 的 commit（）方法的时, 该方法先 flush ，然后在向数据库提交事务
	- 当应用程序执行一些查询(HQL, Criteria)操作时，如果缓存中持久化对象的属性已经发生了变化，会先 flush 缓存，以保证查询结果能够反映持久化对象的最新状态
- **flush 缓存的例外情况**: 如果对象使用 native 生成器生成 OID, 那么当调用 Session 的 save() 方法保存对象时, 会立即执行向数据库插入该实体的 insert 语句.
- **commit() 和 flush() 方法的区别**：flush 执行一系列 sql 语句，但不提交事务；commit 方法先调用flush() 方法，然后提交事务. 意味着提交事务意味着对数据库操作永久保存下来。
###4. Hibernate主键生成策略
|标识符生成器		 |描述	|
|:---------------|:-------|
|increment |适用于代理主键：由hibernate自动以递增的方式生成|
|identity|适用于代理主键：由底层数据库生成标识符|
|sequence|适用于代理主键：Hibernate根据底层数据库的序列生成标识符，这要求底层数据库支持序列|
|hilo   |适用于代理主键：hibernate分局high/low算法生成标识符|
|seqhilo|适用于代理主键：使用一个高/低位算法来高效的生成long，short或者int类型标识符|
|native|适用于代理主键：根据底层数据库自动生成标识符的方式，自动选择identity、sequence或者hilo|
|uuid.hex|适用于代理主键：采用128位UUID算法生成标识符|
|uuid.string|适用于代理主键：UUID被编码为16字符长字符串|
|assigned|适用于自然主键：由Java应用程序负责生成标识符内容|
|foreign|适用于代理主键：使用另外一个相关联的对象的标识符|
###5. 设定刷新缓存的时间点
- 若希望改变flush的默认时间点，可以通过Session的setFlushMode()方法显示指定flush的时间点：
|清理缓存的模式|查询方法|Transcation的commit()方法|Session的flush()方法|
|:---|:---|:---|:---|
|FlushMode.AUTO(Default)|清理|清理|清理|
|FlushMode.COMMIT|不清理|清理|清理|
|FlushMode.NEVER|不清理|不清理|清理|

###6. 数据库的隔离级别
- 对于同时运行的多个事务, 当这些事务访问数据库中相同的数据时, 如果没有采取必要的隔离机制, 就会导致各种并发问题:
	- 脏读: 对于两个事物 T1, T2, T1 读取了已经被 T2 更新但还没有被提交的字段. 之后, 若 T2 回滚, T1读取的内容就是临时且无效的.
	- 不可重复读: 对于两个事物 T1, T2, T1 读取了一个字段, 然后 T2 更新了该字段. 之后, T1再次读取同一个字段, 值就不同了.
	- 幻读: 对于两个事物 T1, T2, T1 从一个表中读取了一个字段, 然后 T2 在该表中插入了一些新的行. 之后, 如果 T1 再次读取同一个表, 就会多出几行.
- 数据库事务的隔离性: 数据库系统必须具有隔离并发运行各个事务的能力, 使它们不会相互影响, 避免各种并发问题. 
- 一个事务与其他事务隔离的程度称为隔离级别. 数据库规定了多种事务隔离级别, 不同隔离级别对应不同的干扰程度, 隔离级别越高, 数据一致性就越好, 但并发性越弱
####6.1 数据库的隔离级别
数据库提供的四种事务隔离级别：
|Modifier and Type|Field and Description|
|:-----|:-----|
|TRANSACTION_NONE|A constant indicating that transactions are not supported.
|TRANSACTION_READ_COMMITTED|A constant indicating that dirty reads are prevented; non-repeatable reads and phantom reads can occur.
|TRANSACTION_READ_UNCOMMITTED|A constant indicating that dirty reads, non-repeatable reads and phantom reads can occur.
|TRANSACTION_REPEATABLE_READ|A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur.
|TRANSACTION_SERIALIZABLE|A constant indicating that dirty reads, non-repeatable reads and phantom reads are prevented.
Oracle支持的两种事务隔离级别：
`TRANSACTION_READ_COMMITTED(Default)
TRANSACTION_SERIALIZABLE`
MySQL支持四种事务隔离级别：
`TRANSACTION_READ_COMMITTED
TRANSACTION_READ_UNCOMMITTED
TRANSACTION_REPEATABLE_READ(Default)
TRANSACTION_SERIALIZABLE`
####6.2 在MySQL中设计隔离级别
- 每启动一个 mysql 程序, 就会获得一个单独的数据库连接. 每个数据库连接都有一个全局变量 @@tx_isolation, 表示当前的事务隔离级别. MySQL 默认的隔离级别为 Repeatable Read
- 查看当前的隔离级别: 
`SELECT @@tx_isolation;`
- 设置当前 mySQL 连接的隔离级别:  
`set transaction isolation level read committed;`
设置数据库系统的全局的隔离级别:
` set global transaction isolation level read committed;`
####6.3 在Hibernate中设置隔离级别
- JDBC 数据库连接使用数据库系统默认的隔离级别. 在 Hibernate 的配置文件中可以显式的设置隔离级别. 每一个隔离级别都对应一个整数:

 1 - READ UNCOMMITED 
 2 - READ COMMITED
 4 - REPEATABLE READ
 8 - SERIALIZEABLE

- Hibernate 通过为 Hibernate 映射文件指定 hibernate.connection.isolation 属性来设置事务的隔离级别



