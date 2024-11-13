package co.edu.cue.pruebasConActions;

import co.edu.cue.pruebasConActions.controller.ProductController;
import co.edu.cue.pruebasConActions.domain.entities.Products;
import co.edu.cue.pruebasConActions.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Products product;

    @BeforeEach
    public void setup() {
        product = new Products(1L, "PC", 300.00);
    }

    // Test for creating a product
    @Test
    public void testCreateProduct() throws Exception {
        String productJson = "{\"id\":1,\"name\":\"Product 1\",\"price\":100.0}";

        when(productService.createProduct(any()))
                .thenReturn(new Products(2L, "Product 1", 100.0));

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.productPrice").value(100.0));

        verify(productService, times(1)).createProduct(any());
    }

    // Test for getting a product by ID
    @Test
    public void testGetProductById() throws Exception {
        Products product = new Products(2L, "Product 1", 100.0);

        when(productService.getProductById(anyLong())).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.productPrice").value(100.0));

        verify(productService, times(1)).getProductById(anyLong());
    }

    // Test for updating a product
    @Test
    public void testUpdateProduct() throws Exception {
        String updatedProductJson = "{\"id\":1,\"name\":\"Updated Product\",\"price\":150.0}";
        Products updatedProduct = new Products(1L, "Updated Product", 150.0);

        when(productService.updateProduct(anyLong(), any())).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                        .contentType("application/json")
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Updated Product"))
                .andExpect(jsonPath("$.productPrice").value(150.0));

        verify(productService, times(1)).updateProduct(anyLong(), any());
    }

    // Test for deleting a product
    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(anyLong());
    }

    // Test for calculating the total price for products
    @Test
    public void testCalculateTotalPriceForProducts() throws Exception {
        String productQuantitiesJson = "{\"1\":2,\"2\":3}";

        when(productService.calculateTotalPriceForProducts(any(), any())).thenReturn(500.0);

        mockMvc.perform(post("/products/calculate-total")
                        .contentType("application/json")
                        .content(productQuantitiesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500.0));

        verify(productService, times(1)).calculateTotalPriceForProducts(any(), any());
    }
}