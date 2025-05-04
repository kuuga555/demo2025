package com.example.demo.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CurrentPriceRowMapper implements RowMapper<CurrentPrice> {

	@Override
    public CurrentPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
		CurrentPrice result = new CurrentPrice();
        {
        	result.setId(rs.getInt("id"));
        	result.setShortName(rs.getString("short_name"));
        	result.setLongName(rs.getString("long_name"));
        	result.setExchangeRate(rs.getDouble("exchange_rate"));
        	result.setUpdDate(rs.getDate("upd_date"));
        }
        return result;
    }
}
