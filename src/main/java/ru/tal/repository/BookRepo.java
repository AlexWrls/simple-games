package ru.tal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tal.entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BookRepo {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveListBook(List<Book> books, Long idProfile) {
        String query = "INSERT INTO public.book (id_profile, word, translate) " +
                "VALUES (?,?,?)";
        this.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Book book = books.get(i);
                ps.setLong(1, idProfile);
                ps.setString(2, book.getWord());
                ps.setString(3, book.getTranslate());
            }

            @Override
            public int getBatchSize() {
                return books.size();
            }
        });
    }

    public List<Book> getAllByIdProfile(long idProfile) {
        String query = String.format("SELECT b.id,b.id_profile,b.word,b.translate FROM public.book as b WHERE b.id_profile = '%s'", idProfile);
        return jdbcTemplate.query(query, ((rs, row) -> map(rs)));
    }

    public Integer count(long idProfile){
        String query = String.format("SELECT count(*) FROM public.book as b WHERE b.id_profile = '%s'",idProfile);
        return  jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Book map(ResultSet res) {
        try {
            Book book = new Book();
            book.setId(res.getLong("id"));
            book.setIdProfile(res.getLong("id_profile"));
            book.setWord(res.getString("word"));
            book.setTranslate(res.getString("translate"));
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
