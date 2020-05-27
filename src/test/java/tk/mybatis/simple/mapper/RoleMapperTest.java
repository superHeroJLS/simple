package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleUseEnum;
import tk.mybatis.simple.model.SysUser;
import tk.mybatis.simple.type.Enabled;

public class RoleMapperTest extends BaseMapperTest {
	
	@Ignore
	@Test
	public void testSelectAllRoleAndPrivilegeList() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			List<SysRole> roleList = roleMapper.selectAllRoleAndPrivilegeList();
			Assert.assertNotNull(roleList);
			for (SysRole role : roleList) {
				System.out.println("roleName: " + role.getRoleName() + "-" + role.getId());
				for (SysPrivilege privilege : role.getPrivilegeList()) {
					System.out.println("privilegeName: " + privilege.getPrivilegeName());
				}
			}
		} finally {
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testSelectRoleByUserIdChoose() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			SysRole role = roleMapper.selectRoleById(2L);
			role.setEnabled(0);//给其中一个role的enabled赋值为0
			roleMapper.updateEnabledById(role);
			
			List<SysRole> roleList = roleMapper.selectRoleByUserIdChoose(1L);
			for (SysRole r : roleList) {
				System.out.println("roleName: " + r.getRoleName() + "-" + r.getId());
				if (r.getId().equals(1L)) {
					//存在权限信息
					Assert.assertNotNull(r.getPrivilegeList());
				} else if (r.getId().equals(2L)) {
					//no privilege infomation
					Assert.assertNull(r.getPrivilegeList());
					System.err.println("error-----" + r.getRoleName() + " no privilege");
	 				continue;
				}
				for (SysPrivilege privilege : r.getPrivilegeList()) {
					System.out.println("roleName: " + r.getRoleName() + " ,privilegeName: " + privilege.getPrivilegeName());
				}
			}
		} finally {
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRoleByIdUseEnum() {
		SqlSession sqlSession = getSqlSession() ;
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class) ;
			//先查询出角色，然后修改角色的enabled 值为disabled
			SysRoleUseEnum role = roleMapper.selectRoleByIdUseEnum(2L);
			System.out.println("before: " + role);
			Assert.assertEquals(Enabled.enabled, role.getEnabled());
			
			role.setEnabled(Enabled.disabled) ;
			roleMapper.updateByIdUseEnum(role);
			
			SysRoleUseEnum roleAfter = roleMapper.selectRoleByIdUseEnum(2L);
			System.out.println("after: " + roleAfter);
		} finally {
			sqlSession.rollback();
			sqlSession.close() ;
		}
	}
	
	/**
	 * <ol>测试MyBatis二级缓存（sqlSessionFactory缓存）：
	 * 	<li>使用MyBatis自带的二级缓存</li>
	 * 	<li>使用ehcache二级缓存</li>
	 * <ol>
	 * 
	 */
	@Ignore
	@Test
	public void testL2Cache() {
		//获取sql Session
		SqlSession sqlSession = getSqlSession() ;
		SysRole rolel = null;
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//第一次查询
			rolel = roleMapper.selectRoleById(1L);
			/*
			 * 这里修改role1的属性位后，按照常理应该更新数据，更新后会清空一、二级缓存，这样在第二部分的代码中就不会出现查询结果的roleName都是New Name的情况了。
			 * 所以想要安全使用，需要避免毫无意义的修改。这样就可以避免人为产生的脏数据，避免缓存和数据库的数据不一致。
			 */
			rolel.setRoleName ("New Name") ;
			
			//第二次查询，命中MyBatis一级缓存
			SysRole role2 = roleMapper.selectRoleById(1L);
			Assert.assertEquals ("New Name", role2.getRoleName());//虽然没有更新数据库，但是这个用户名和role1重新赋值的名字相同
			Assert.assertEquals(rolel , role2);//role2 和rolel完全就是同一个实例，都是MyBatis一级缓存中的实例
			
		} finally {
			//关闭当前的sqlSessio，此时一级缓存数据保存至二级缓存中，保存之后二级缓存在有了缓存数据
			sqlSession.close();
		}
		
		System.err.println ("开启新的sqlSession");
		sqlSession = getSqlSession();
		
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			/*
			 * 新sqlSession第一次查询，命中MyBatis二级缓存；
			 * 此时一级缓存（sqlSession缓存）是没有数据的，因为没有对数据库发起查询请求
			 */
			SysRole role2 = roleMapper.selectRoleById(1L);
			Assert.assertEquals("New Name", role2.getRoleName());//第二个sqlSession获取的用户名是New Nam
			Assert.assertNotEquals ( rolel , role2) ;//这里的role2 和前一个sqlSession查询的结果是两个不同的实例，因为是从MyBatis二级缓存中通过反序列化返回的不同实例
			
			/*
			 * 新sqlSession第二次查询，命中MyBatis二级缓存；
			 * 此时一级缓存（sqlSession缓存）还是没有数据的，因为没有对数据库发起查询请求
			 */
			SysRole role3 = roleMapper.selectRoleById(1L);
			Assert.assertNotEquals(role2 , role3);//这里的role2 和role3 是两个不同的实例，因为是从MyBatis二级缓存中通过反序列化返回的不同实例
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * <ol>测试MyBatis二级缓存（sqlSessionFactory缓存）：
	 * 	<li>使用Redis作为二级缓存</li>
	 * <ol></br>
	 * <b>这个测试方法运行过一次之后，Redis缓存中就已经存在查询数据的缓存了，Redis作为缓存服务器，它缓存的数据和程序（或测试）的启动无关， 
	 * Redis的缓存并不会因为应用的关闭而失效。所以再次执行时没有进行一次数据库查询，所有查询都使用Redis二级缓存，不会再查询数据库。</b>
	 */
	@Ignore
	@Test
	public void testL2CacheOfRedis() {
		//获取sql Session
		SqlSession sqlSession = getSqlSession() ;
		SysRole rolel = null;
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//第一次查询
			rolel = roleMapper.selectRoleById(1L);
			/*
			 * 这里修改role1的属性位后，按照常理应该更新数据，更新后会清空一、二级缓存，这样在第二部分的代码中就不会出现查询结果的roleName都是New Name的情况了。
			 * 所以想要安全使用，需要避免毫无意义的修改。这样就可以避免人为产生的脏数据，避免缓存和数据库的数据不一致。
			 */
			rolel.setRoleName ("New Name") ;
			
			//第二次查询，命中MyBatis一级缓存
			SysRole role2 = roleMapper.selectRoleById(1L);
			Assert.assertEquals ("New Name", role2.getRoleName());//虽然没有更新数据库，但是这个用户名和role1重新赋值的名字相同
			Assert.assertEquals(rolel , role2);//role2 和rolel完全就是同一个实例，都是MyBatis一级缓存中的实例
			
		} finally {
			//关闭当前的sqlSessio，此时一级缓存数据保存至二级缓存中，保存之后二级缓存在有了缓存数据
			sqlSession.close();
		}
		
		System.err.println ("开启新的sqlSession");
		sqlSession = getSqlSession();
		
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			/*
			 * 新sqlSession第一次查询，命中MyBatis二级缓存；
			 * 此时一级缓存（sqlSession缓存）是没有数据的，因为没有对数据库发起查询请求
			 */
			SysRole role2 = roleMapper.selectRoleById(1L);
			Assert.assertEquals("New Name", role2.getRoleName());//第二个sqlSession获取的用户名是New Nam
			Assert.assertNotEquals ( rolel , role2) ;//这里的role2 和前一个sqlSession查询的结果是两个不同的实例，因为是从MyBatis二级缓存中通过反序列化返回的不同实例
			
			/*
			 * 新sqlSession第二次查询，命中MyBatis二级缓存；
			 * 此时一级缓存（sqlSession缓存）还是没有数据的，因为没有对数据库发起查询请求
			 */
			SysRole role3 = roleMapper.selectRoleById(1L);
			Assert.assertNotEquals(role2 , role3);//这里的role2 和role3 是两个不同的实例，因为是从MyBatis二级缓存中通过反序列化返回的不同实例
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * 在这个测试中， 一共有3 个不同的SqlSession 。第一个SqlSession 中获取了用户和关联的角色信息， 第二个SqlSessio 口中查询角色并修改了角色的信息，第三个SqlSessio
	 * 中查询用户和关联的角色信息。这时从缓存中直接取出数据，就出现了脏数据，因为角色名称己经修改，但是这里读取到的角色名称仍然是修改前的名字，因此出现了脏读。
	 * 
	 * </br>该如何避免脏数据的出现呢？这时就需要用到参照缓存了。当某几个表可以作为一个业务整体时，通常是让几个会关联的ER 表同时使用同一个二级缓存，这样就能解决脏数据问题。
	 * <b>在上面这个例子中，将UserMapper.xml中的缓存配置修改如下:
	 * <cache-ref namespace= "tk.mybatis.simple.mapper.RoleMapper"><cache-ref></b>
	 * 
	 * </br>修改为参照缓存后，再次执行测试，这时就会发现在第二次查询用户和关联角色信息时并没有使用二级缓存，而是重新从数据库获取了数据。虽然这样可以解决脏数据的问题，但是并
	 * 不是所有的关联查询都可以这么解决，如果有几十个表甚至所有表都以不同的关联关系存在于各自的映射文件中时，使用参照缓存显然没有意义。
	 */
	@Test
	public void testDirtyDate() {
		SqlSession sqlSession = getSqlSession();
		try {
			//查询用户和角色信息
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleById(1001L);
			Assert.assertEquals("普通用户", user.getRole().getRoleName());
			System.out.println("角色名：" + user.getRole().getRoleName());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();//sqlSession关闭，一级缓存数据存至二级缓存
		}
		
		//开启新sqlSession
		System.err.println("开启新的sqlSession");
		sqlSession = getSqlSession();
		try {
			//更新ID为2的角色信息
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			SysRole role = roleMapper.selectRoleById(2L);
			role.setRoleName("脏数据");
			roleMapper.updateById(role);
			sqlSession.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		
		//再开启新sqlSession
		System.err.println("再次开启新的sqlSession");
		sqlSession = getSqlSession();
		try {
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			SysUser user = userMapper.selectUserAndRoleById(1001L);
			SysRole role = roleMapper.selectRoleById(2L);
			Assert.assertEquals("普通用户", user.getRole().getRoleName());
			Assert.assertEquals("脏数据", role.getRoleName());
			System.out.println("用户-角色名：" + user.getRole().getRoleName());
			
			//还原数据
			role.setRoleName("普通用户");
			roleMapper.updateById(role);
			sqlSession.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
