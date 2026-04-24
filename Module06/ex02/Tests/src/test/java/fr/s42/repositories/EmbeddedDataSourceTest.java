package fr.s42.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


public class EmbeddedDataSourceTest {
	private DataSource dataSource;

    @BeforeEach
    void init() {
        dataSource = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.addScripts("schema.sql", "data.sql")
			.build();
    }

    @Test 
    void testMethod()
    {
        try (Connection con =  dataSource.getConnection()) 
        {
            assertNotNull(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void testSimpleQuery() throws Exception {
        String QUERY = "SELECT COUNT(*) FROM product";

        try (Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(QUERY);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                assertEquals(5, rs.getInt(1));
            } else {
                fail("No result returned from COUNT(*) query");
            }
        }
    }
}
