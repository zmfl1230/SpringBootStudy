package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository{

    private final JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    /*
     * 클래스 레벨에서 구체적인 의존관계가 그려지고 있다.(인터페이스를 두지 않고, 클래스 오브젝트를 주입했기 때문)
     * 인터페이스를 둬 클래스간 느슨한 의존관계를 나타내고 있지 않았다는 것에 온전한 DI라고 볼 수 없을 순 있으나,
     * DI는 넓게 보면 오브젝트의 생성과 관계 설정을 오브젝트에서 제거하고, 이 역할을 제 3자(스프링 컨테이너)에게 위임한다는 IOC 개념을 포괄하고 있다.
     * 이런 개념하에. jdbcMemberContextWithStatementStrategy을 JdbcMemberRepository에서 사용할 수 있도록 주입했다는 것은 DI의 원리를 따른다고 볼 수 있다.
     * */

    public JdbcMemberRepository(JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy) {
        this.jdbcMemberContextWithStatementStrategy = jdbcMemberContextWithStatementStrategy;
    }

    @Override
    public Member save(String name){
        return jdbcMemberContextWithStatementStrategy.jdbcMemberContext(new JdbcMemberSave(name));
    }

    @Override
    public Optional<Member> findById(long id) {

        String selectSQL = "select * from member where id = ?";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getLong("id"));
                member.setName(resultSet.getString("name"));
                return Optional.of(member);

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String selectSQL = "select * from member where name = ?";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getLong("id"));
                member.setName(resultSet.getString("name"));
                return Optional.of(member);

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<Member> findAll() {
        String selectAllSQL = "select * from member";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(selectAllSQL);
            resultSet = preparedStatement.executeQuery();

            List<Member> members = new ArrayList<>();
            while(resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getLong("id"));
                member.setName(resultSet.getString("name"));
                members.add(member);
            }
            return members;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void deleteAll() {
        String deleteAllSQL = "delete from member";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(deleteAllSQL);
            preparedStatement.executeUpdate();

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
