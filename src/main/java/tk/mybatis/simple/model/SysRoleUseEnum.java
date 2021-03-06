package tk.mybatis.simple.model;

import java.util.Date;
import java.util.List;

import tk.mybatis.simple.type.Enabled;

public class SysRoleUseEnum {
	private Long id;
	private String roleName;
	private Enabled enabled;
	private Long createBy;
	private Date createTime;
	private SysUser user;
	private List<SysPrivilege> privilegeList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Enabled getEnabled() {
		return enabled;
	}
	public void setEnabled(Enabled enabled) {
		this.enabled = enabled;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public List<SysPrivilege> getPrivilegeList() {
		return privilegeList;
	}
	public void setPrivilegeList(List<SysPrivilege> privilegeList) {
		this.privilegeList = privilegeList;
	}
	@Override
	public String toString() {
		return "SysRoleUseEnum [id=" + id + ", roleName=" + roleName + ", enabled=" + enabled + ", createBy=" + createBy
				+ ", createTime=" + createTime + ", user=" + user + ", privilegeList=" + privilegeList + "]";
	}
	
	
}
