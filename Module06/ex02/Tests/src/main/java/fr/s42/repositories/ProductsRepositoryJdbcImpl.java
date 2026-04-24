package fr.s42.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import fr.s42.models.Product;

public class ProductsRepositoryJdbcImpl implements ProductsRepository{

	private final DataSource dataSource;

	public ProductsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	@Override
	public List<Product> findAll(){
		return new LinkedList<>();
	}
	@Override
	public Optional<Product> findById(Long id) {
		return Optional.empty();
	}
	@Override
	public void update(Product product) {
		
	}
	@Override
	public void save(Product product){

	}
	@Override
	public void delete(Long id) {		
	}

    
}