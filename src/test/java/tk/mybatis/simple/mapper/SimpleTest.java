package tk.mybatis.simple.mapper;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
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
	public void test() {
	    /**
	     * 第1步：手动指定使用Log4j记录日志，项目中必须要有Log4j的依赖，否则不会生效；
	     * 默认情况下， MyBatis会按照默认的顺序去寻找日志实现类， 日志的优先级顺序为:
	     * SLF4j > Apache Commons Logging > Log4j2 > Log4j > JDK Logging > StdOut Logging > NO Logging
	     * 只要MyBatis找到了对应的依赖，就会停止继续找
	     */
	    LogFactory.useLog4JLogging();
	    
	    /**
	     * 第1步：创建配置对象，且配置 settings 中的部分属性；
	     * Configuration这个类几乎包括了Mybatis中的全部内容，记录了各项属性配置，常见的 settings 中的配置基本都可以通过这个类配置
	     */
	    final Configuration config = new Configuration();
	    config.setCacheEnabled(true);// 二级缓存
	    config.setLazyLoadingEnabled(false);// 对象中属性为对象的属性的懒加载配置
	    config.setAggressiveLazyLoading(true);
	    
	    /**
	     * 第2步：添加拦截器，在后面的执行过程中可以看到拦截器的执行顺序
	     */
	    SimpleInterceptor si = new SimpleInterceptor("inteceptor 1");
	    SimpleInterceptor si2 = new SimpleInterceptor("inteceptor 2");
	    config.addInterceptor(si2);
	    config.addInterceptor(si2);
	    
	    /**
	     * 第3步：创建数据源和JDBC事务：使用MyBatis提供的最简单UnPooledDataSource创建数据源，使用JDBC事物
	     */
	    UnpooledDataSource dataSource = new UnpooledDataSource();
	    dataSource.setDriver("com.mysql.driver.Driver");
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
	     * 第6步：创建参数映射配置：
	     * 上面的sql中有一个参数id，这里需要提供ParameterMapping。
	     * 在第5步的SQL中包含一个参数id，MyBatis文档中建议在XML中不去配置parameterMap 属性，因为MyBatis 会自动根据参数去判断和生成这个配置。在底层中，这个配置是必须存在的。
	     */
	    List<ParameterMapping> pms = new ArrayList<ParameterMapping>();
	    pms.add(new ParameterMapping.Builder(config, "id", new TypeHandlerRegistry().getTypeHandler(Long.class)).build());
	    ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", Country.class , pms);
	    
	    /**
	     * 第7步：创建结果映射配置：
	     * 这种配置方式和xml中配置resultMap元素是相同的，经常使用的resultType方式，在底层仍然是ResultMap对象，但是创建起来更容易
	     */
        ResultMap rm = new ResultMap.Builder(config, "defaultResultMap", Country.class, new ArrayList<ResultMapping>(){
	        {
	            add(new ResultMapping.Builder(config, "id", "id", Long.class).build());
	            add(new ResultMapping.Builder(config, "countryName", "countryname", Long.class).build());
	            add(new ResultMapping.Builder(config, "countryCode", "countrycode", Long.class).build());
	        }
	    }).build();
	    
        // 最后一个List为空，MyBatis完全参照第3个参数来映射结果，这个resultMap相当于xml中的resultType作用
        // ResultMap rm2 = new ResultMap.Builder(config, "defaultResultMap", Country.class, new ArrayList<ResultMapping>()).build();
        
        /**
         * 第8步：创建缓存对象：
         * 这是MyB ati s 根据装饰模式创建的缓存对象，通过层层装饰使得简单的缓存拥有了可配置的复杂功能。各级装饰缓存的含义可以参考代码中对应的注释。
         */
        final Cache countryCache =  
                new SynchronizedCache(
                new SerializedCache(
                new LoggingCache(
                new LruCache(// 最少使用缓存
                new PerpetualCache("country_cache")))));
        
        
	    
	    
	}
	
	
	
	
	
	
	
}
