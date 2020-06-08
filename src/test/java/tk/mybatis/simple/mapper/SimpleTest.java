package tk.mybatis.simple.mapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.junit.Test;

import tk.mybatis.simple.model.Country;
import tk.mybatis.simple.plugin.SimpleInterceptor;

/**
 * <pre>通过一段示例代码将MyBatis中的各部分代码串联起来了，通过这段代码可以了解MyBatis各部分的作用</pre>
 * 
 * @author Administrator
 *
 */
public class SimpleTest {
	
	@Test
	public void test() throws SQLException {
	    /**
	     * 第1步：手动指定使用Log4j记录日志，项目中必须要有Log4j的依赖，否则不会生效；
	     * 默认情况下， MyBatis会按照默认的顺序去寻找日志实现类， 日志的优先级顺序为:
	     * SLF4j > Apache Commons Logging > Log4j2 > Log4j > JDK Logging > StdOut Logging > NO Logging
	     * 只要MyBatis找到了对应的依赖，就会停止继续找
	     */
	    LogFactory.useLog4JLogging();
	    
	    /**
	     * 第1步：创建Configuration，且配置 settings 中的部分属性；
	     * Configuration这个类几乎包括了Mybatis中的全部内容，记录了各项属性配置，常见的 settings 中的配置基本都可以通过这个类配置
	     */
	    final Configuration config = new Configuration();
	    config.setCacheEnabled(true);// 二级缓存
	    config.setLazyLoadingEnabled(false);// 对象中属性为对象的属性的懒加载配置
	    config.setAggressiveLazyLoading(true);
	    
	    /**
	     * 第2步：添加Interceptor，在后面的执行过程中可以看到拦截器的执行顺序。
	     * 这里的Interceptor不需要配置到mybatis-config.xml的pugings的plugin中
	     */
	    SimpleInterceptor si = new SimpleInterceptor("inteceptor 1");
	    SimpleInterceptor si2 = new SimpleInterceptor("inteceptor 2");
	    config.addInterceptor(si);
	    config.addInterceptor(si2);
	    
	    /**
	     * 第3步：创建DataSource和JDBC事务：使用MyBatis提供的最简单UnPooledDataSource创建数据源，使用JDBC事物
	     */
	    UnpooledDataSource dataSource = new UnpooledDataSource();
	    dataSource.setDriver("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?useSSL=false");
	    dataSource.setUsername("root");
	    dataSource.setPassword("123456");
	    
	    Transaction tx = new JdbcTransaction(dataSource, null, false);
	    
	    /**
	     * 第4步：创建Executor：
	     * Executor是MyBatis底层执行数据库操作的直接对象， 大多数MyBatis方便调用的方式都是对该对象的封装。通过Configuration的newExecutor方法创建的Executor会自
	     * 动根据配置的拦截器对Executor进行多层代理。通过这种代理机制使得MyBatis的扩展更方便更强大。
	     */
	    final Executor exe = config.newExecutor(tx);
	    
	    /**
	     * 第5步：创建SqlSource对象：
	     * 无论是通过xml还是注解方式配置sql语句，在MyBatis中，Sql语句都会被封装成SqlSource对象。
	     * xml中静态的Sql会对应生成StaticSqlSouce，带有if标签或${}用法的Sql会按动态Sql被处理为DynamicSqlSource；
	     * 使用Provider类注解标记的方法生成ProviderSqlSource；
	     * 所有类型的SqlSource在最终执行前都会被处理成StaticSqlSouce。
	     */
	    StaticSqlSource sqlSource = new StaticSqlSource(config, "select * from country where id = ?");
	    
	    /**
	     * 第6步：创建parameterMapping对象：
	     * 上面的sql中有一个参数id，这里需要提供ParameterMapping。
	     * 在第5步的SQL中包含一个参数id，MyBatis文档中建议在XML中不去配置parameterMap 属性，因为MyBatis 会自动根据参数去判断和生成这个配置。在底层中，这个配置是必须存在的。
	     */
	    List<ParameterMapping> pms = new ArrayList<ParameterMapping>();
	    pms.add(new ParameterMapping.Builder(config, "id", new TypeHandlerRegistry().getTypeHandler(Long.class)).build());
	    ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", Country.class , pms);
	    
	    /**
	     * 第7步：创建ResultMap对象：
	     * 这种配置方式和xml中配置resultMap元素是相同的，经常使用的resultType方式，在底层仍然是ResultMap对象，但是创建起来更容易
	     */
        ResultMap rm = new ResultMap.Builder(config, "defaultResultMap", Country.class, new ArrayList<ResultMapping>(){
	        {
	            add(new ResultMapping.Builder(config, "id", "id", Long.class).build());
	            add(new ResultMapping.Builder(config, "countryName", "countryname", String.class).build());
	            add(new ResultMapping.Builder(config, "countryCode", "countrycode", String.class).build());
	        }
	    }).build();
	    
        // 最后一个List为空，MyBatis完全参照第3个参数来映射结果，这个resultMap相当于xml中的resultType作用
        // ResultMap rm2 = new ResultMap.Builder(config, "defaultResultMap", Country.class, new ArrayList<ResultMapping>()).build();
        
        /**
         * 第8步：创建Cache对象：
         * 这是MyB ati s 根据装饰模式创建的缓存对象，通过层层装饰使得简单的缓存拥有了可配置的复杂功能。各级装饰缓存的含义可以参考代码中对应的注释。
         */
        final Cache countryCache =  
                new SynchronizedCache(
                new SerializedCache(
                new LoggingCache(
                new LruCache(// 最近最少使用缓存，least recently use
                new PerpetualCache("country_cache")))));

        /**
         * 第9部分：创建MappedStatement对象。
         * 上述第一、五、六、七、八五个部分己经准备好了一个SQL执行和映射的基本配置，
         * MappedStatement就是对SQL更高层次的一个封装，这个对象包含了执行SQL所需的各种配置信息
         */
     // 第2个参数就是这个sql语句的唯一id，相当于在xml和接口模式下namespace和id的组合，但是这里的id不需要和xml+接口中的方法匹配，只要取一个唯一的id就行
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, 
        		"tk.mybatis.simple.mapper.SimpleMapper.selectCountryById", 
        		sqlSource, 
        		SqlCommandType.SELECT);
        List<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(rm);
        
        msBuilder.parameterMap(paramBuilder.build());
        msBuilder.resultMaps(resultMaps);
        msBuilder.cache(countryCache);
        MappedStatement ms = msBuilder.build();
        
        /**
         * 第10步：有了封装sql的MappedStatement和执行sql的Executo，就可以执行sql语句了。
         * 执行下面的方法便可以得到查询的结果。当使用MyBatis时， 项目启动后就已经准备好了所有方法对应的MappedStatement对象。
         * 在执行MyBatis的数据库操作时，底层就是通过调用Executor相应的方法来执行。
         */
        // 第2个参数是查询参数；第3个参数是MyBatis内存分页参数，DEFAULT表示查询全部不用分页；第4个参数大多数情况下都是null，MyBatis本身的结果映射已经做得很好了，设置为null可以使用默认的结果映射处理器。
//        List<Country> countries = exe.query(ms, 1L, RowBounds.DEFAULT, null);
//        System.out.println(countries.get(0));
        
        /**
         * 第11步：第10步中使用Executor执行并不方便，因此可以再提高一个层次，将上面的操作封装起来，使查询者更方便调用。
         * 先将MappedStatement添加到Configuration中，在Configuration中会以Map的形式记录，其中Map的key就是MappedStatement的id，
         * 这样就可以很方便地通过id从Configuration中获取MappedStatement了，在使用完整id 保存的同时，还会尝试使用“．”分割最后的字符串（通常是方法名）作为key 井保存一份。
         * 如果key 已经存在，就会标记该key有歧义，这种情况下若通过短的key调用就会因为有歧义而抛出异常。
         */
        config.addMappedStatement(ms);
        SqlSession sqlSession = new DefaultSqlSession(config, exe, false);
        Country country = sqlSession.selectOne("selectCountryById", 1L);// 通过方法名作为key调用mappedStatement
//        Country country = sqlSession.selectOne("tk.mybatis.simple.mapper.SimpleMapper.selectCountryById", 1L);// 也可通过完整的id作为key调用mappedStatement
        System.out.println(country);
        
        /**
         * 第12步：后来就有人将第11步的这种调用方式封装为DAO和DAOImpl，在DAOImpl中实现第11步，但是这种方法比较麻烦且工作量比较大，故逐渐被淘汰
         */
        
        /**
         * 第13步：再后来，MyBatis使用JDK动态代理解决了解决了DAO接口实现类的问题，使得我们使用起来更加方便且不用再去实现一个DAOImpl。
         * 基于这个，我们还可以在提高一个层次，使用动态代理的方式实现接口调用：
         * 1. 创建Mapper接口，SimplerMapper。
         * 2. 创建代理接口，并且调用查询方法。
         */
        MapperProxyFactory<SimpleMapper> mapperProxyFactory = new MapperProxyFactory<>(SimpleMapper.class);
        //创建代理接口，动态代理工厂类创建动态接口时传入了参数SqlSession ，这是对SqlSession更高层次的封装，从上面的方法调用也能看出使用接口是多么方便。
        SimpleMapper simpleMapper = mapperProxyFactory.newInstance(sqlSession);
        Country country1 = simpleMapper.selectCountryById(1L);
        System.out.println(country1);
        
        /**
         * 总结：MyBatis 底层的层层封装包含了很多细节，在使用时并不需要了解封装的具体过程， 只要能够直接利用接口方式去执行各种各样的方法即可。
         * 阅读MyBatis 的源码可以学到很多东西，如缓存的装饰模式、大量Builder 类的建造者模式、拦截器的代理链调用等。
         * 这一节的内容虽然尽可能涵盖MyBatis 的方方面面，但是仍然有很多细节隐藏在各个步骤的实现类中没有单独说明，比如MyBatis 将ResultSet 映射到对象中的方法，
         * 再比如处理复杂的关联查询以及关联查询结果的映射的方法等，因此MyBaits 中还有很多需要我们深入学习的内容。
         */
        
        
	}
	
	
	
	
	
	
	
	
	
}
