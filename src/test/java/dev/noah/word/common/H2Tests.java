package dev.noah.word.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@JdbcTest
public class H2Tests {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void should_pass_when_useDataSource() {
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_pass_when_getConnection() {
        Assertions.assertNotNull(dataSource);

        try {
            Assertions.assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void should_pass_when_useJdbcTemplate() {
        Assertions.assertNotNull(jdbcTemplate);
    }

    @Test
    void should_pass_when_useQueryWithJdbcTemplate() {
        Assertions.assertNotNull(jdbcTemplate);

        Integer i = jdbcTemplate.queryForObject("select 1", Integer.class);

        Assertions.assertNotNull(i);
        Assertions.assertEquals(1, i);
    }
}
