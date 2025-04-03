package it.unical.progweb.model.request;

public class CarrelloRequest {
    private int userId;
    private int productId;
    private int quantity;
    private String taglia;

    public CarrelloRequest() {
    }

    public CarrelloRequest(int userId, int productId, int quantity, String taglia) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.taglia = taglia;
    }

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