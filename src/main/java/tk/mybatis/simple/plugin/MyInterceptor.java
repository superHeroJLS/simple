package tk.mybatis.simple.plugin;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 拦截ResultSetHandler中的handleResultSets(Statement stmt)方法
 * @author Administrator
 *
 */
@Intercepts({
	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class MyInterceptor implements Interceptor {
	private String field;

	/* 
	 * 这个是MyBatis Interceptor运行时要执行的拦截方法。通过该方法的参数invocation可以得到很多有用的信息；
	 * 这里代码没有做任何特殊处理，直接返回了执行结果
	 */
	@SuppressWarnings("unused")
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();//获取被拦截的对象
		Method method = invocation.getMethod();//获取当前被拦截的方法
		Object[] args = invocation.getArgs();//获取被拦截方法中的参数
		System.err.println("-----MyIntercept method is called-----");
		Object result = invocation.proceed();//执行当前被拦截的方法，实际执行了method.invoke(target, args)
		return result;
	}

	/* 
	 * 方法的参数target 就是拦截器要拦截的对象，该方法会在创建被拦截的接口实现类时被调用；
	 * 方法的实现很简单，只需要调用MyBatis提供的Plugin(org.apache.ibatis.plugin.Plugin)类的wrap静态方法就可以通过Java的动态代理拦截目标对象
	 * Plugin.wrap方法会自动判断拦截器的签名和被拦截对象的接口是否匹配，只有匹配的情况下才会使用动态代理拦截目标对象，因此在上面的实现方法中不必做额外的逻辑判断
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	
	/* 
	 * 在拦截器中可以很方便地通过Properties取得配置的参数值，
	 * 参数是在mybatis-config.xml中的plugins中的plugin中的property标签配置的
	 */
	public void setProperties(Properties properties) {
		System.out.println(properties.get("field").toString());
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	

}
