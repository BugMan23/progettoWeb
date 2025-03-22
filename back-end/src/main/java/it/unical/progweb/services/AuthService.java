package it.unical.progweb.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.proxy.UtenteProxy;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    private final UtenteProxy utenteProxy;
    private final Key secretKey;

    public AuthService(UtenteProxy utenteProxy) {
        this.utenteProxy = utenteProxy;
        // Genera la chiave al momento della creazione del servizio
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        int keySizeBits = secretKey.getEncoded().length * 8;
        System.out.println("✅ AuthService creato con successo! Key size = " + keySizeBits + " bits");
    }

    public String login(String email, String password) {
        Utente utente = utenteProxy.validateUser(email, password);
        if (utente != null) {
            return generateToken(utente);
        }
        return null;
    }

    public boolean register(Utente utente) {
        return utenteProxy.save(utente);
    }

    public String generateToken(Utente utente) {
        return Jwts.builder()
                .setSubject(utente.getEmail())
                .claim("role", utente.getRuolo() ? "admin" : "user")
                .claim("userId", utente.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey)
                .compact();
    }
    public Utente loginAndGetUser(String email, String password) {
        return utenteProxy.validateUser(email, password);
    }

}