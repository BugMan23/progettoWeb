package it.unical.progweb.utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final Key secretKey;

    public JwtAuthFilter(Key secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔎 Header Authorization: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("🟢 Token ricevuto: " + token);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                String role = claims.get("role", String.class); // "admin" o "user"
                Integer userId = claims.get("userId", Integer.class);

                System.out.println("✅ JWT valido:");
                System.out.println("   👤 Email: " + email);
                System.out.println("   🧩 Ruolo: " + role);
                System.out.println("   🆔 ID utente: " + userId);


                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                        role.equalsIgnoreCase("admin") ? "ROLE_ADMIN" : "ROLE_USER"
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("🔐 Autenticazione salvata nel SecurityContext con ruolo: " + authority.getAuthority());

                System.out.println("🔐 Ruolo ricevuto nel token: " + role);
                System.out.println("👉 Authority costruita: " + authority.getAuthority());

            } catch (Exception e) {
                System.out.println("❌ JWT invalido: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Nessun header Authorization valido presente");
        }

        filterChain.doFilter(request, response);
    }
}