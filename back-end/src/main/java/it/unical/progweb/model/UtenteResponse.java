package it.unical.progweb.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtenteResponse {
    public int id;
    public String nome;
    public String cognome;
    public String email;
    public String telefono;
    public String username;
    public String password;
    public Boolean ruolo;
}
