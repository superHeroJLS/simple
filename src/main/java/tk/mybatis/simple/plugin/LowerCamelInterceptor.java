package tk.mybatis.simple.plugin;

import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;


/**
 * 使用Map类型的返回值。使用Map作为返回值时， Map中的key就是查询结果中的列名，而列名一般都是大小写字母或者下画线形式，
 * 和Java中使用的驼峰形式不一致，将下划线形式的key转换为小驼峰形式。
 * @author Administrator
 *
 */
@Intercepts({
	@Signature(
			type=ResultSetHandler.class,
			method="handleResultSets",
			args={Statement.class}
			)
})
public class LowerCamelInterceptor implements Interceptor {

	@SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation) throws Throwable {
		List<Object> list = (List<Object>) invocation.proceed();
		for (Object obj : list) {
			if (obj instanceof Map) {
				processMap((Map<String, Object>)obj);
			} else {
				break;
			}
		}
		
		return list;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {

	}
	
	/**
	 * 
	 * @param map
	 */
	private void processMap(Map<String, Object> map) {
		Set<String> keySet = new HashSet<String>(map.keySet());
		for (String key : keySet) {
			if ((key.charAt(0) > 'A' && key.charAt(0) < 'Z') || key.indexOf("_") >=0) {
				Object value = map.get(key);
				map.remove(key);
				map.put(underlineToCamel(key), value);
			}
		}
		
	}
	
	private String underlineToCamel(String str) {
		StringBuilder sb = new StringBuilder();
		
		boolean nextUpperCase = false;
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '_') {
				if (sb.length() > 0) {
					nextUpperCase = true;
				}
			} else {
				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
		}
		return sb.toString();
	}

}
