package tk.mybatis.simple.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleExtend;
import tk.mybatis.simple.model.SysUser;

public interface UserMapper {
	
	SysUser selectById(Long id);
	
	List<SysUser> selectAll();
	
	/**
	 * 根据用户id获取用户拥有的所有角色，返回的结果为角色集合，结果只有角色的信息，不包含额外的其他字段信息
	 * @param userId
	 * @return
	 */
	List<SysRole> selectRolesByUserId(Long userId);
	
	/**
	 * 根据用户id获取用户拥有的所有角色，返回的结果不仅只有角色集合，还有部分用户数据
	 * @param userId
	 * @return
	 */
	List<SysRoleExtend> selectRolesWithUserInfoByUserId1(Long userId);
	
	/**
	 * 根据用户id获取用户拥有的所有角色，返回的结果不仅只有角色集合，还有部分用户数据
	 * @param userId
	 * @return
	 */
	List<SysRole> selectRolesWithUserInfoByUserId2(Long userId);
	
	int insert(SysUser sysUser);
	
	int insert2(SysUser sysUser);
	
	int insert3(SysUser sysUser);
	
	int insert4(SysUser sysUser);
	
	int updateById(SysUser sysUser);
	
	int deleteById(Long id);
	
	List<SysRole> selectRolesByUserIdAndRoleEnabled(@Param("userId")Long userId, @Param("enabled")Integer enabled);
	
	List<SysRole> selectRolesByUserAndRole(@Param("user")SysUser user, @Param("role")SysRole role);
	
	List<SysUser> selectByUser(SysUser user);
	
	int updateByIdSelective(SysUser user);
	
	int insertWithIf(SysUser user);
	
	/**
	 * 当参数id 有值的时候优先使用id 查询；
	 * 当id 没有值时就去判断用户名是否有值，如果有值就用用户名查询；
	 * 如果用户名也没有值，就使SQL查询无结果。
	 * @param user
	 * @return
	 */
	SysUser selectByIdOrUserName(SysUser user);

	List<SysUser> selectByUser2(SysUser user);

	
	/**
	 * 使用trim标签替代where标签
	 * @param user
	 * @return
	 */
	List<SysUser> selectWithTrim(SysUser user);

	/**
	 * 使用trim标签替代set标签
	 * @param user
	 * @return
	 */
	int updateWithTrim(SysUser user);
	
	/**
	 * foreach 可以对数组、Map 或实现了Iterable 接口（如List、Set）的对象进行遍历
	 * 数组在处理时会转换为List对象，因此foreach 遍历的对象可以分为两大类：<b> Iterable类型和Map类型</b>
	 * @param ids
	 * 			推荐使用@Param来指定参数名称，此时foreach中属性collection的值就为@Param指定的名称；
	 * 			若不使用@Param参数，则此时foreach中属性collection的值就为list或collection。
	 * @return
	 */
	List<SysUser> selectByIdList(@Param("ids")List<Long> ids);

	/**
	 * @param idArr
	 * 			推荐使用@Param来指定参数名称，此时foreach中属性collection的值就为@Param指定的名称；
	 * 			若不使用@Param参数，则此时foreach中属性collection的值就为array
	 * @return
	 */
	List<SysUser> selectByIdArray(Long[] idArr);
	
	/**
	 * @param map
	 * 			若循环Map中某个key对应的value：
	 * 			1.参数不使用@Param注解，此时foreach中属性collection的值就为对应Map中的key；
	 * 			2.若使用@Param注解，例如@Param("map")，此时foreach中属性collection的值就为map.key；
	 * @return
	 */
	List<SysUser> selectByKeyInMap(Map<String, List<Long>> map);
	
	/**
	 * @param idMap
	 * 			若要循环Map：
	 * 			1.参数不使用@Param注解，此时foreach中属性collection的值就为_parameter；
	 * 			2.若使用@Param注解，例如@Param("map")，此时foreach中属性collection的值就为map；
	 * @return
	 */
	List<SysUser> selectByMap(@Param("map")Map<String, Long> idMap);
	
	/**
	 * 批量插入：支持的数据库有：DB2,SQLServer2008 及以上版本、PostgreSQL8.2 及以上版本、MySQL、SQLite3.7.11及以上版本、H2；
	 * MyBatis3.3.1之后支持Mysql批量插入返回主键
	 * @param userList
	 * @return
	 */
	int batchInsert(List<SysUser> userList);
	
	/**
	 * 根据map更新
	 * @param map
	 * @return
	 */
	int updateByMap(Map<String, Object> map);
	
	/**
	 * 使用concat函数连接字符串，在MySQL中，这个函数支持多个参数，但在Oracle中只支持两个参数；<br/>
	 * 由于不同数据库之间的语法差异，如果更换数据库，有些SQL 语句可能就需要重写；<br/>
	 * 针对这种情况，可以使用bind 标签来避免由于更换数据库带来的一些麻烦。<br/>
	 * bind标签中2个属性都是个必填项，name为绑定到上下文中的变量名，value为OGNL表达式；<br/>
	 * <b>bind标签拼接字符串不仅可以避免因更换数据库而修改SQL，也能预防SQL注入。大家可以根据需求，灵活使用OGNL表达式来实现功能。</b>
	 * @param user
	 * @return
	 */
	List<SysUser> selectByUserWithBind(SysUser user);

	List<SysUser> selectByIdWithStaticMethod(@Param("id")Long id);
	
	/**
	 * 假设用户和角色之间的关系是一对一，可以直接使用MyBatis的自动映射一次性将两张表中的数据都查询出来；<br/>
	 * 使用自动映射就是通过别名让MyBatis自动将值匹配到对应的宇段上， 简单的别名映射如user_name对应userName；<br/>
	 * <b>除此之外MyBatis还支持复杂的属性映射， 可以多层嵌套，例如将role.role_name映射到role.roleName上。</b>
	 * <br/>优点：减少数据库查询次数，减轻数据库压力
	 * @param userId
	 * @return
	 */
	SysUser selectUserAndRoleById(Long userId);

	SysUser selectUserAndRoleById2(Long userId);

	SysUser selectUserAndRoleById3(Long userId);
	
	SysUser selectUserAndRoleByIdSelect(Long userId);
	
	List<SysUser> selectAllUserAndRoleList();
	
	/**
	 * 一次性查询出user、role、privilege数据，2重嵌套
	 * @return
	 */
	List<SysUser> selectAllUserAndRoleListAndPrivilegeList();
	
	/**
	 * 查询所有user和roleList，roleList是通过额外sql的懒加载查询
	 * @return
	 */
	List<SysUser> selectAllUserAndRoleListSelect();
	
	/**
	 * 这个存储过程没有返回值，数据通过OUT参数设置到了user中
	 * 
	 * @param user
	 * @return
	 */
	void selectUserByIdWithProcedure(SysUser user);
	
	/**
	 * 使用存储过程实现分页查询
	 * 
	 * @param userName
	 * @param offset
	 * @param limit
	 * @param total
	 * @return
	 */
	List<SysUser> selectUserPageWithProcedure(Map<String, Object> params);
	
	/**
	 * 使用存储过程保存用户信息和角色关联信息
	 * @param user
	 * @param roleIds
	 * @return
	 */
	int insertUserAndRolesWithProcedure(@Param("user")SysUser user, @Param("roleIds")String roleIds);
	
	/**
	 * 使用存储过程根据用户id 删除用户和用户的角色信息
	 * @param id
	 * @return
	 */
	int deleteUserByIdWithProcedure(Long id);
}














