package dev.noah.word.repository;

import dev.noah.word.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(String imageUrl, String email, String password, String nickname) {
        jdbcTemplate.update("INSERT INTO Member (imageUrl, email, password, nickname) VALUES (?, ?, ?, ?)", imageUrl, email, password, nickname);
    }

    public Optional<Member> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM Member WHERE id = ?", memberRowMapper(), id).stream().findFirst();
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return jdbcTemplate.query("SELECT * FROM Member WHERE email = ? and password = ?", memberRowMapper(), email, password).stream().findFirst();
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM Member", memberRowMapper());
    }

    public void updateImageUrlAndNicknameById(long id, String imageUrl, String nickname) {
        jdbcTemplate.update("UPDATE Member SET imageUrl = ?, nickname = ? WHERE id = ?", imageUrl, nickname, id);
    }

    public void updatePasswordById(long id, String password) {
        jdbcTemplate.update("UPDATE Member SET password = ? WHERE id = ?", password, id);
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM Member WHERE id = ?", id);
    }

    public boolean existsByEmail(String email) {
        return !jdbcTemplate.query("SELECT 1 FROM Member WHERE email = ? LIMIT 1", (rs, rowNum) -> 1, email).isEmpty();
    }

    public boolean existsByNickname(String nickname) {
        return !jdbcTemplate.query("SELECT 1 FROM Member WHERE nickname = ? LIMIT 1", (rs, rowNum) -> 1, nickname).isEmpty();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("imageUrl"), rs.getString("email"), rs.getString("password"), rs.getString("nickname"));
    }
}
