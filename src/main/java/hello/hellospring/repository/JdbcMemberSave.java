package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.sql.*;

public class JdbcMemberSave implements JdbcMemberPreparedStatement {
    private String name;

    public JdbcMemberSave(String name) {
        this.name = name;
    }

    @Override
    public Member makeStatement(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        String insertSQL = "insert into member(name) values(?)";
        preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        resultSet = preparedStatement.getGeneratedKeys();

        if (resultSet.next()) {
            Member member = new Member();
            member.setId(resultSet.getLong("id"));
            member.setName(name);
            return member;
        }
        return null;

    }
}
