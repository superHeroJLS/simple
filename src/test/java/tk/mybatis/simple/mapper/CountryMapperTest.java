package tk.mybatis.simple.mapper;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import tk.mybatis.simple.model.Country;

public class CountryMapperTest extends BaseMapperTest{
	
	@Test
	public void testSelectAllCountry() {
		SqlSession sqlSession = getSqlSession();
		
		try {
			//Mapper接口中没有名称为selectAll的方法，但是对应的Mapper.xml文件中有ID为selecAll的sql语句，也能执行。
			List<Country> countryList = sqlSession.selectList("tk.mybatis.simple.mapper.CountryMapper.selectAll");
			printfList(countryList);
		} finally {
			sqlSession.close();
		}
	}
	
	private void printfList(List<Country> countryList) {
		for (Country country : countryList) {
			System.out.printf("%4d%4s%4s\n", country.getId(), country.getCountryName(), country.getCountryCode());
		}
	}
	
}
