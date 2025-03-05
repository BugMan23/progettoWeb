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
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public AuthService(UtenteProxy utenteProxy) {
        this.utenteProxy = utenteProxy;
        int keySizeBits = KEY.getEncoded().length * 8;
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

    private String generateToken(Utente utente) {
        return Jwts.builder()
                .setSubject(utente.getEmail())
                .claim("role", utente.getRuolo() ? "admin" : "user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 ora di validità
                .signWith(KEY)
                .compact();
    }
}
