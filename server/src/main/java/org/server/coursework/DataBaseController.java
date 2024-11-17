package org.server.coursework;

import java.sql.*;

public class DataBaseController {

    private static final String URL = "jdbc:sqlite:src/main/resources/org/server/coursework/database/data.db";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Не вдалося підключитися до бази даних: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Не вдалося закрити з'єднання: " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ip TEXT NOT NULL," +
                "username TEXT NOT NULL" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Помилка при створенні таблиці: " + e.getMessage());
        }
    }

    public void addUserByIp(String ip) {
        String checkSql = "SELECT COUNT(*) FROM users WHERE ip = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, ip);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("User with IP " + ip + "already exists");
            } else {
                String insertSql = "INSERT INTO users (ip, username) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, ip);
                    insertStmt.setString(2, "none");
                    insertStmt.executeUpdate();
                    System.out.println("Користувач з IP " + ip + " доданий з ім'ям 'none'.");
                } catch (SQLException e) {
                    System.err.println("Помилка при додаванні користувача: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при перевірці наявності користувача: " + e.getMessage());
        }
    }

    public String getUsernameByIp(String ip) {
        String sql = "SELECT username FROM users WHERE ip = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні користувача за IP: " + e.getMessage());
            return null;
        }
    }

    public void updateUsernameByIp(String ip, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE ip = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, ip);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Ім'я користувача успішно оновлено!");
            } else {
                System.out.println("Не знайдено користувача для оновлення.");
            }
        } catch (SQLException e) {
            System.err.println("Помилка при оновленні імені користувача за IP: " + e.getMessage());
        }
    }
}
