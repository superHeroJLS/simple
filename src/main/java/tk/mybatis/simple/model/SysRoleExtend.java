package tk.mybatis.simple.model;

/**
 * 1. 包含role数据
 * 2. 还包含user的userName数据
 * 这种方式比较适合在需要少量额外宇段时使用， 但是如果需要其他表中大量列的值时，这种方式就不适用了， 因为我们不能将一个类的属性都照搬到另一个类中
 * @author jls
 */
public class SysRoleExtend extends SysRole {
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "SysRoleExtend [userName=" + userName + ", getId()=" + getId() + ", getRoleName()=" + getRoleName()
				+ ", getEnabled()=" + getEnabled() + ", getCreateBy()=" + getCreateBy() + ", getCreateTime()="
				+ getCreateTime() + "]";
	}

	
		
}
