package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {
  public static Connection connect() {
    String db_file = "jdbc:sqlite:resources/bank.db";
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(db_file);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return connection;
  }

  public static Customer getCustomer(String username) {
    String sql = "SELECT * FROM customers WHERE username = ?";
    Customer customer = null;

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      try (ResultSet rs = pstmt.executeQuery()) {
        customer = new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getInt("account_id"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return customer;

  }

  public static Account getAccount(int accountId) {
    String sql = "SELECT * FROM accounts WHERE id = ?";
    Account account = null;

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, accountId);
      try (ResultSet rs = pstmt.executeQuery()) {
        account = new Account(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getDouble("balance"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return account;
  }

  public static void updateAccountBalance(int accountId, double newBalance) {
    String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

    try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setDouble(1, newBalance);
      pstmt.setInt(2, accountId);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
