package fr.s42.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import fr.s42.models.Product;

public class ProductsReposutoryJdbcImplTest {
	private  DataSource dataSource;
	private  ProductsRepositoryJdbcImpl pJdbcImpl;
	private final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
    new Product(1L, "Wireless Mouse", new BigDecimal("25.99")),
    new Product(2L, "Mechanical Keyboard", new BigDecimal("79.50")),
    new Product(3L, "USB-C Hub", new BigDecimal("45.00")),
    new Product(4L, "Gaming Monitor", new BigDecimal("299.99")),
    new Product(5L, "Noise Cancelling Headphones", new BigDecimal("150.25"))
);

	@BeforeEach
	void init (){
		dataSource = new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.HSQL)
		.addScript("schema.sql")
		.addScript("data.sql")
		.build();

		pJdbcImpl = new ProductsRepositoryJdbcImpl(dataSource);
	}
	@Test
	void testFindAll(){
		assertEquals(EXPECTED_FIND_ALL_PRODUCTS, pJdbcImpl.findAll());
	}
}
