package ru.tal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tal.entity.ResultGame;
import ru.tal.service.GameType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ResultGameRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ResultGameRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public ResultGame getByGameTypeAndIdProfile(GameType gameType, long idProfile) {
        try {
            String query = String.format("SELECT rg.id,rg.id_profile,rg.type_game,rg.count FROM public.result_game as rg " +
                    "WHERE rg.type_game like '%s' and rg.id_profile = '%s'", gameType.name(), idProfile);
            List<ResultGame> queryRes = jdbcTemplate.query(query, (rs, rowNum) -> map(rs));
            return queryRes.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    private ResultGame map(ResultSet res) {
        try {
            ResultGame resultGame = new ResultGame();
            resultGame.setId(res.getLong("id"));
            resultGame.setCount(res.getInt("count"));
            resultGame.setIdProfile(res.getLong("id_profile"));
            resultGame.setGameType(GameType.valueOf(res.getString("type_game")));
            return resultGame;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Transactional
    public void save(ResultGame resultGame) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", resultGame.getCount())
                .addValue("id_profile", resultGame.getIdProfile())
                .addValue("type_game", resultGame.getGameType().name());
        String query = "INSERT INTO public.result_game ( id_profile, type_game, count) VALUES (:id_profile, :type_game, :count)";
        jdbcTemplate.update(query, params);
    }
}
