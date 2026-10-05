package shops;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductRepository {
    public List<Product> findAll(){
        List<Product> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:shop.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM products ORDER BY id");){

            while (resultSet.next()) {
                result.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price_cents")));
            }
        } catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return result;
    }
}
