package tk.mybatis.simple.mapper.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 * 为什么Mapper 接口没有实现类却能被正常调用呢？
 * 这是因为MyBaits 在Mapper 接口上使用了动态代理的一种非常规的用法，熟悉这种动态代
 * 理的用法不仅有利于理解MyBatis 接口和XML 的关系，还能开阔思路。接下来提取出这种动
 * 态代理的主要思路，用代码来为大家说明。<br/>
 * <b>使用Java动态代理的方式创建一个代理类。</b><br/>
 * 由于方法参数和返回值存在很多种情况，因此MyBatis 的内部实现会比上面的逻辑复杂得
 * 多，正是因为MyBatis 对接口动态代理的实现，我们在使用接口方式的时候才会如此容易。
 * @author Administrator
 *
 * @param <T>
 */
public class MyMapperProxy<T> implements InvocationHandler{
	private Class<T> mapperInterface;
	private SqlSession sqlSeesion;
	
	public MyMapperProxy(Class<T> mapperInterface, SqlSession sqlSeesion) {
		super();
		this.mapperInterface = mapperInterface;
		this.sqlSeesion = sqlSeesion;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//针对不同的sql类型，需要调用sqlSession的不同方法
		//接口方法中的参数也有很多情况，这里只考虑没有参数的情况
		List<T> list = sqlSeesion.selectList(mapperInterface.getCanonicalName() + "." + method.getName());
		//返回值也有很多情况，这里不做处理直接返回
		return list;
	}
	
}
