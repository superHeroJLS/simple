package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleUseEnum;
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
			roleMapper.updateById(role);
			
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
	 * 测试MyBatis二级缓存（sqlSessionFactory缓存）
	 */
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
