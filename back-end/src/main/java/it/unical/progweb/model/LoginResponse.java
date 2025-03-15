package it.unical.progweb.model;

class LoginResponse {
    private int id;
    private String nome;
    private boolean isAdmin;

    public LoginResponse(int id, String nome, boolean isAdmin) {
        this.id = id;
        this.nome = nome;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}