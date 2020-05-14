package tk.mybatis.simple.model;

public class SysPrivilege {
	private Long id;
	private String privilegeName;
	private String PrivilegeUri;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrivilegeName() {
		return privilegeName;
	}
	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
	public String getPrivilegeUri() {
		return PrivilegeUri;
	}
	public void setPrivilegeUri(String privilegeUri) {
		PrivilegeUri = privilegeUri;
	}
	
}
