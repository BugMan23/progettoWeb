package it.unical.progweb.persistence;

import it.unical.progweb.persistence.dao.*;
import it.unical.progweb.persistence.db.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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

        DbConn.setDataSource(dataSource);

        return dataSource;
    }

    @Bean
    public UtenteDAO utenteDAO(DataSource dataSource) {
        System.out.println("✅ UtenteDAOJDBC registrato come Bean!");
        return new UtenteDAOJDBC(dataSource);
    }

    @Bean
    public ProdottoDAO prodottoDAO(DataSource dataSource) {
        System.out.println("✅ ProdottoDAOJDBC registrato come Bean!");
        return new ProdottoDAOJDBC(dataSource);
    }

    @Bean
    public CarrelloDAO carrelloDAO(DataSource dataSource) {
        System.out.println("✅ CarrelloDAOJDBC registrato come Bean!");
        return new CarrelloDAOJDBC(dataSource);
    }

    @Bean
    public CategoriaDAO categoriaDAO(DataSource dataSource) {
        System.out.println("✅ CategoriaDAOJDBC registrato come Bean!");
        return new CategoriaDAOJDBC(dataSource);
    }

    @Bean
    public DettagliOrdineDAO dettagliOrdineDAO(DataSource dataSource) {
        System.out.println("✅ DettagliOrdineDAOJDBC registrato come Bean!");
        return new DettagliOrdineDAOJDBC(dataSource);
    }

    @Bean
    public DisponibilitaDAO disponibilitaDAO(DataSource dataSource) {
        System.out.println("✅ DisponibilitaDAOJDBC registrato come Bean!");
        return new DisponibilitaDAOJDBC(dataSource);
    }

    @Bean
    public IndirizzoDAO indirizzoDAO(DataSource dataSource) {
        System.out.println("✅ IndirizzoDAOJDBC registrato come Bean!");
        return new IndirizzoDAOJDBC(dataSource);
    }

    @Bean
    public MetodoDiPagamentoDAO metodoDiPagamentoDAO(DataSource dataSource) {
        System.out.println("✅ MetodoDiPagamentoDAOJDBC registrato come Bean!");
        return new MetodoDiPagamentoDAOJDBC(dataSource);
    }

    @Bean
    public OrdineDAO ordineDAO(DataSource dataSource) {
        System.out.println("✅ OrdineDAOJDBC registrato come Bean!");
        return new OrdineDAOJDBC(dataSource);
    }

    @Bean
    public RecensioneDAO recensioneDAO(DataSource dataSource) {
        System.out.println("✅ RecensioneDAOJDBC registrato come Bean!");
        return new RecensioneDAOJDBC(dataSource);
    }
}