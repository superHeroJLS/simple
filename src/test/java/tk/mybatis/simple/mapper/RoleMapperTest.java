package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;

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
					Assert.assertNull(r.getPrivilegeList());
					System.out.println("-----no privilege");
	 				continue;
				}
				for (SysPrivilege privilege : r.getPrivilegeList()) {
					System.out.println("privilegeName: " + privilege.getPrivilegeName());
				}
			}
		} finally {
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
}
