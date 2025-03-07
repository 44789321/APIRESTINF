package com.infomedia.rest.ApiRest.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySource("file:/C:/Users/Consultor/Desktop/Infomedia/db2.config")
public class DbConfig {

    @Value("${db2.url}")
    private String dbUrl;

    @Value("${db2.username}")
    private String dbUsername;

    @Value("${db2.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() throws SQLException{
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}
