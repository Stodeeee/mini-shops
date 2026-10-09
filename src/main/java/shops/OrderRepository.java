package shops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public Order findById(int id){
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:shop.db");
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders WHERE id = ?"))
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Order(
                        rs.getInt("id"),
                        rs.getString("customer_email"),
                        rs.getString("status"),
                        rs.getInt("total_cents"),
                        rs.getString("created_at"));
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderEvent> findEvents(int orderId){
        List<OrderEvent> orderEvent = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:shop.db");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM order_events WHERE order_id = ?"))
             {
                preparedStatement.setInt(1, orderId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    orderEvent.add(new OrderEvent(
                        resultSet.getString("message"),
                        resultSet.getString("created_at")));
                }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
            return orderEvent;
        }

    public int createOrder(String email, List<CartLine> lines) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:shop.db");
             PreparedStatement orderPs = connection.prepareStatement(
                     "INSERT INTO orders (customer_email, total_cents) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS);
             PreparedStatement itemPs = connection.prepareStatement(
                     "INSERT INTO order_items (order_id, product_id, qty, price_cents) VALUES (?, ?, ?, ?)");
             PreparedStatement eventPs = connection.prepareStatement(
                     "INSERT INTO order_events (order_id, message) VALUES (?, ?)");
                PreparedStatement reservePs = connection.prepareStatement(
                     "UPDATE products SET stock_available = stock_available - ?, " +
                     "stock_reserved = stock_reserved + ? " +
                     "WHERE id = ? AND stock_available >= ?")) {

            connection.setAutoCommit(false);
            try {
                int totalCents = 0;
                for (CartLine line : lines) {
                    totalCents += line.lineTotal();
                }

                orderPs.setString(1, email);
                orderPs.setInt(2, totalCents);
                orderPs.executeUpdate();

                int orderId;
                try (ResultSet rs = orderPs.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("База не вернула номер заказа");
                    }
                    orderId = rs.getInt(1);
                }

                for (CartLine line : lines) {

                    reservePs.setInt(1, line.qty());
                    reservePs.setInt(2, line.qty());
                    reservePs.setInt(3, line.product().id());
                    reservePs.setInt(4, line.qty());
                    if (reservePs.executeUpdate() == 0) {
                        throw new OutOfStockException(line.product().name());
                    }

                    itemPs.setInt(1, orderId);
                    itemPs.setInt(2, line.product().id());
                    itemPs.setInt(3, line.qty());
                    itemPs.setInt(4, line.product().priceCents());
                    itemPs.executeUpdate();
                }

                eventPs.setInt(1, orderId);
                eventPs.setString(2, "Заказ создан");
                eventPs.executeUpdate();

                eventPs.setInt(1, orderId);
                eventPs.setString(2, "Товар зарезервирован");
                eventPs.executeUpdate();

                connection.commit();
                return orderId;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать заказ", e);
        }
    }
}
