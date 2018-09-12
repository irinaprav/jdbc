package com.alevel.jdbctest.game;

import com.alevel.jdbctest.common.Repository;
import com.alevel.jdbctest.common.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class GameRepository implements Repository<Game, Long> {

    private final Supplier<Connection> connectionSupplier;

    public GameRepository(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }


    @Override
    public void save(Game entity) throws StorageException {
        Long score = entity.getScore();
        Long winner_id = entity.getWinner_id();
        String sql = "INSERT INTO games (mvp_id,score) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE  mvp_id = ?, score = ?";

        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            statement.setLong(1, winner_id);
            statement.setLong(2, score);
            statement.setLong(3, winner_id);
            statement.setLong(4, score);
            statement.executeUpdate();
            String sqlQuery = "INSERT INTO participiants (player_id,game_id) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE  player_id = ?, game_id = ?";
            try (PreparedStatement statement1 = connectionSupplier.get().prepareStatement(sqlQuery)) {
                for (Long player : entity.getPlayers()) {
                    statement1.setLong(1, player);
                    statement1.setLong(2, entity.getId());
                    statement1.setLong(3, player);
                    statement1.setLong(4, entity.getId());
                    statement1.executeUpdate();
                }
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Game> list() throws StorageException {
        String sql = "SELECT games.id id, games.mvp_id mvp_id, participiants.player_id player_id , games.score score " +
                "FROM games INNER JOIN participiants " +
                "ON participiants.game_id = games.id";
        List<Game> games = new LinkedList<>();
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            long currectId = 0;
            Game game = null;
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long player_id = resultSet.getLong("player_id");
                if (currectId == id) {
                    game.getPlayers().add(player_id);
                } else {
                    currectId = id;
                    Long winnerId = resultSet.getLong("mvp_id");
                    Long score = resultSet.getLong("score");
                    game = new Game(id, winnerId, score, player_id);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return games;
    }

    @Override
    public Game get(Long aLong) throws StorageException {
        String sql = "SELECT games.id id, games.mvp_id mvp_id, participiants.player_id player_id , games.score score " +
                "FROM games INNER JOIN participiants " +
                "ON participiants.game_id = games.id WHERE games.id = ?";
        Game game = null;
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            statement.setLong(1, aLong);
            long currectId = 0;
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long player_id = resultSet.getLong("player_id");
                if (currectId == id) {
                    game.getPlayers().add(player_id);
                } else {
                    currectId = id;
                    Long winnerId = resultSet.getLong("mvp_id");
                    Long score = resultSet.getLong("score");
                    game = new Game(id, winnerId, score, player_id);
                }
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return game;
    }

    @Override
    public void delete(Game entity) throws StorageException {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement statement = connectionSupplier.get().prepareStatement(sql)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
