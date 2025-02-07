package it.unical.progweb.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.UtenteDAO;
import java.util.Date;

public class AuthService {
    private final UtenteDAO utenteDAO;
    private final String SECRET_KEY = "MiaChiaveSegreta"; // Usare una chiave più sicura in produzione


    public AuthService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /*public String login(String email, String password) {
        Utente utente = utenteDAO.validateUser(email, password);
        if (utente != null) {
            return generateToken(utente);
        }
        return null; // Login fallito
    }*/

    /*public boolean register(Utente utente) {
        return utenteDAO.save(utente);
    }
*/
    private String generateToken(Utente utente) {
        return Jwts.builder()
                .setSubject(utente.getEmail())
                .claim("role", utente.getRuolo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 ora di validità
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
