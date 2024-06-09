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
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM Member WHERE id = ?", memberRowMapper(), id));
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM Member WHERE email = ? and password = ?", memberRowMapper(), email, password));
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
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT 1 FROM Member WHERE email = ? LIMIT 1", Boolean.class, email));
    }

    public boolean existsByNickname(String nickname) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT 1 FROM Member WHERE nickname = ? LIMIT 1", Boolean.class, nickname));
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("imageUrl"), rs.getString("email"), rs.getString("password"), rs.getString("nickname"));
    }
}
