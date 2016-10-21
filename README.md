#Hibernate
## 1 Hibernate初步
###1.1 Hibernate概述
**Hibernate**是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库。 Hibernate可以应用在任何使用JDBC的场合，既可以在Java的客户端程序使用，也可以在Servlet/JSP的Web应用中使用，最具革命意义的是，Hibernate可以在应用EJB的J2EE架构中取代CMP，完成数据持久化的重任。
- **中文名称**：
对象关系映射框架，又称ORM框架
###1.2 对象的持久化
- **狭义的理解**：“持久化”仅仅只把对象保存在数据库中
- **广义的理解**：“持久化”包括和数据库相关的各种操作
- **保存**：把对象永久保存在数据库中
- **更新**：更新数据库中对象（记录）的状态
- **删除**：从数据库中删除一个对象
- **查询**：根据特定的查询条件，吧符合查询条件的一个或多个对象从数据库加载到内存中
- **加载**：根据特定的OID，把一个对象从数据库加载到内存中
>为了在系统中能够找到所需要的对象，需要为每一个对象创建一个唯一的标识号。在关系型数据库中称之为主键，而在对象术语中则叫做对象的标识OID（Object-Identifier）
###1.3 ORM概述
**对象关系映射**（英语：Object Relational Mapping，简称ORM，或O/RM，或O/R mapping）
ORM的主要解决对象-关系的映射

| 面向对象概念   |面向关系概念     |
| :--------     | -------------:|
| 类   		    | 表            |
| 对象          | 表的行（记录）  |
| 属性          | 表的列（字段）  |

**ORM的思想**：将关系数据库中表中的记录映射成为对象，以对象的形式展现，程序员可以把对数据库的操作转化为对对象的操作。
**ORM 采用元数据来描述对象-关系映射细节**, 元数据通常采用 XML 格式, 并且存放在专门的对象-关系映射文件中。
##2 使用Maven构建Hibernate项目
###2.1 使用Maven Archetype构建一个Java项目
####2.1.1 创建Maven项目
在构建时加入如下信息
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
#### 2.1.2 在pom.xml文件中添加依赖及配置
~/pom.xml
#### 2.1.3 创建hibernate.cfg.xml文件
~/src/main/resources/hibernate.cfg.xml
#### 2.1.4 使用Intellij Idea中的 Persistent 自动生成实体类
~/src/main/java/cn/jxzhang/hibernate/Employee.java
~/src/main/java/cn/jxzhang/hibernate/Department.java
#### 2.1.5 创建测试类
~/src/test/java/cn/jxzhang/hibernate/session/SessionTest.java
#### 2.1.6 执行测试
Lifecycle --> test
### 2.2 Hibernate开发步骤
#### 2.2.1 创建持久化Java类
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
- Hibernate不要求持久化类继承任何父类或实现接口，这可以保证代码不被污染，这就是Hibernate被称为低侵入式设计的原因
####2.2.2 对象关系映射文件
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
**generator标签**：用于指定主键的生成方式。native表示使用数据库底层的生成方式来生成数据库主键
**class标签**：用于指定类和表的映射关系
**id标签**：指定持久类的OID以及表的主键
**property标签**：指定列与表之间的映射
####2.2.3 Hibernate配置文件
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
###2.3 Hibernate常用类解析
#### 2.3.1 Configuration
Configuration类负责管理Hibernate的配置信息。包括如下内容：
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
####2.3.2 SessionFactory
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
#### 2.3.3 Session接口
**Session** 是应用程序与数据库之间交互操作的一个单线程对象，是 Hibernate 运作的中心，所有持久化对象必须在 session 的管理下才可以进行持久化操作。此对象的生命周期很短。Session 对象有一个一级缓存，显式执行 flush 之前，所有的持久层操作的数据都缓存在 session 对象处。相当于 JDBC 中的 Connection.
Session提供的方法：
- 取得持久化对象的方法： get(),load()
- 持久化对象都得保存，更新和删除：save(),update(),saveOrUpdate(),delete()
- 开启事务: beginTransaction().
- 管理 Session 的方法：isOpen(),flush(), clear(), evict(), close()等
#### 2.3.4 Transaction事务
**Transcation**代表一次原子操作，它具有数据库事务的概念。**所有持久层都应该在事务管理下进行，即使是只读操作**。 
``` java
Transaction tx = session.beginTransaction();
```
常用方法:
- commit():提交相关联的session实例
- rollback():撤销事务操作
- wasCommitted():检查事务是否提交
> **Caution**:如果没有开启事务，就无法进行增删改查操作
####2.3.5 Hibernate配置文件hbm2ddl.auto属性
~/src/main/resources/hibernate.cfg.xml
``` java
<hibernate-configuration>
    <session-factory>
        <property name="hbm2ddl.auto">update</property>
        ...
        
    </session-factory>
</hibernate-configuration>
```
**hbm2ddl.auto**：该属性可帮助程序员实现正向工程, 即由 java 代码生成数据库脚本, 进而生成具体的表结构. 。取值 create | update | create-drop | validate
- **create** : 会根据 .hbm.xml  文件来生成数据表, 但是每次运行都会**删除上一次的表 ,重新生成表,** 哪怕二次没有任何改变 
- **create-drop** : 会根据 .hbm.xml 文件生成表,但是**SessionFactory一关闭, 表就自动删除** 
- **update**(**最常用的属性值**) : 也会根据 .hbm.xml 文件生成表, 但若 .hbm.xml  文件和数据库中对应的数据表的表结构不同, Hiberante  将更新数据表结构，但不会删除已有的行和列 
- **validate** : 会和数据库中的表进行比较, 若 .hbm.xml 文件中的列在**数据表中不存在**，则**抛出异常**
##3 通过Session操作对象
###3.1 Session概述
- Session 接口是 Hibernate 向应用程序提供的操纵数据库的最主要的接口, 它提供了基本的保存, 更新, 删除和加载 Java 对象的方法.
- Session 具有一个缓存, 位于缓存中的对象称为持久化对象, 它和数据库中的相关记录对应. Session 能够在某些时间点, 按照缓存中对象的变化来执行相关的 SQL 语句, 来同步更新数据库, 这一过程被称为刷新缓存(flush)
- 站在持久化的角度, Hibernate 把对象分为 4 种状态: **持久化状态**, **临时状态**, **游离状态**, **删除状态**. Session 的特定方法能使对象从一个状态转换到另一个状态. 
###3.2 Session缓存（Hibernate一级缓存）
- 在 Session 接口的实现中包含一系列的 Java 集合, 这些 Java 集合构成了 Session 缓存. 只要 Session 实例**没有结束生命周期**, 且**没有清理缓存**，则存放在它缓存中的对象也不会结束生命周期
- Session 缓存可减少 Hibernate 应用程序访问数据库的频率。

###3.3 操作session缓存
Hibernate提供个方法操作session缓存：
- flush     ：使数据表中的记录和SESSION缓存中的对象的状态保持一致。为了保持一致，则可能会发送对应的SQL语句
- refresh   : 会强制发送SELECT语句，以使Session缓存中的数据与数据库中的数据一致
- clear     : 清空session缓存
####3.3.1 flush方法
**flush**：Session按照缓存中对象的属性变化来同步更新数据库
- **默认情况下Session在以下时间点刷新缓存**：
	- 显式调用 Session 的 flush() 方法
	- 当应用程序调用 Transaction 的 commit（）方法的时, 该方法先 flush ，然后在向数据库提交事务
	- 当应用程序执行一些查询(HQL, Criteria)操作时，如果缓存中持久化对象的属性已经发生了变化，会先 flush 缓存，以保证查询结果能够反映持久化对象的最新状态
- **flush 缓存的例外情况**: 如果对象使用 native 生成器生成 OID, 那么当调用 Session 的 save() 方法保存对象时, 会立即执行向数据库插入该实体的 insert 语句.
- **commit() 和 flush() 方法的区别**：flush 执行一系列 sql 语句，但不提交事务；commit 方法先调用flush()方法，然后提交事务. 意味着提交事务意味着对数据库操作永久保存下来。
####3.3.2 refresh方法
**refresh** : 强制发送SELECT语句，以使Session缓存中的数据与数据库中的数据一致
MySQL数据库可能会出现使用refresh方法，但是对象的状态不是最新（MySQL数据库默认事务隔离级别为可重复读：REPEATABLE READ，详见3.6），需要手动指定MySQL的事务隔离级别。
####3.3.3 clear方法
**clear** :清空session缓存
###3.4 Hibernate主键生成策略
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
###3.5 设定刷新缓存的时间点
- 若希望改变flush的默认时间点，可以通过Session的setFlushMode()方法显示指定flush的时间点：

|清理缓存的模式|查询方法|Transcation的commit()方法|Session的flush()方法|
|:---|:---|:---|:---|
|FlushMode.AUTO(Default)|清理|清理|清理|
|FlushMode.COMMIT|不清理|清理|清理|
|FlushMode.NEVER|不清理|不清理|清理|

###3.6 数据库的隔离级别概述
- 对于同时运行的多个事务, 当这些事务访问数据库中相同的数据时, 如果没有采取必要的隔离机制, 就会导致各种并发问题:
	- 脏读: 对于两个事物 T1, T2, T1 读取了已经被 T2 更新但还没有被提交的字段. 之后, 若 T2 回滚, T1读取的内容就是临时且无效的.
	- 不可重复读: 对于两个事物 T1, T2, T1 读取了一个字段, 然后 T2 更新了该字段. 之后, T1再次读取同一个字段, 值就不同了.
	- 幻读: 对于两个事物 T1, T2, T1 从一个表中读取了一个字段, 然后 T2 在该表中插入了一些新的行. 之后, 如果 T1 再次读取同一个表, 就会多出几行.
- 数据库事务的隔离性: 数据库系统必须具有隔离并发运行各个事务的能力, 使它们不会相互影响, 避免各种并发问题. 
- 一个事务与其他事务隔离的程度称为隔离级别. 数据库规定了多种事务隔离级别, 不同隔离级别对应不同的干扰程度, 隔离级别越高, 数据一致性就越好, 但并发性越弱
####3.6.1 数据库的隔离级别
数据库提供的四种事务隔离级别：

|Modifier and Type|Field and Description|
|:-----|:-----|
|TRANSACTION_NONE|A constant indicating that transactions are not supported.
|TRANSACTION_READ_COMMITTED|A constant indicating that dirty reads are prevented; non-repeatable reads and phantom reads can occur.
|TRANSACTION_READ_UNCOMMITTED|A constant indicating that dirty reads, non-repeatable reads and phantom reads can occur.
|TRANSACTION_REPEATABLE_READ|A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur.
|TRANSACTION_SERIALIZABLE|A constant indicating that dirty reads, non-repeatable reads and phantom reads are prevented.

Oracle支持的两种事务隔离级别：

`TRANSACTION_READ_COMMITTED(Default)`
`TRANSACTION_SERIALIZABLE`

MySQL支持四种事务隔离级别：

`TRANSACTION_READ_COMMITTED`
`TRANSACTION_READ_UNCOMMITTED`
`TRANSACTION_REPEATABLE_READ(Default)`
`TRANSACTION_SERIALIZABLE`
####3.6.2 在MySQL中设置隔离级别
- 每启动一个 mysql 程序, 就会获得一个单独的数据库连接. 每个数据库连接都有一个全局变量 @@tx_isolation, 表示当前的事务隔离级别. MySQL 默认的隔离级别为 Repeatable Read
- 查看当前的隔离级别: 
`SELECT @@tx_isolation;`
- 设置当前 mySQL 连接的隔离级别:  
`set transaction isolation level read committed;`
- 设置数据库系统的全局的隔离级别:
` set global transaction isolation level read committed;`
####3.6.3 在Hibernate中设置隔离级别
JDBC 数据库连接使用数据库系统默认的隔离级别. 在 Hibernate 的配置文件中可以显式的设置隔离级别. 每一个隔离级别都对应一个整数:

- 1 - READ UNCOMMITED

- 2 - READ COMMITED
 
- 4 - REPEATABLE READ

- 8 - SERIALIZEABLE

Hibernate 通过为 Hibernate 映射文件指定 hibernate.connection.isolation 属性来设置事务的隔离级别
``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
    <!-- 指定数据库事务的隔离级别为：READ COMMITED(MySQL需要手动指定为该隔离级别，Oracle默认为该隔离级别) -->
    <property name="connection.isolation">2</property>
    ...
    </session-factory>
</hibernate-configuration>
```
##4 Hibernate持久化对象状态
###4.1 Entity states
Hibernate 官方文档：[Chapter 3. Persistence Contexts](http://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html_single/#d5e883)


####4.1.1 临时对象(New or Transient):
- 在使用代理主键的情况下, OID 通常为 null
- 不处于 Session 的缓存中
- 在数据库中没有对应的记录

>**new**, or **transient**(['trænzɪənt] adj. 短暂的；路过的) - the entity has just been instantiated([ɪn'stænʃɪeɪt] v. 实例化（instantiate的过去分词）；具现化，实体化) and is not associated with a persistence context. It has no persistent representation in the database and no identifier value has been assigned.

####4.1.2 持久化对象(Managed or Persistent)：

- OID 不为 null(has an associated identifier)
- 位于 Session 缓存中
- 若在数据库中已经有和其对应的记录(associated with a persistence context), 持久化对象和数据库中的相关记录对应
- Session 在 flush 缓存时, 会根据持久化对象的属性变化, 来同步更新数据库
- 在同一个 Session 实例的缓存中, 数据库表中的每条记录只对应唯一的持久化对象

>**managed**, or **persistent**([pə'sɪst(ə)nt] adj. 固执的，坚持的；持久稳固的) - the entity has an **associated identifier** and is associated with a persistence context.

####4.1.3 游离对象(Detached)：
- OID 不为 null
- 不再处于 Session 缓存中
- 一般情况需下, 游离对象是由持久化对象转变过来的, 因此在数据库中可能还存在与它对应的记录

>**detached**([dɪ'tætʃt] adj. 分离的，分开的；超然的) - the entity has an associated identifier, but is no longer associated with a persistence context (usually because the persistence context was closed or the instance was evicted(evict [ɪ'vɪkt] vt. 驱逐；逐出) from the context)

####4.1.4 删除对象(Removed):
- 在数据库中没有和其 OID 对应的记录
- 不再处于 Session 缓存中
- 一般情况下, 应用程序不该再使用被删除的对象

>**removed** - the entity has an associated identifier and is associated with a persistence context, however it is scheduled for removal from the database.

###4.2 Making entities persistent
将一个对象从临时状态转换为持久化状态：
session.save();
session.persist();

>Once you've created a new entity instance (using the standard new operator) it is in new state. You can make it persistent by associating it to either a _org.hibernate.Session_ or _javax.persistence.EntityManager_

``` java
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
```

persist方法与save方法的区别：
- 在persist方法执行之前，若对象已经有id了，则不会执行INSERT方法，相反会抛出一个异常PersistentObjectException

###4.3 session的get与load方法
从数据库中加载数据:
session.load();
session.get();

load方法与get方法的区别：
- 1. 执行get方法会立即加载对象，而执行load方法，若不使用该对象，则不会立即执行查询操作，而是返回一个代理对象
    - load是延迟检索
    - get是立即检索
- 2. 若数据库中没有对应的记录，且SESSION也没有被关闭，同时需要使用对象时
    - get返回null
    - load直接抛出异常（但是如果没有使用该对象的任何属性（没有打印），没问题；若需要初始化会抛出异常）
- 3. load方法可能会抛出LazyInitializationException异常 ：
    - 在需要初始化代理对象之前已经关闭了SESSION（在打印EMP对象之前关闭SESSION，get方法不会抛出异常（已经加载完毕），load方法会抛出异常）。

###4.4 session的update方法
session.update()
- 若操作的是一个持久化对象，不需要显式调用update方法
- 若操作的是一个若操作的是一个游离对象，需要使用update方法将对象从游离状态转换为持久化状态        
- 关闭并重新打开session将会清空session缓存，同样会使session从持久化状态转换为游离状态
- 无论更新的游离对象与数据表中的数据是否一致，都会发送UPDATE语句，可以通过配置文件来修改

###4.5 session的saveOrUpdate方法
session.saveOrUpdate()方法：
- 如果对象是一个游离对象，那么将会执行update(发送update语句)方法，如果是一个临时对象，将会执行save（发送insert语句）方法
- 如何判断一个对象为临时对象？
    1. java对象的OID为NULL
    2. 映射文件中的id列的unsaved-value元素指定的值与对象的OID一致，那么也认为该对象为临时对象
- 若OID不为空，但是数据表中还没有和其对应的记录，会抛出异常
- 了解：OID值为id的unsaved-value对象，也被认为是一个游离对象

###4.6 session的delete方法
session.delete()
- 执行删除操作：只要OID和数据库表中的一条记录对应，就会准备执行DELETE方法
- 若OID在数据表中没有记录的数据，则抛出异常

###4.7 session的evict()方法
session.evict()方法：从session缓存中将指定的对象移除

###4.8 使用session获取JDBC的Connection
``` java
@Test
public void testDoWork(){
    session.doWork(new Work() {
        @Override
        public void execute(Connection connection) throws SQLException {
            logger.info(connection);
        }
    });
}
```
##5 Hibernate检索
###5.1 Hibernate检索方式概述
**导航对象图检索方式**:  根据已经加载的对象导航到其他对象
**OID 检索方式**:  按照对象的 OID 来检索对象
**HQL 检索方式**: 使用面向对象的 HQL 查询语言
**QBC 检索方式**: 使用 QBC(Query By Criteria) API 来检索对象. 这种 API 封装了基于字符串形式的查询语句, 提供了更加面向对象的查询接口. 
**本地 SQL 检索方式**: 使用本地数据库的 SQL 查询语句

###5.2 HQL 检索方式
HQL(Hibernate Query Language) 是面向对象的查询语言, 它和 SQL 查询语言有些相似. 在 Hibernate 提供的各种检索方式中, HQL 是使用最广的一种检索方式. 它有如下功能:
- 在查询语句中设定各种查询条件
- 支持投影查询, 即仅检索出对象的部分属性
- 支持分页查询
- 支持连接查询
- 支持分组查询, 允许使用 HAVING 和 GROUP BY 关键字
- 提供内置聚集函数, 如 sum(), min() 和 max()
- 支持子查询
- 支持动态绑定参数
- 能够调用 用户定义的 SQL 函数或标准的 SQL 函数
####5.2.1 使用HQL进行检索
#####5.2.1.1 使用HQL进行检索的步骤
- 通过 Session 的 createQuery() 方法创建一个 Query 对象, 它包括一个 HQL 查询语句. HQL 查询语句中可以包含命名参数
- 动态绑定参数
- 调用 Query 相关方法执行查询语句. 

#####5.2.1.2 HQL与SQL的区别
- HQL 查询语句是面向对象的, Hibernate 负责解析 HQL 查询语句, 然后根据对象-关系映射文件中的映射信息, 把 HQL 查询语句翻译成相应的 SQL 语句. HQL 查询语句中的主体是域模型中的类及类的属性
- SQL 查询语句是与关系数据库绑定在一起的. SQL 查询语句中的主体是数据库表及表的字段. 

#####5.2.1.3 绑定参数
Hibernate 的参数绑定机制依赖于 JDBC API 中的 PreparedStatement 的预定义 SQL 语句功能.
HQL 的参数绑定由两种形式:
1. 按参数名字绑定: 在 HQL 查询语句中定义命名参数, 命名参数以 “:” 开头.
2. 按参数位置绑定: 在 HQL 查询语句中用 “?” 来定义参数位置
相关方法:
- setEntity(): 把参数与一个持久化类绑定
- setParameter(): 绑定任意类型的参数. 该方法的第三个参数显式指定 Hibernate 映射类型

HQL 采用 **ORDER BY** 关键字对查询结果排序

####5.2.2 在映射文件中定义命名查询语句
在映射文件中定义命名查询语句
- Hibernate 允许在映射文件中定义字符串形式的查询语句. 
- <query> 元素用于定义一个 HQL 查询语句, 它和 <class> 元素并列. 
> WARN: 查询语句需要放在CDATA区:<![CDATA[]]>
``` xml
<hibernate-mapping>
    <class name="cn.jxzhang.hibernate.entities.Employee" table="EMPLOYEE" schema="SCOTT">
        ...
    </class>
        ...
    <!-- 在配置文件中定义查询语句并在代码中执行 -->
    <query name="salaryEmps"><![CDATA[FROM Employee e WHERE e.salary > :minSal and e.salary < :maxSal]]></query>
</hibernate-mapping>
```

####5.2.3 HQL分页查询
query.setFirstResult()：设置从第几条记录开始查询
query.setMaxResults():  设置查询出来的最大记录数

####5.2.4 HQL投影查询
- 投影查询: 查询结果仅包含实体的部分属性. 通过 SELECT 关键字实现.
- Query 的 list() 方法返回的集合中包含的是数组类型的元素, 每个对象数组代表查询结果的一条记录
- 可以在持久化类中定义一个对象的构造器来包装投影查询返回的记录, 使程序代码能完全运用面向对象的语义来访问查询结果集. 
 -可以通过 DISTINCT 关键字来保证查询结果不会返回重复元素

####5.2.5  HQL报表查询
报表查询用于对数据分组和统计, 与 SQL 一样, HQL 利用 GROUP BY 关键字对数据分组, 用 HAVING 关键字对分组数据设定约束条件.
在 HQL 查询语句中可以调用以下聚集函数
- count()
- min()
- max()
- sum()
- avg()
####5.2.6 Hibernate连接查询
#####5.2.6.1 外连接&迫切外连接
######5.2.6.1.1 Hibernate中的外连接
- LEFT JOIN 关键字表示左外连接查询. 
- RIGHT JOIN 关键字表示左外连接查询. 
- list() 方法返回的集合中存放的是对象数组类型
- 根据配置文件来决定 Employee 集合的检索策略. 
- 如果希望 list() 方法返回的集合中仅包含 Department 对象, 可以在HQL 查询语句中使用 SELECT 关键字
######5.2.6.1.2 Hibernate中的迫切外连接
- LEFT JOIN FETCH 关键字表示迫切左外连接检索策略.
- RIGHT JOIN FETCH 关键字表示迫切左外连接检索策略.
- list() 方法返回的集合中存放实体对象的引用, 每个 Department 对象关联的 Employee  集合都被初始化, 存放所有关联的 Employee 的实体对象. 
- 查询结果中可能会包含重复元素, 可以通过一个 HashSet 来过滤重复元素
#####5.2.6.2 内链接&迫切内连接
######5.2.6.2.1 Hibernate中的内连接
- INNER JOIN 关键字表示内连接, 也可以省略 INNER 关键字
- list() 方法的集合中存放的每个元素对应查询结果的一条记录, 每个元素都是对象数组类型
如果希望 list() 方法的返回的集合仅包含 Department  对象, 可以在 HQL 查询语句中使用 SELECT 关键字
######5.2.6.2.1 Hibernate中的迫切内连接
- INNER JOIN FETCH 关键字表示迫切内连接, 也可以省略 INNER 关键字
- list() 方法返回的集合中存放 Department 对象的引用, 每个 Department 对象的 Employee 集合都被初始化, 存放所有关联的 Employee 对象

###5.2.2 QBC检索方式
####5.2.2.1 QBC检索方式概述
QBC 查询就是通过使用 Hibernate 提供的 Query By Criteria API 来查询对象，这种 API 封装了 SQL 语句的动态拼装，对查询提供了更加面向对象的功能接口


##6 Hibernate配置文件
###6.1 Hibernate配置文件概述
- Hibernate 配置文件主要用于配置数据库连接和 Hibernate 运行时所需的各种属性，**每个 Hibernate 配置文件对应一个 Configuration 对象**
Hibernate配置文件可以有两种格式:
    - hibernate.properties
    - hibernate.cfg.xml 
###6.2 hibernate.cfg.xml的常用属性
- JDBC 连接属性
    - connection.url：数据库URL 
    - connection.username：数据库用户名
    - connection.password：数据库用户密码 
    - connection.driver_class：数据库JDBC驱动 
    - dialect：配置数据库的方言，根据底层的数据库不同产生不同的 sql 语句，Hibernate 会针对数据库的特性在访问时进行优化
- C3P0 数据库连接池属性
    - hibernate.c3p0.max_size: 数据库连接池的最大连接数
    - hibernate.c3p0.min_size: 数据库连接池的最小连接数
    - hibernate.c3p0.timeout:   数据库连接池中连接对象在多长时间没有使用过后，就应该被销毁
    - hibernate.c3p0.max_statements:  缓存 Statement 对象的数量
    - hibernate.c3p0.idle_test_period:  表示连接池检测线程多长时间检测一次池内的所有链接对象是否超时. 连接池本身不会把自己从连接池中移除，而是专门有一个线程按照一定的时间间隔来做这件事，这个线程通过比较连接对象最后一次被使用时间和当前时间的时间差来和 timeout 做对比，进而决定是否销毁这个连接对象。 
    - hibernate.c3p0.acquire_increment: 当数据库连接池中的连接耗尽时, 同一时刻获取多少个数据库连接
- 其他
    - show_sql：是否将运行期生成的SQL输出到日志以供调试。取值 true | false 
    - format_sql：是否将 SQL 转化为格式良好的 SQL . 取值 true | false
    - hbm2ddl.auto：在启动和停止时自动地创建，更新或删除数据库模式。取值 create | update | create-drop | validate
    - hibernate.jdbc.fetch_size：实质是调用 Statement.setFetchSize() 方法设定 JDBC 的 Statement 读取数据的时候每次从数据库中取出的记录条数。例如一次查询1万条记录，对于Oracle的JDBC驱动来说，是不会 1 次性把1万条取出来的，而只会取出 fetchSize 条数，当结果集遍历完了这些记录以后，再去数据库取 fetchSize 条数据。因此大大节省了无谓的内存消耗。Fetch Size设的越大，读数据库的次数越少，速度越快；Fetch Size越小，读数据库的次数越多，速度越慢。Oracle数据库的JDBC驱动默认的Fetch Size = 10，是一个保守的设定，根据测试，当Fetch Size=50时，性能会提升1倍之多，当 fetchSize=100，性能还能继续提升20%，Fetch Size继续增大，性能提升的就不显著了。并不是所有的数据库都支持Fetch Size特性，例如MySQL就不支持
    - hibernate.jdbc.batch_size：设定对数据库进行批量删除，批量更新和批量插入的时候的批次大小，类似于设置缓冲区大小的意思。batchSize 越大，批量操作时向数据库发送sql的次数越少，速度就越快。测试结果是当Batch Size=0的时候，使用Hibernate对Oracle数据库删除1万条记录需要25秒，Batch Size = 50的时候，删除仅仅需要5秒！Oracle数据库 batchSize=30 的时候比较合适。
####6.2.1 Hibernate中C3P0数据源配置方式
1. 导入jar包
``` xml
    <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>c3p0</artifactId>
        <version>0.9.2.1</version>
    </dependency>
    
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-c3p0</artifactId>
        <version>4.3.2.Final</version>
    </dependency>
    
    <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>mchange-commons-java</artifactId>
        <version>0.2.3.4</version>
    </dependency>
```

2. 加入配置
    C3P0 数据库连接池属性
``` xml
    <!-- 配置C3P0数据库连接池 -->
    <property name="hibernate.c3p0.max_size">10</property>
    <property name="hibernate.c3p0.min_size">5</property>
    <property name="hibernate.c3p0.acquire_increment">2</property>
    <property name="hibernate.c3p0.idle_test_period">2000</property>
    <property name="hibernate.c3p0.timeout">2000</property>
    <property name="hibernate.c3p0.max_statements">10</property>

    <!-- 以下两条配置项对Oracle有效，对MySQL无效 -->
    <!-- 设置jdbc的statement读取数据时每次从数据库中取出的记录数 -->
    <property name="hibernate.jdbc.fetch_size">100</property>

    <!-- 设定数据库进行批量删除，批量更新和批量插入时的批次大小 -->
    <property name="hibernate.jdbc.batch_size">30</property>
```

##7 对象关系映射文件
###7.1 对象关系映射文件概述
POJO 类和数据库的映射文件*.hbm.xml
- POJO 类和关系数据库之间的映射可以用一个XML文档来定义。
- 通过 POJO 类的数据库映射文件，Hibernate可以理解持久化类和数据表之间的对应关系，也可以理解持久化类属性与数据库表列之间的对应关系
- 在运行时 Hibernate 将根据这个映射文件来生成各种 SQL 语句
- 映射文件的扩展名为 .hbm.xml
###7。2 映射文件说明
hibernate-mapping
- 类层次：class
    - 主键：id
    - 基本类型:property
    - 实体引用类: many-to-one  |  one-to-one
    - 集合:set | list | map | array
        - one-to-many
        - many-to-many
    - 子类:subclass | joined-subclass
    - 其它:component | any 等
- 查询语句:query（用来放置查询语句，便于对数据库查询的统一管理和优化）
每个Hibernate-mapping中可以同时定义多个类. 但更推荐为每个类都创建一个单独的映射文件
####7.2.1 <hibernate-mapping>元素属性（root元素）
hibernate-mapping 是 hibernate 映射文件的根元素
- schema: 指定所映射的数据库schema的名称。若指定该属性, 则表明会自动添加该 schema 前缀
- catalog:指定所映射的数据库catalog的名称。  
- **default-cascade**(默认为 none): 设置hibernate默认的级联风格. 若配置 Java 属性, 集合映射时没有指定 cascade 属性, 则 Hibernate 将采用此处指定的级联风格.   
- **default-access** (默认为 property): 指定 Hibernate 的默认的属性访问策略。默认值为 property, 即使用 getter, setter 方法来访问属性. 若指定 access, 则 Hibernate 会忽略 getter/setter 方法, 而通过反射访问成员变量.
- **default-lazy**(默认为 true): 设置 Hibernat morning的延迟加载策略. 该属性的默认值为 true, 即启用延迟加载策略. 若配置 Java 属性映射, 集合映射时没有指定 lazy 属性, 则 Hibernate 将采用此处指定的延迟加载策略 
- auto-import (默认为 true): 指定是否可以在查询语言中使用非全限定的类名（仅限于本映射文件中的类）。 
- _package_ (可选): 指定一个包前缀，如果在映射文档中没有指定全限定的类名， 就使用这个作为包名。 

####7.2.2 <class>元素属性（hibernate-mapping子元素）
class 元素用于指定类和表的映射
- name:指定该持久化类映射的持久化类的类名（与package配合使用）
- table:指定该持久化类映射的表名, Hibernate 默认以持久化类的类名作为表名
- dynamic-insert: 若设置为 true, 表示当保存一个对象时, 会动态生成 insert 语句, insert 语句中仅包含所有取值不为 null 的字段. 默认值为 false
- dynamic-update: 若设置为 true, 表示当更新一个对象时, 会动态生成 update 语句, update 语句中仅包含所有取值需要更新的字段. 默认值为 false
- select-before-update:设置 Hibernate 在更新某个持久化对象之前是否需要先执行一次查询. 默认值为 false
- batch-size:指定根据 OID 来抓取实例时每批抓取的实例数.
- lazy: 指定是否使用延迟加载.  
- mutable: 若设置为 true, 等价于所有的 <property> 元素的 update 属性为 false, 表示整个实例不能被更新. 默认为 true. 
- discriminator-value: 指定区分不同子类的值. 当使用 <subclass/> 元素来定义持久化类的继承关系时需要使用该属性

#####7.2.2.1 映射对象标识符
- Hibernate 使用对象标识符(OID) 来建立内存中的对象和数据库表中记录的对应关系. 对象的 OID 和数据表的主键对应. Hibernate 通过标识符生成器来为主键赋值
- Hibernate 推荐在数据表中使用代理主键, 即不具备业务含义的字段. 代理主键通常为整数类型, 因为整数类型比字符串类型要节省更多的数据库空间.
- 在对象-关系映射文件中, <id> 元素用来设置对象标识符. <generator> 子元素用来设定标识符生成器.
- Hibernate 提供了标识符生成器接口: IdentifierGenerator, 并提供了各种内置实现

####7.2.3 <id>元素属性（class元素子元素）
id元素设定持久化类的 OID 和表的主键的映射
- name: 标识持久化类 OID 的属性名  
- column: 设置标识属性所映射的数据表的列名(主键字段的名字). 
- unsaved-value:若设定了该属性, Hibernate 会通过比较持久化类的 OID 值和该属性值来区分当前持久化类的对象是否为临时对象
- type:指定 Hibernate 映射类型. Hibernate 映射类型是 Java 类型与 SQL 类型的桥梁. 如果没有为某个属性显式设定映射类型, Hibernate 会运用反射机制先识别出持久化类的特定属性的 Java 类型, 然后自动使用与之对应的默认的 Hibernate 映射类型
- Java 的基本数据类型和包装类型对应相同的 Hibernate 映射类型. 基本数据类型无法表达 null, 所以对于持久化类的 OID 推荐使用包装类型

####7.2.4 <generator>元素属性(id元素子元素)
generator：设定持久化类设定标识符生成器
- class: 指定使用的标识符生成器全限定类名或其缩写名

#####7.2.4.1 主键生成策略

|标识符生成器|查询方法|
|:---|:---|
|increment|适用代理主键。由Hibernate自动以自增的方式生成(只适用于测试范围) |
|identity|适用代理主键。由底层数据库生成标识符 |
|sequence|适用代理主键。Hibernate根据底层数据库的序列生成标识符，这要求底层数据库支持序列 |
|hilo|适用代理主键。Hibernate分局high/low算法生成标识符 |
|seqhilo|适用代理主键。使用一个高/低位算法来高校的生成long，short或者int类型的标识符 |
|native|适用代理主键。根据底层数据库对自动生成标识符的方式，自动选择identity，sequence或者hilo |
|uuid.hex|适用代理主键。hibernate采用128位的UUID算法生成标识符 |
|uuid.string|适用代理主键。UUID被编码为16字符长度的字符串 |
|assigned|自然主键。有Java应用程序负责生成标识符 |
|foreign|适用代理主键。使用另外一个相关联的对象标识符 |

#####7.2.4.1.1 主键生成策略-INCREMENT
increment 标识符生成器由 Hibernate 以递增的方式为代理主键赋值。Hibernate 会先读取 NEWS 表中的主键的最大值, 而接下来向 NEWS 表中插入记录时, 就在 max(id) 的基础上递增, 增量为 1.
- 适用范围:
    - 由于 increment 生存标识符机制不依赖于底层数据库系统, 因此它适合所有的数据库系统
    - **适用于只有单个 Hibernate 应用进程访问同一个数据库的场合, 在集群环境下不推荐使用它**
    - OID 必须为 long, int 或 short 类型, 如果把 OID 定义为 byte 类型, 在运行时会抛出异常
``` sql 
Hibernate:
    select
        max(EMPNO) 
    from
        EMP
Hibernate: 
    insert 
    into
        SCOTT.EMP
        (ENAME, EMPNO) 
    values
        (?, ?)
```

#####7.2.4.1.2 主键生成策略-IDENTITY
identity 标识符生成器由底层数据库来负责生成标识符, 它要求底层数据库把主键定义为自动增长字段类型
- 适用范围:
    - 由于 identity 生成标识符的机制依赖于底层数据库系统, 因此, **要求底层数据库系统必须支持自动增长字段类型**. 支持自动增长字段类型的数据库包括: DB2, Mysql, MSSQLServer, Sybase 等
    - OID 必须为 long, int 或 short 类型, 如果把 OID 定义为 byte 类型, 在运行时会抛出异常
由于Oracle不支持主键自增长，所以使用该种方式指定主键生成策略会抛出异常：SQLIntegrityConstraintViolationException

#####7.2.4.1.3 主键生成策略-SEQUENCE
sequence  标识符生成器利用底层数据库提供的序列来生成标识符. **Hibernate 在持久化一个 News 对象时, 先从底层数据库的 news_seq 序列中获得一个唯一的标识号, 再把它作为主键值**
- 适用范围:
    - 由于 sequence 生成标识符的机制依赖于底层数据库系统的序列, 因此, 要求底层数据库系统必须支持序列. 支持序列的数据库包括: DB2, Oracle 等
    - OID 必须为 long, int 或 short 类型, 如果把 OID 定义为 byte 类型, 在运行时会抛出异常
``` sql
Hibernate: 
    select
        hibernate_sequence.nextval 
    from
        dual
Hibernate: 
    insert 
    into
        SCOTT.EMP
        (ENAME, EMPNO) 
    values
        (?, ?)
```
#####7.2.4.1.4 主键生成策略-HILO
hilo 标识符生成器由 Hibernate 按照一种 high/low 算法*生成标识符, 它从数据库的特定表的字段中获取 high 值.
Hibernate 在持久化一个 News 对象时, 由 Hibernate 负责生成主键值. hilo 标识符生成器在生成标识符时, 需要读取并修改 HI_TABLE 表中的 NEXT_VALUE 值.
- 适用范围:
    - 由于 hilo 生存标识符机制不依赖于底层数据库系统, 因此它适合所有的数据库系统
    - OID 必须为 long, int 或 short 类型, 如果把 OID 定义为 byte 类型, 在运行时会抛出异常
``` sql
Hibernate: 
    select
        next_hi 
    from
        hibernate_unique_key for update
            
Hibernate: 
    update
        hibernate_unique_key 
    set
        next_hi = ? 
    where
        next_hi = ?
Hibernate: 
    insert 
    into
        SCOTT.EMP
        (ENAME, EMPNO) 
    values
        (?, ?)
```
#####7.2.4.1.5 主键生成策略-NATIVE
native 标识符生成器依据底层数据库对自动生成标识符的支持能力, 来选择使用 identity, sequence 或 hilo 标识符生成器. 
- 适用范围:
    - 由于 native 能根据底层数据库系统的类型, 自动选择合适的标识符生成器, 因此很适合于跨数据库平台开发
    - OID 必须为 long, int 或 short 类型, 如果把 OID 定义为 byte 类型, 在运行时会抛出异常
``` sql
Hibernate: 
    select
        hibernate_sequence.nextval 
    from
        dual
Hibernate: 
    insert 
    into
        SCOTT.EMP
        (ENAME, EMPNO) 
    values
        (?, ?)
```
#####7.2.4.1.6 主键生成策略-UUID
The UUID contains: IP address, startup time of the JVM that is accurate to a quarter second, system time and a counter value that is unique within the JVM. It is not possible to obtain a MAC address or memory address from Java code, so this is the best option without using JNI.


####7.2.5 <property>元素属性(class元素子元素)
property 元素用于指定类的属性和表的字段的映射
- **name**:指定该持久化类的属性的名字
- **column**:指定与类的属性映射的表的字段名. 如果没有设置该属性, Hibernate 将直接使用类的属性名作为字段名. 
- **type**:指定 Hibernate 映射类型. Hibernate 映射类型是 Java 类型与 SQL 类型的桥梁. 如果没有为某个属性显式设定映射类型, Hibernate 会运用反射机制先识别出持久化类的特定属性的 Java 类型, 然后自动使用与之对应的默认的 Hibernate 映射类型.
- **not-null**:若该属性值为 true, 表明不允许为 null, 默认为 false
- **access**:指定 Hibernate 的默认的属性访问策略。默认值为 property, 即使用 getter, setter 方法来访问属性. 若指定 field, 则 Hibernate 会忽略 getter/setter 方法, 而通过反射访问成员变量
- **unique**: 设置是否为该属性所映射的数据列添加唯一约束. 
- **index**: 指定一个字符串的索引名称. 当系统需要 Hibernate 自动建表时, 用于为该属性所映射的数据列创建索引, 从而加快该数据列的查询.
- **length**: 指定该属性所映射数据列的字段的长度
- **scale**: 指定该属性所映射数据列的小数位数, 对 double, float, decimal 等类型的数据列有效.
- **update**: 指定该属性是否可以被修改，默认为true，意为可以修改
- **formula**：设置一个 SQL 表达式, Hibernate 将根据它来计算出派生属性的值. 
    - formula=“(sql)” 的英文括号不能少
    - Sql 表达式中的列名和表名都应该和数据库对应, 而不是和持久化对象的属性对应
    - 如果需要在 formula 属性中使用参数, 这直接使用 where cur.id=id 形式, 其中 id 就是参数, 和当前持久化对象的 id 属性对应的列的 id 值将作为参数传入. 
- **_派生属性_**: 并不是持久化类的所有属性都直接和表的字段匹配, 持久化类的有些属性的值必须在运行时通过计算才能得出来, 这种属性称为派生属性

> **注意**：Oracle的Concat函数只支持两个参数的链接（多参数链接需要使用||连接符），如果concat函数有三个及以上参数，将会抛出如下异常：
``` text
java.sql.SQLSyntaxErrorException: ORA-00909: invalid number of arguments
```

###7.3 映射文件说明Java类型、Hibernate类型、标准SQL类型对应关系
参见：http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#basic

###7.4 Java 时间和日期类型的 Hibernate 映射
- 在 Java 中, 代表时间和日期的类型包括: java.util.Date 和 java.util.Calendar. 此外, 在 JDBC API 中还提供了 3 个扩展了 java.util.Date 类的子类: java.sql.Date, java.sql.Time 和 java.sql.Timestamp, 这三个类分别和标准 SQL 类型中的 DATE, TIME 和 TIMESTAMP 类型对应
- 在标准 SQL 中, DATE 类型表示日期, TIME 类型表示时间, TIMESTAMP 类型表示时间戳, 同时包含日期和时间信息. 

|Hibernate type (org.hibernate.type package)|JDBC type  |Java type   |**BasicTypeRegistry key(s)**   |
|:------------|:----------|:-----------|:-----------|
|TimestampType |TIMESTAMP|java.sql.Timestamp|timestamp, java.sql.Timestamp|
|TimeType |TIME |java.sql.Time|time, java.sql.Time
|DateType|DATE|java.sql.Date|date, java.sql.Date
|CalendarType|TIMESTAMP|java.util.Calendar|calendar, java.util.Calendar
|CalendarDateType|DATE|java.util.Calendar|calendar_date
|CalendarTimeType|TIME|java.util.Calendar|calendar_time

- 如何进行映射

 1. 因为java.util.Date是java.sql.Date java.sql.Time java.sql.Timestamp的父类，所以java.util.date可以对应比好准SQL中的 DATE, TIME 和 TIMESTAMP 类型对应
 2. 所以建议大家将持久化类中的时间类型定义为java.util.Date

- 如何把 java.util.Date 映射为 DATE, TIME 和 TIMESTAMP ?

可以通过 property 的 type 属性来进行映射: 

例如:
``` xml
<property name="date" type="timestamp">
    <column name="DATE" />
</property>

<property name="date" type="data">
    <column name="DATE" />
</property>

<property name="date" type="time">
    <column name="DATE" />
</property>
```
其中 timestamp, date, time 既不是 Java 类型, 也不是标准 SQL 类型, 而是 hibernate 映射类型. 

如果没有指定type，那么就会默认使用Date类型（即对应时间又对应日期）


###7.5 Java 大对象类型的 Hiberante 映射
- 在 Java 中, java.lang.String 可用于表示长字符串(长度超过 255), 字节数组 byte[] 可用于存放图片或文件的二进制数据. 此外, 在 JDBC API 中还提供了 java.sql.Clob 和 java.sql.Blob 类型, 它们分别和标准 SQL 中的 CLOB 和 BLOB 类型对应. CLOB 表示字符串大对象(Character Large Object), BLOB表示二进制对象(Binary Large Object)
- Mysql 不支持标准 SQL 的 CLOB 类型, 在 Mysql 中, 用 TEXT, MEDIUMTEXT 及 LONGTEXT 类型来表示长度操作 255 的长文本数据
- 在持久化类中, 二进制大对象可以声明为 byte[] 或 java.sql.Blob 类型; 字符串可以声明为 java.lang.String 或 java.sql.Clob
- 实际上在 Java 应用程序中处理长度超过 255 的字符串, 使用 java.lang.String 比 java.sql.Clob 更方便

##8 对象关系映射
###8.1 Introduction
建立域模型和关系数据模型有着不同的出发点:
- 域模型: 由程序代码组成, 通过细化持久化类的的粒度可提高代码的可重用性, 简化编程
- 在没有数据冗余的情况下, 应该尽可能减少表的数目, 简化表之间的参照关系, 以便提高数据的访问速度

Hibernate 把持久化类的属性分为两种: 
- 值(value)类型: 没有 OID, 不能被单独持久化, 生命周期依赖于所属的持久化类的对象的生命周期(没有hbm.xml配置文件)
- 无法直接用 property 映射 值类型（自定义对象类型） 属性
- Hibernate 使用 <component> 元素来映射组成关系, 该元素表名 pay 属性是 Worker 类一个组成部分, 在 Hibernate 中称之为组件

参考：
``` text
<project-name>/src/main/java/cn.jxzhang.hibernate/entities/Programmer.java
<project-name>/src/main/java/cn.jxzhang.hibernate/entities/Programmer.hbm.xml
<project-name>/src/main/java/cn.jxzhang.hibernate/entities/Salary.java
```
###8.2 Unidirectional associations
###8.2.1 Many-to-one
A unidirectional many-to-one association is the most common kind of unidirectional association.

单向 n-1 关联只需从 n 的一端可以访问 1 的一端
    - 域模型: 从 Order 到 Customer 的多对一单向关联需要在Order 类中定义一个 Customer 属性, 而在 Customer 类中无需定义存放 Order 对象的集合属性
    - 关系数据模型:ORDERS 表中的 CUSTOMER_ID 参照 CUSTOMER 表的主键
- 显然无法直接用 property 映射 customer 属性
- Hibernate 使用 <many-to-one> 元素来映射多对一关联关系





















