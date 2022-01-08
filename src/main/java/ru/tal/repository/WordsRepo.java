package ru.tal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tal.entity.Words;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WordsRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WordsRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public int max() {
        return namedParameterJdbcTemplate.queryForObject("SELECT count(*) FROM public.words", new MapSqlParameterSource(), Integer.class);
    }

    public List<Words> getRandomWords() {
        int random = (int) ((Math.random() * ((max() - 1000) - 1)) + 1);
        String query = String.format("SELECT w.id,w.word,w.translate FROM public.words as w " +
                "WHERE w.id >= %s limit 1000", random);
        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> map(rs));
    }

    @Transactional
    public void insertListWords(final List<Words> wordsList) {

        String query = "INSERT INTO public.words (word,translate) "
                + "VALUES (?,?)";

        this.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                Words words = wordsList.get(i);
                ps.setString(1, words.getWord());
                ps.setString(2, words.getTranslate());
            }

            @Override
            public int getBatchSize() {
                return wordsList.size();
            }
        });
    }

    public Words map(ResultSet res) {
        try {
            Words words = new Words();
            words.setId(res.getLong("id"));
            words.setWord(res.getString("word"));
            words.setTranslate(res.getString("translate"));
            return words;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
