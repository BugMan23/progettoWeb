package it.unical.progweb;

import it.unical.progweb.controller.UtenteController;
import it.unical.progweb.model.Utente;
import it.unical.progweb.model.request.LoginRequest;
import it.unical.progweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtenteControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UtenteController utenteController;

    @Test
    public void testRegistrazioneUtente() {
        Utente utente = new Utente();
        utente.setEmail("test@example.com");
        utente.setPassword("password");

        ResponseEntity<?> response = utenteController.registerUser(utente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).registraUtente(utente);
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Utente utente = new Utente();
        utente.setId(1);
        utente.setRuolo(false);

        when(userService.login(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenReturn(utente);

        ResponseEntity<?> response = utenteController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
