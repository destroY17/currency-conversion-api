package com.destroY17.repository.rowmapper;

import com.destroY17.entity.Currency;
import com.destroY17.entity.ExchangeRate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateRowMapper implements RowMapper<ExchangeRate> {
    @Override
    public ExchangeRate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("id"),
                new Currency(
                        resultSet.getInt("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getInt("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getBigDecimal("rate")
        );
    }
}
