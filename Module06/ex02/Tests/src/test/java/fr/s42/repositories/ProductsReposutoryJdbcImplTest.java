package fr.s42.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.lang.annotation.Target;
import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Optional;

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

	private final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1L, "Wireless Mouse", new BigDecimal("25.99"));
	private final Product EXPECTED_UPDATED_PRODUCT = new Product(1L, "Wireless Mouse Pro", new BigDecimal("43.66"));

	@BeforeEach
	void init (){
		dataSource = new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.HSQL)
		.addScript("schema.sql")
		.addScript("data.sql")
		.build();

		pJdbcImpl = new ProductsRepositoryJdbcImpl(dataSource);
	}

	//test findAll
	@Test
	void testFindAll(){
		assertEquals(EXPECTED_FIND_ALL_PRODUCTS, pJdbcImpl.findAll());
	}

	// test findById
	@Test
	void testFindByIdTrueId()
	{
		Optional<Product> pr = pJdbcImpl.findById(1L);
		assertTrue(pr.isPresent());
		assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, pr.get());
	}

	@Test
	void testFindByIdFakeId()
	{
		Optional<Product> pr = pJdbcImpl.findById(99L);
		assertTrue(pr.isEmpty());
	}

	// test update
	@Test
	void testUpdate(){
		pJdbcImpl.update(EXPECTED_UPDATED_PRODUCT);
		Optional<Product> pr = pJdbcImpl.findById(1L);
		assertTrue(pr.isPresent());
		assertEquals(EXPECTED_UPDATED_PRODUCT, pr.get());

	}

	@Test 
	void testSave(){
		Product pr = new Product(null, "Google pixel 7", new BigDecimal("9999.99"));

		pJdbcImpl.save(pr);
		assertTrue(pr.getId() != null);
		Optional<Product> productFinded = pJdbcImpl.findById(pr.getId());
		assertTrue(productFinded.isPresent());
		assertEquals(pr, productFinded.get());
	}
}
