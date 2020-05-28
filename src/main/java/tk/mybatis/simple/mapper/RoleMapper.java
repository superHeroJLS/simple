package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespaceRef;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleUseEnum;

//@CacheNamespaceRef(RoleMapper.class)//配置MyBatis二级缓存参照，直接参照RoleMapper.xml中配置好的MyBatis二级缓存
public interface RoleMapper {

	List<SysRole> selectAllCopy2();
	
	int updateEnabledById(SysRole role);
	
	int updateById(SysRole role);
	
	SysRole selectRoleById(Long id);
	
	/**
	 * 查询所有的role和privilege，role和privilege的关系是一对多
	 * @return
	 */
	List<SysRole> selectAllRoleAndPrivilegeList();
	
	List<SysRole> selectRoleByUserId(Long userId);

	/**
	 * @param userId
	 * @return
	 */
	List<SysRole> selectRoleByUserIdChoose(Long userId);
	
	SysRoleUseEnum selectRoleByIdUseEnum(Long id);
	
	int updateByIdUseEnum(SysRoleUseEnum use);
	
	
}
