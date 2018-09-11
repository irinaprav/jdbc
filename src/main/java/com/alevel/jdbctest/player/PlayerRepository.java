package com.alevel.jdbctest.player;


import com.alevel.jdbctest.common.Repository;
import com.alevel.jdbctest.common.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PlayerRepository implements Repository<Player, Long> {
    private final Supplier<Connection> connectionSupplier;

    public PlayerRepository(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public void save(Player entity) throws StorageException {
        String name = entity.getNickname();
        long score = entity.getScore();
        String sql = "INSERT INTO players (nickname, score) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE nickname = ?, score = ?";
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setLong(2, score);
            statement.setString(3, name);
            statement.setLong(4, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }

    @Override
    public List<Player> list() throws StorageException {
        String sql = "SELECT players.id id, players.nickname name, ranks.name player_rank, players.score score " +
                "FROM players INNER JOIN ranks " +
                "ON players.score >= lower_t AND players.score < upper_t";
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Player> players = new LinkedList<>();
            while (resultSet.next()) {
                players.add(new Player(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("player_rank"),
                        resultSet.getLong("score")
                ));
            }
            return players;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Player get(Long aLong) throws StorageException {
        String sql = "SELECT players.id id, players.nickname name, ranks.name player_rank, players.score score " +
                "FROM players INNER JOIN ranks " +
                "ON players.score >= lower_t AND players.score < upper_t WHERE players.id = ?";
        Player player = null;
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.first();
            player = new Player(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("player_rank"),
                    resultSet.getLong("score")
            );
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return player;
    }

    @Override
    public void delete(Player entity) throws StorageException {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
