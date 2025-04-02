package it.unical.progweb.config;


import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import it.unical.progweb.utility.JwtAuthFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.security.Key;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final Key secretKey;

    public WebSecurityConfig(Key secretKey) {
        this.secretKey = secretKey;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/api/utenti/registrazione").permitAll()

                        // Rotte pubbliche
                        .requestMatchers("/api/prodotti/**").permitAll()
                        .requestMatchers("/api/categorie/**").permitAll()
                        .requestMatchers("/api/disponibilita/**").permitAll()
                        .requestMatchers("/api/indirizzi/**").permitAll()
                        .requestMatchers("/api/metodipagamento/**").permitAll()
                        .requestMatchers("/api/carrello/**").permitAll()
                        .requestMatchers("/api/ordini/**").permitAll()
                        .requestMatchers("/api/reviews/**").permitAll()
                        .requestMatchers("/api/utenti/profile/**").permitAll()

                        // Endpoint protetto solo per admin
                        .requestMatchers("/api/utenti/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/utenti/admin/**").hasRole("ADMIN")

                        // Tutto il resto richiede autenticazione
                        .anyRequest().authenticated()
                )
                // Aggiungi il filtro JWT prima dell'autenticazione standard
                .addFilterBefore(new JwtAuthFilter(secretKey), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // ✅ PATCH aggiunto
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}