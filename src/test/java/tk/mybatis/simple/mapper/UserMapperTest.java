package tk.mybatis.simple.mapper;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import tk.mybatis.simple.mapper.impl.MyMapperProxy;
import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleExtend;
import tk.mybatis.simple.model.SysUser;
import tk.mybatis.simple.type.Enabled;

public class UserMapperTest extends BaseMapperTest{
	
	@Ignore
	@Test
	public void testSelectById() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser sysUser = userMapper.selectById(1L);
			Assert.assertNotNull(sysUser);
			Assert.assertEquals("admin", sysUser.getUserName());
			
			System.out.println(sysUser);
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectAll() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> sysUserList = userMapper.selectAll();
			Assert.assertNotNull(sysUserList);
			Assert.assertTrue(sysUserList.size() > 0);
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRolesByUserId() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysRole> roleList = userMapper.selectRolesByUserId(1L);
			Assert.assertNotNull(roleList);
			Assert.assertTrue(roleList.size() > 0);
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRolesWithUserInfoByUserId1() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysRoleExtend> roleList = userMapper.selectRolesWithUserInfoByUserId1(1L);
			Assert.assertNotNull(roleList);
			Assert.assertTrue(roleList.size() > 0);
			for (SysRoleExtend sysRoleExtend : roleList) {
				System.out.println(sysRoleExtend.toString());
			}
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRolesWithUserInfoByUserId2() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysRole> roleList = userMapper.selectRolesWithUserInfoByUserId2(1L);
			Assert.assertNotNull(roleList);
			Assert.assertTrue(roleList.size() > 0);
			for (SysRole sysRole : roleList) {
				System.out.println(sysRole.toString());
			}
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testInsert() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setUserName("test1");
			user.setUserPassword("123456");
			user.setUserEmail("test@tk.mybatis");
			user.setUserInfo("test info");
			//假装是一张图片
			user.setHeadImg(new byte[]{1,2,3});
			user.setCreateTime(new Date());
			int result = userMapper.insert(user);
			//只插入一条数据
			Assert.assertEquals(1, result);
			//未给id设值，也没有配置回写id的值
			Assert.assertNull(user.getId());
			
		} finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testInsert2() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setUserName("test2");
			user.setUserPassword("123456");
			user.setUserEmail("test@tk.mybatis");
			user.setUserInfo("test2 info");
			//假装是一张图片
			user.setHeadImg(new byte[]{1,2,3});
			user.setCreateTime(new Date());
			int result = userMapper.insert2(user);
			//只插入一条数据
			Assert.assertEquals(1, result);
			//未给id设值，也没有配置回写id的值
			Assert.assertNotNull(user.getId());
			System.out.println(user.toString());
			
		} finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testInsert3() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setUserName("test3");
			user.setUserPassword("123456");
			user.setUserEmail("test@tk.mybatis");
			user.setUserInfo("test3 info");
			//假装是一张图片
			user.setHeadImg(new byte[]{1,2,3});
			user.setCreateTime(new Date());
			int result = userMapper.insert3(user);
			//只插入一条数据
			Assert.assertEquals(1, result);
			//未给id设值，也没有配置回写id的值
			Assert.assertNotNull(user.getId());
			System.out.println(user.toString());
			
		} finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testUpdateById() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser dbUser = userMapper.selectById(1L);
			Assert.assertEquals("admin", dbUser.getUserName());
			
			dbUser.setUserName("admin_test");
			dbUser.setUserEmail("test@mybatis.tk");
			int result = userMapper.updateById(dbUser);
			
			//只更新一条数据
			Assert.assertEquals(1, result);
			dbUser = userMapper.selectById(1L);
			//修改后的名字是admin_test
			Assert.assertEquals("admin_test", dbUser.getUserName());
			System.out.println("-----userName-----: " + dbUser.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testDelteById() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser dbUser = userMapper.selectById(1L);
			Assert.assertNotNull(dbUser);
			
			//删除
			Assert.assertEquals(1, userMapper.deleteById(1L));
			//再次查询
			Assert.assertNull(userMapper.selectById(1L));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRolesByUserIdAndRoleEnabled() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysRole> roleList = userMapper.selectRolesByUserIdAndRoleEnabled(1L, 1);
			
			Assert.assertNotNull(roleList);
			Assert.assertTrue(roleList.size() >0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectRolesByUserAndRole() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//为了查询手动创建user和role
			SysUser user = new SysUser();
			user.setId(1L);
			SysRole role = new SysRole();
			role.setEnabled(1);
			
			List<SysRole> roleList = userMapper.selectRolesByUserAndRole(user, role);
			
			Assert.assertNotNull(roleList);
			Assert.assertTrue(roleList.size() >0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByUser() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			SysUser query= new SysUser() ;
			query.setUserName("ad");
			List<SysUser> userList = userMapper.selectByUser(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//只查询用户邮箱时
			query= new SysUser();
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//当同时查询用户名和邮箱时
			query= new SysUser();
			query.setUserName ("ad");
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser(query) ;
			Assert.assertTrue(userList.size() == 0);//由于没有同时符合这两个条件的用户，因此查询结采数为0
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testUpdateByIdSelective() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setId(1L);
			user.setUserEmail("test@mybatis.tk2");;
			//更新邮箱，特别注意，这里的返回值result执行的是SQL 影响的行数
			int result = userMapper.updateByIdSelective(user);
			Assert.assertEquals(1, result);//只更新1 条数据
			
			//根据当前id 查询修改后的数据
			user= userMapper.selectById(1L);
			//修改后的名字保持不变，但是邮箱变成了新的
			Assert.assertEquals("admin", user.getUserName()) ;
			Assert.assertEquals("test@mybatis.tk2", user.getUserEmail());
			System.out.println(user.getUserEmail());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testInsertWithIf() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setUserName("test-selective");
			user.setUserPassword("123456");
			user.setUserInfo("test2 info");
			//假装是一张图片
			user.setCreateTime(new Date());
			int result = userMapper.insertWithIf(user);
			//只插入一条数据
			Assert.assertEquals(1, result);
			//未给id设值，也没有配置回写id的值
			Assert.assertNotNull(user.getId());
			System.out.println(user.getId());
			
			//获取插入的这条数据
			user= userMapper.selectById(user.getId());
			Assert.assertEquals ("test@mybatis.tk", user.getUserEmail());
			
		} finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByIdOrUserName() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			SysUser query= new SysUser();
			query.setId(1L);
			query.setUserName("admin");
			SysUser user = userMapper.selectByIdOrUserName(query) ;
			Assert.assertNotNull(user) ;
			
			//当没有Id时
			query.setId(null) ;
			user = userMapper.selectByIdOrUserName(query) ;
			Assert.assertNotNull(user);
			
			//当Id和name都为空时
			query.setUserName(null) ;
			user= userMapper.selectByIdOrUserName(query);
			Assert.assertNull(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testSelectByUser2() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			SysUser query= new SysUser() ;
			query.setUserName("ad");
			List<SysUser> userList = userMapper.selectByUser2(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//只查询用户邮箱时
			query= new SysUser();
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser2(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//当同时查询用户名和邮箱时
			query= new SysUser();
			query.setUserName ("ad");
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser2(query) ;
			Assert.assertTrue(userList.size() == 0);//由于没有同时符合这两个条件的用户，因此查询结采数为0
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectWithTrim() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			SysUser query= new SysUser() ;
			query.setUserName("ad");
			List<SysUser> userList = userMapper.selectWithTrim(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//只查询用户邮箱时
			query= new SysUser();
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectWithTrim(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//当同时查询用户名和邮箱时
			query= new SysUser();
			query.setUserName ("ad");
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectWithTrim(query) ;
			Assert.assertTrue(userList.size() == 0);//由于没有同时符合这两个条件的用户，因此查询结采数为0
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testUpdateWithTrim() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = new SysUser();
			user.setId(1L);
			user.setUserEmail("test@mybatis.tk2");;
			//更新邮箱，特别注意，这里的返回值result执行的是SQL 影响的行数
			int result = userMapper.updateWithTrim(user);
			Assert.assertEquals(1, result);//只更新1 条数据
			
			//根据当前id 查询修改后的数据
			user= userMapper.selectById(1L);
			//修改后的名字保持不变，但是邮箱变成了新的
			Assert.assertEquals("admin", user.getUserName()) ;
			Assert.assertEquals("test@mybatis.tk2", user.getUserEmail());
			System.out.println(user.getUserEmail());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByIdList() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			List<Long> idList = new ArrayList<Long>();
			idList.add(1L);
			idList.add(1001L);
			List<SysUser> userList = userMapper.selectByIdList(idList);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testSelectByIdArray() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			List<Long> idList = new ArrayList<Long>();
			idList.add(1L);
			idList.add(1001L);
			List<SysUser> userList = userMapper.selectByIdArray(idList.toArray(new Long[]{}));
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByKeyInMap() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			List<Long> idList = new ArrayList<Long>();
			idList.add(1L);
			idList.add(1001L);
			map.put("ids", idList);
			List<SysUser> userList = userMapper.selectByKeyInMap(map);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testSelectByMap() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			Map<String, Long> map = new HashMap<String, Long>();
			map.put("1", 1L);
			map.put("2", 1001L);
			List<SysUser> userList = userMapper.selectByMap(map);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testBatchInsert() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> userList = new ArrayList<SysUser>();
			for (int i = 0 ; i < 2; i++) {
				SysUser user = new SysUser() ;
				user.setUserName(" test"+ i);
				user.setUserPassword("123456");
				user.setUserEmail("test@mybatis.tk");
				userList.add(user);
			}
			
			int result = userMapper.batchInsert(userList);
			//只插入一条数据
			Assert.assertEquals(2, result);
			//未给id设值，也没有配置回写id的值
			System.out.println(userList);
			
		} finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testUpdateByMap() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			
			Map<String, Object> map = new HashMap<String, Object>();
			//map中的key存放的是数据库表中字段名称
			map.put("id", 1L);
			map.put("user_email", "test@map.tk");
			map.put("user_password", "123456789");
			
			userMapper.updateByMap(map);
			SysUser user = userMapper.selectById(1L);
			Assert.assertEquals("test@map.tk", user.getUserEmail());
			System.out.println(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//选择回滚，不影响其他测试，默认的openSeeion()是不会auto commit，故不手动执行commit是不会提交的
			sqlSession.rollback();
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByUserWithBind() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			//只查询用户名
			SysUser query= new SysUser() ;
			query.setUserName("ad");
			List<SysUser> userList = userMapper.selectByUserWithBind(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//只查询用户邮箱时
			query= new SysUser();
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser(query);
			Assert.assertTrue(userList.size() > 0);
			System.out.println(userList);
			
			//当同时查询用户名和邮箱时
			query= new SysUser();
			query.setUserName ("ad");
			query.setUserEmail("test@mybatis.tk");
			userList = userMapper.selectByUser(query) ;
			Assert.assertTrue(userList.size() == 0);//由于没有同时符合这两个条件的用户，因此查询结采数为0
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectByIdWithStaticMethod() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> users = userMapper.selectByIdWithStaticMethod(1L);
			Assert.assertNotNull(users);
			Assert.assertEquals("admin", users.get(0).getUserName());
			
			System.out.println(users);
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectUserAndRoleById() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleById(1001L);
			Assert.assertNotNull(user);
			Assert.assertNotNull(user.getRole());
			
		} finally {
			sqlSession.close();
		}
	}

	@Ignore
	@Test
	public void testSelectUserAndRoleById2() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleById2(1001L);
			Assert.assertNotNull(user);
			Assert.assertNotNull(user.getRole());
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectUserAndRoleById3() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleById3(1001L);
			Assert.assertNotNull(user);
			Assert.assertNotNull(user.getRole());
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectUserAndRoleByIdSelect() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleByIdSelect(1001L);
			Assert.assertNotNull(user);
//			System.out.println("-----调用user.equals(null)-----");//验证mybatis配置中的属性lazyLoadTriggerMethods的作用
			System.out.println("-----调用user.getRole()-----");
			Assert.assertNotNull(user.getRole());
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectAllUserAndRoleList() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> userList = userMapper.selectAllUserAndRoleList();
			Assert.assertNotNull(userList);
			for (SysUser sysUser : userList) {
				System.out.println("-----userName: -----" + sysUser.getUserName());
				for (SysRole role : sysUser.getRoleList()) {
					System.out.println("roleName: " + role.getRoleName() + "-" + role.getId());
				}
			}
			
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectAllUserAndRoleListAndPrivilegeList() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> userList = userMapper.selectAllUserAndRoleListAndPrivilegeList();
			Assert.assertNotNull(userList);
			for (SysUser sysUser : userList) {
				System.out.println("userName: " + sysUser.getUserName());
				for (SysRole role : sysUser.getRoleList()) {
					System.out.println("roleName: " + role.getRoleName() + "-" + role.getId());
					for (SysPrivilege privilege : role.getPrivilegeList()) {
						System.out.println("privilegeName: " + privilege.getPrivilegeName());
					}
				}
			}
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectAllUserAndRoleListSelect() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			List<SysUser> userList = userMapper.selectAllUserAndRoleListSelect();
			Assert.assertNotNull(userList);
			for (SysUser sysUser : userList) {
				System.out.println("userName: " + sysUser.getUserName());
				for (SysRole role : sysUser.getRoleList()) {
					System.out.println("roleName: " + role.getRoleName() + "-" + role.getId());
					for (SysPrivilege privilege : role.getPrivilegeList()) {
						System.out.println("privilegeName: " + privilege.getPrivilegeName());
					}
				}
			}
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectUserByIdWithProcedure() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user= new SysUser();
			user.setId(1016L);
			userMapper.selectUserByIdWithProcedure(user);
			Assert.assertNotNull(user.getUserName());
			System.out.println("用户名： " + user.getUserName());
			System.out.println(user);
		} finally {
			sqlSession.close();
		}
	}
	
	@Ignore
	@Test
	public void testSelectUserPage() {
		SqlSession sqlSession = getSqlSession();
		try {
			
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userName", "ad");
			params.put("offset", 0);
			params.put("limit", 10);
			params.put("total", 0L);
			List<SysUser> users = userMapper.selectUserPageWithProcedure(params);
			System.out.println("total: " + (Long) params.get("total"));
			for (SysUser user : users) {
				System.out.println(user);
			}
		} finally {
			sqlSession.close();
		}
	}
	
	@Test
	public void testInsertUserAndRolesWithProcedure() {
		SqlSession sqlSession = getSqlSession() ;
		try {
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class) ;
			SysUser user= new SysUser();
			user.setUserName("test") ;
			user.setUserPassword("123456");
			user.setUserEmail("test@mybatis.tk");
			user.setUserInfo("userInfo");
			user.setHeadImg(new byte[]{1,2,3});
			//插入用户信息和角色关联信息
			userMapper.insertUserAndRolesWithProcedure(user, "1,2");
			Assert.assertNotNull(user.getId());
			Assert.assertNotNull(user.getCreateTime());
			
			//可以执行下面的commit 后再查看数据库中的数据
			sqlSession.commit();
			//测试删除刚刚插入的数据
			userMapper.deleteUserByIdWithProcedure(user.getId());
		} finally {
			sqlSession.commit();//记得commit，否则delete语句不会生效
			sqlSession.close( );
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 测试Mapper接口(MyMapperProxy)的代理，从代理类中可以看到，当调用一个接口的方法时，会先通过接口的全限定名称和当前调用的方法名的组合得到一个方法id，这个id的值就是映射XML中namespace
	 * 和具体方法id的组合。。所以可以在代理方法中使用sqlSession 以命名空间的方式调用方法。通过这种方式
	 * 可以将接口和XML 文件中的方法关联起来。这种代理方式和常规代理的不同之处在于，这里
	 * 没有对某个具体类进行代理，而是通过代理转化成了对其他代码的调用。
	 */
	@Ignore
	@Test
	public void testMyMapperProxy() {
		SqlSession sqlSession = getSqlSession();
		MyMapperProxy<UserMapper> userMapperProxy = new MyMapperProxy<UserMapper>(UserMapper.class, sqlSession);
		UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(), 
				new Class[]{UserMapper.class}, 
				userMapperProxy);
		
		List<SysUser> userList = userMapper.selectAll();
		Assert.assertNotNull(userList);
	}
	
	
	
}
