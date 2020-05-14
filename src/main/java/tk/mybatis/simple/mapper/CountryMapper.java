package tk.mybatis.simple.mapper;

import java.util.List;

import tk.mybatis.simple.model.Country;

public interface CountryMapper {
	List<Country> selectAll();
}
