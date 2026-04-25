package fr.s42.repositories;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import fr.s42.models.Product;
import fr.s42.repositories.ProductsRepository;


public class ProductsRepositoryJdbcImpl implements ProductsRepository{

	private final DataSource dataSource;

	public ProductsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	@Override
	public List<Product> findAll(){
		String SQL_QUERY = "SELECT * FROM product ORDER BY id;";
		List<Product> products = new LinkedList<>();
		try (Connection con = dataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_QUERY)) {
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					Product product = new Product();
					product.setId(rs.getLong("id"));
					product.setName(rs.getString("name"));
					product.setPrice(rs.getBigDecimal("price"));
					products.add(product);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return products;
	}
	@Override
	public Optional<Product> findById(Long id) {
		String SQL_QUERY = "SELECT id, name, price FROM product WHERE id = ?";
		try (Connection con = dataSource.getConnection();
	        PreparedStatement ps = con.prepareStatement(SQL_QUERY);
		){
			ps.setLong(1, id);
			try(ResultSet rs = ps.executeQuery()){
				if (rs.next())
					return Optional.of(new Product(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("price")));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	@Override
	public void update(Product product) {
		String SQL_QUERY = "UPDATE product SET name = ?, price = ? WHERE id = ?";

		try (Connection con  = dataSource.getConnection();
		PreparedStatement ps = con.prepareStatement(SQL_QUERY)) {
			ps.setString(1, product.getName());
			ps.setBigDecimal(2, product.getPrice());
			ps.setLong(3, product.getId());

			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void save(Product product)
	{
		String SQL_QUERY = "INSERT INTO product (name , price) VALUES (?, ?);";
		try (Connection con = dataSource.getConnection();
		PreparedStatement ps = con.prepareStatement(SQL_QUERY,  java.sql.Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, product.getName());
			ps.setBigDecimal(2, product.getPrice());
			ps.executeUpdate();
	
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					product.setId(rs.getLong(1));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	 
	}

	@Override
	public void delete(Long id) {		
	}

    
}