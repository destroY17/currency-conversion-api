package com.destroY17.repository;

import com.destroY17.entity.ExchangeRate;
import com.destroY17.repository.rowmapper.ExchangeRateRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ExchangeRateRepository implements CrudRepository<ExchangeRate, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExchangeRateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ExchangeRate> getAll() {
        final String query = """
                SELECT er.id as id,
                bc.id as base_id,
                bc.code as base_code,
                bc.full_name as base_name,
                bc.sign as base_sign,
                tc.id as target_id,
                tc.code as target_code,
                tc.full_name as target_name,
                tc.sign as target_sign,
                er.rate as rate
                FROM exchange_rates er
                JOIN currencies bc ON er.base_currency_id = bc.id
                JOIN currencies tc ON er.target_currency_id = tc.id
                """;
        return jdbcTemplate.query(query, new ExchangeRateRowMapper());
    }

    @Override
    public Optional<ExchangeRate> getById(Integer id) {
        final String query = """
                SELECT er.id as id,
                bc.id as base_id,
                bc.code as base_code,
                bc.full_name as base_name,
                bc.sign as base_sign,
                tc.id as target_id,
                tc.code as target_code,
                tc.full_name as target_name,
                tc.sign as target_sign,
                er.rate as rate
                FROM exchange_rates er
                JOIN currencies bc ON er.base_currency_id = bc.id
                JOIN currencies tc ON er.target_currency_id = tc.id
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, new ExchangeRateRowMapper(), id).stream().findAny();
    }

    public Optional<ExchangeRate> getByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = """
                SELECT er.id as id,
                bc.id as base_id,
                bc.code as base_code,
                bc.full_name as base_name,
                bc.sign as base_sign,
                tc.id as target_id,
                tc.code as target_code,
                tc.full_name as target_name,
                tc.sign as target_sign,
                er.rate as rate
                FROM exchange_rates er
                JOIN currencies bc ON er.base_currency_id = bc.id
                JOIN currencies tc ON er.target_currency_id = tc.id
                WHERE bc.code = ? AND tc.code = ?
                """;
        return jdbcTemplate.query(query, new ExchangeRateRowMapper(), baseCurrencyCode, targetCurrencyCode)
                .stream().findAny();
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        final String query = "INSERT INTO exchange_rates " +
                "(base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        int saveId = jdbcTemplate.update(query, entity.getBaseCurrency().getId(),
                entity.getTargetCurrency().getId(), entity.getRate());
        entity.setId(saveId);
        return entity;
    }

    @Override
    public ExchangeRate update(ExchangeRate entity) {
        final String query = "UPDATE exchange_rates" +
                " SET (base_currency_id, target_currency_id, rate) = (?, ?, ?) WHERE id = ?";
        jdbcTemplate.update(query, entity.getBaseCurrency().getId(),
                entity.getTargetCurrency().getId(), entity.getRate(), entity.getId());
        return entity;
    }
}