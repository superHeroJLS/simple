package tk.mybatis.simple.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 自定义TypeHandle，用于处理Enabled类型
 * 
 * @author Administrator
 *
 */
public class EnabledTypeHandler implements TypeHandler<Enabled> {
	private final Map<Integer, Enabled> enabledMap = new HashMap<Integer, Enabled>();
	
	

	public EnabledTypeHandler() {
		for (Enabled e : Enabled.values()) {
			enabledMap.put(e.getValue(), e);
		}
	}

	public void setParameter(PreparedStatement ps, int i, Enabled parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType != null)
			ps.setObject(i, parameter.getValue(), jdbcType.TYPE_CODE);
		else
			ps.setInt(i, parameter.getValue());
			
		
	}

	public Enabled getResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		return enabledMap.get(value);
	}

	public Enabled getResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		return enabledMap.get(value);
	}

	public Enabled getResult(CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
		return enabledMap.get(value);
	}

}
