package tk.mybatis.simple.mapper;

import tk.mybatis.simple.model.Country;

public interface SimpleMapper {
    
    Country selectCountryById(Long id);
}
