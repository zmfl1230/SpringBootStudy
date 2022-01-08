package hello.hellospring.repository;
import hello.hellospring.domain.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

interface JdbcMemberPreparedStatement {
    void makeStatement(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException;
}
