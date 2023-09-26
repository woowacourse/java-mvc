package di.stage1.constructorinjection;

import di.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class H2UserRepository implements UserRepository {

    private final JdbcDataSource dataSource;

    public H2UserRepository() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");

        this.dataSource = jdbcDataSource;
    }

    @Override
    public void insert(final User user) {
        try (final Connection connection = dataSource.getConnection()) {
            // 디비에 넣기
        } catch (SQLException ignored) {
        }
    }

    @Override
    public User findById(final long id) {
        try (final Connection connection = dataSource.getConnection()) {
            // 디비에서 찾기
        } catch (SQLException ignored) {
        }

        return null;
    }
}
