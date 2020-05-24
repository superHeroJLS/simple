package tk.mybatis.simple.type;

public enum Enabled {
	disabled(0),enabled(1);
	
	private final int value;
	
	private Enabled(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
