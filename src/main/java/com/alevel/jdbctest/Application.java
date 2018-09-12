package com.alevel.jdbctest;

import com.alevel.jdbctest.common.SingleConnectionPool;
import com.alevel.jdbctest.common.StorageException;
import com.alevel.jdbctest.player.Player;
import com.alevel.jdbctest.player.PlayerRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Supplier;

public class Application {
    public static void main(String[] args) {
        Properties connectionProps = new Properties(); //object of our properties

        try (InputStream props = Application.class.getResourceAsStream("/datasource.properties")) { // slash vazhen potomu chto iz korne classpath
            connectionProps.load(props);
        } catch (IOException e) {
            panic(e);
        }

        String url = connectionProps.getProperty("url");
        try (Connection connection = DriverManager.getConnection(url, connectionProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ranks")) {
                ResultSet resultSet = preparedStatement.executeQuery(); //ne nado v try s resourse but closeable
                while (resultSet.next()) { //-1 firstly
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    long lower_trashehold = resultSet.getLong("lower_t");
                    long upper_trashehold = resultSet.getLong("upper_t");
                    System.out.printf("id: %d, name: %s, lower_t: %d, upper_t: %d \n", id, name, lower_trashehold, upper_trashehold);
                }
            }
        } catch (SQLException e) {
            panic(e);
        }

        try (Connection connection = DriverManager.getConnection(url, connectionProps)) {
            Supplier<Connection> connectionSupplier = new SingleConnectionPool(connection);
            PlayerRepository playerRepository = new PlayerRepository(connectionSupplier);
           /* for (String playerName : inputPlayerNames()) {
                playerRepository.save(new Player(playerName));
            }
            for (Player player : playerRepository.list()) {
                System.out.println(player);
            }*/
            Player player = playerRepository.get(new Long(2));
            System.out.println(player.toString());
        } catch (SQLException | StorageException e) {
            panic(e);
        }
    }

    private static List<String> inputPlayerNames() {
        System.out.println("Input player names:");
        Scanner scanner = new Scanner(System.in);
        List<String> names = new LinkedList<>();
        String name;
        while (!(name = scanner.nextLine()).isEmpty()) {
            names.add(name);
        }
        return names;
    }

    private static void panic(Throwable e) {
        e.printStackTrace();
        System.exit(1);
    }
}
