package com.microserviceProject.productservice;

import com.microserviceProject.productservice.dto.ProductRequest;
import com.microserviceProject.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
			.withDatabaseName("product_service")
			.withUsername("postgres")
			.withPassword("ramzi")
			.withExposedPorts(5432);
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url",
				() -> String.format("jdbc:postgresql://localhost:%d/product_service", postgres.getFirstMappedPort()));
		dynamicPropertyRegistry.add("spring.datasource.username", () -> "postgres");
		dynamicPropertyRegistry.add("spring.datasource.password", () -> "ramzi");
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("IPhone 15")
				.description("IPhone 15")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
