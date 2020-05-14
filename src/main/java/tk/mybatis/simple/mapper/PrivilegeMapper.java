package tk.mybatis.simple.mapper;

import java.util.List;

import tk.mybatis.simple.model.SysPrivilege;

public interface PrivilegeMapper {

	List<SysPrivilege> selectAllCopy3();
	
	List<SysPrivilege> selectByRoleId(Long roleId);
}
