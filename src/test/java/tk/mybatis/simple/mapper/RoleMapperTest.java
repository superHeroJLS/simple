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
	
	
	
}
