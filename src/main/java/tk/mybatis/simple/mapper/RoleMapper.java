package tk.mybatis.simple.mapper;

import java.util.List;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysRoleUseEnum;

public interface RoleMapper {

	List<SysRole> selectAllCopy2();
	
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
