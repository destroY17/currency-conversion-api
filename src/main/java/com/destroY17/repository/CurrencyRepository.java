package com.destroY17.repository;

import com.destroY17.entity.Currency;
import com.destroY17.repository.rowmapper.CurrencyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyRepository implements CrudRepository<Currency, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CurrencyRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Currency> getAll() {
        final String query = "SELECT *FROM currencies";
        return jdbcTemplate.query(query, new CurrencyRowMapper());
    }

    @Override
    public Optional<Currency> getById(Integer id) {
        final String query = "SELECT *FROM currencies WHERE id = ?";
        return jdbcTemplate.query(query, new CurrencyRowMapper(), id).stream().findAny();
    }

    public Optional<Currency> getByCode(String code) {
        final String query = "SELECT *FROM currencies WHERE code = ?";
        return jdbcTemplate.query(query, new CurrencyRowMapper(), code).stream().findAny();
    }

    @Override
    public Currency save(Currency entity)  {
        final String query = "INSERT INTO Currencies (code, full_name, sign) VALUES (?, ?, ?)";
        int saveId = jdbcTemplate.update(query, entity.getCode(), entity.getName(), entity.getSign());
        entity.setId(saveId);
        return entity;
    }

    @Override
    public Currency update(Currency entity) {
        final String query = "UPDATE Currencies SET (code, full_name, sign) = (?, ?, ?) WHERE id = ?";
        jdbcTemplate.update(query, entity.getCode(), entity.getName(), entity.getSign(), entity.getId());
        return entity;
    }
}
