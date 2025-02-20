package it.unical.progweb;


import it.unical.progweb.controller.ProdottoController;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Recensione;
import it.unical.progweb.service.ProdottoService;
import it.unical.progweb.service.RecensioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdottoControllerTest {

    @Mock
    private ProdottoService prodottoService;

    @Mock
    private RecensioneService recensioneService;

    @InjectMocks
    private ProdottoController prodottoController;

    @Test
    public void testGetAllProducts() {
        List<Prodotto> mockProducts = Arrays.asList(
                new Prodotto(1, "Scarpe", "Nike", "Rosso", 100, "Scarpe running", false, "url", 1),
                new Prodotto(2, "Maglia", "Adidas", "Blu", 50, "Maglia calcio", false, "url", 2)
        );

        when(prodottoService.getAllProducts()).thenReturn(mockProducts);

        ResponseEntity<List<Prodotto>> response = prodottoController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetProductById() {
        Prodotto mockProduct = new Prodotto(1, "Scarpe", "Nike", "Rosso", 100, "Scarpe running", false, "url", 1);
        List<Recensione> mockReviews = Collections.emptyList();

        when(prodottoService.getProductById(1)).thenReturn(mockProduct);
        when(recensioneService.getProductReviews(1)).thenReturn(mockReviews);

        ResponseEntity<?> response = prodottoController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProduct() {
        Prodotto newProduct = new Prodotto(3, "Pantaloni", "Puma", "Nero", 70, "Pantaloni sport", false, "url", 3);

        ResponseEntity<?> response = prodottoController.addProduct(newProduct);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(prodottoService).addProduct(newProduct);
    }
}