package com.destroY17.configuration;

import org.modelmapper.ModelMapper;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.destroY17")
@EnableWebMvc
@EnableTransactionManagement
public class Config implements WebMvcConfigurer, TransactionManagementConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        try {
            InputStream inputStream = PGSimpleDataSource.class.getResourceAsStream("/db/db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            dataSource.setURL(properties.getProperty("db.URL"));
            dataSource.setUser(properties.getProperty("db.USERNAME"));
            dataSource.setPassword(properties.getProperty("db.PASSWORD"));

            return dataSource;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading database properties: " + e.getMessage());
        }
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
