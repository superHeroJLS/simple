package tk.mybatis.simple.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts(
			@Signature(type = Executor.class,
				method = "query",
				args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
					)
		)
public class SimpleInterceptor implements Interceptor {
	private String name;
	
	public SimpleInterceptor(String name) {
		super();
		this.name = name;
	}

	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("-----进入拦截器：" + this.name + "-----");
		Object result = invocation.proceed();
		System.out.println("-----跳出拦截器：" + this.name + "-----");
		return result;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		// do nothing

	}

}
