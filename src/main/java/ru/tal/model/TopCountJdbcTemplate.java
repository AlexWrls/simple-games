package ru.tal.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tal.service.GameType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TopCountJdbcTemplate {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TopCountJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TopCount> getTopCountByGameType(GameType gameType) {
        String query = String.format("SELECT  rg.id, p.email, rg.count from result_game rg " +
                "join profile p on rg.id_profile = p.id WHERE rg.type_game = '%s' " +
                "order by rg.count desc limit 10", gameType.name());
        System.out.println(query);
        return jdbcTemplate.query(query, (rs, rowNum) -> mapTopCount(rs));
    }

    private TopCount mapTopCount(final ResultSet res) {
        try {
            return TopCount.builder()
                    .id(res.getLong("id"))
                    .name(res.getString("email"))
                    .count(res.getInt("count"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
