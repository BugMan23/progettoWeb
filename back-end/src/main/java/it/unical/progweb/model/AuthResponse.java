package it.unical.progweb.model;

class AuthResponse {
    private int id;
    private String nome;
    private boolean isAdmin;
    private String token;

    public AuthResponse(int id, String nome, boolean isAdmin, String token) {
        this.id = id;
        this.nome = nome;
        this.isAdmin = isAdmin;
        this.token = token;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public boolean isAdmin() { return isAdmin; }
    public String getToken() { return token; }
}
