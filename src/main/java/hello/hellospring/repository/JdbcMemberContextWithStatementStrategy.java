package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcMemberContextWithStatementStrategy {

    private final DataSource dataSource;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public JdbcMemberContextWithStatementStrategy(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcMemberContext(JdbcMemberPreparedStatement jdbcMemberPreparedStatement) {
        try{
            connection = dataSource.getConnection();
            jdbcMemberPreparedStatement.makeStatement(connection, preparedStatement, resultSet);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
