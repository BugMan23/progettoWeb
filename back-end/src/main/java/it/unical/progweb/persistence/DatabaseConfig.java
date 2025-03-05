package it.unical.progweb.persistence;

import it.unical.progweb.persistence.dao.UtenteDAO;
import it.unical.progweb.persistence.db.UtenteDAOJDBC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriver;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceDriver);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);

        DBConn.setDataSource(dataSource);

        return dataSource;
    }

    @Bean
    public UtenteDAO utenteDAO(DataSource dataSource) {
        System.out.println("✅ UtenteDAOJDBC registrato come Bean!");
        return new UtenteDAOJDBC(dataSource);
    }
}
