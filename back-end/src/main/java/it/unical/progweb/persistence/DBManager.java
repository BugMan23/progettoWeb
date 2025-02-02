package it.unical.progweb.persistence;

import java.sql.*;

import it.unical.progweb.model.Ordine;
import it.unical.progweb.persistence.dao.*;
import it.unical.progweb.persistence.db.*;

public class DBManager {
    private static DBManager instance = null;
    private UtenteDAO userDAO = null;
    private ProdottoDAO productDAO = null;
    private OrdineDAO orderDAO = null;
    private CarrelloDAO cartDAO = null;
    private RecensioneDAO reviewDAO = null;
    private IndirizzoDAO addressDAO = null;
    private MetodoDiPagamentoDAO paymentMethodDAO = null;

    private static final String URL = "jdbc:postgresql://localhost:5432/eCommerce";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sofia";

    private static Connection con = null;

    public static Connection getConnection(){
        if (con == null){
            try {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to the database", e);
            }
        }
        return con;
    }

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    public UtenteDAO getUserDAO(){
        if (userDAO == null) {
            userDAO = new UtenteDAOJDBC(getConnection());
        }
        return userDAO;
    }

    public ProdottoDAO getProductDAO() {
        if (productDAO == null) {
            productDAO = new ProdottoDAOJDBC(getConnection());
        }
        return productDAO;
    }

    public OrdineDAO getOrderDAO() {
        if (orderDAO == null) {
            orderDAO = new OrdineDAOJDBC(getConnection());
        }
        return orderDAO;
    }

    public CarrelloDAO getCartDAO() {
        if (cartDAO == null) {
            cartDAO = new CarrelloDAOJDBC(getConnection());
        }
        return cartDAO;
    }

    public RecensioneDAO getReviewDAO() {
        if (reviewDAO == null) {
            reviewDAO = new RecensioneDAOJDBC(getConnection());
        }
        return reviewDAO;
    }

    public IndirizzoDAO getAddressDAO() {
        if (addressDAO == null) {
            addressDAO = new IndirizzoDAOJDBC(getConnection());
        }
        return addressDAO;
    }

    public MetodoDiPagamentoDAO getPaymentMethodDAO() {
        if (paymentMethodDAO == null) {
            paymentMethodDAO = new MetodoDiPagamentoDAOJDBC(getConnection());
        }
        return paymentMethodDAO;
    }
}