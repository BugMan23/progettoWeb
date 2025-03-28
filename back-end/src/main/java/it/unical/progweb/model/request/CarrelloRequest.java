package it.unical.progweb.model.request;

public class CarrelloRequest {
    private int userId;
    private int productId;
    private int quantity;
    private String taglia;

    // Costruttore vuoto per il deserializzatore JSON
    public CarrelloRequest() {
    }

    // Costruttore con tutti i parametri
    public CarrelloRequest(int userId, int productId, int quantity, String taglia) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.taglia = taglia;
    }

    // Getter e setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }
}