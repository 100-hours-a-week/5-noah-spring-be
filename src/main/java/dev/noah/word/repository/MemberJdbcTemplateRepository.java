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
public class MemberJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(String imageUrl, String email, String password, String nickname) {
        jdbcTemplate.update("INSERT INTO member (image_url, email, password, nickname) VALUES (?, ?, ?, ?)", imageUrl, email, password, nickname);
    }

    public Optional<Member> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM member WHERE id = ?", memberRowMapper(), id).stream().findFirst();
    }

    public Optional<Member> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM member WHERE email = ?", memberRowMapper(), email).stream().findFirst();
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member", memberRowMapper());
    }

    public void updateImageUrlAndNicknameById(long id, String imageUrl, String nickname) {
        jdbcTemplate.update("UPDATE member SET image_url = ?, nickname = ? WHERE id = ?", imageUrl, nickname, id);
    }

    public void updatePasswordById(long id, String password) {
        jdbcTemplate.update("UPDATE member SET password = ? WHERE id = ?", password, id);
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM member WHERE id = ?", id);
    }

    public boolean existsByEmail(String email) {
        return !jdbcTemplate.query("SELECT 1 FROM member WHERE email = ? LIMIT 1", (rs, rowNum) -> 1, email).isEmpty();
    }

    public boolean existsByNickname(String nickname) {
        return !jdbcTemplate.query("SELECT 1 FROM member WHERE nickname = ? LIMIT 1", (rs, rowNum) -> 1, nickname).isEmpty();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("image_url"), rs.getString("email"), rs.getString("password"), rs.getString("nickname"));
    }
}
