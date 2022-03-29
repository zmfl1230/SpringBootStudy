package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository{

    private final JdbcTemplate JdbcTemplate;
    private final JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy;
    /*
     * 클래스 레벨에서 구체적인 의존관계가 그려지고 있다.(인터페이스를 두지 않고, 클래스 오브젝트를 주입했기 때문)
     * 인터페이스를 둬 클래스간 느슨한 의존관계를 나타내고 있지 않았다는 것에 온전한 DI라고 볼 수 없을 순 있으나,
     * DI는 넓게 보면 오브젝트의 생성과 관계 설정을 오브젝트에서 제거하고, 이 역할을 제 3자(스프링 컨테이너)에게 위임한다는 IOC 개념을 포괄하고 있다.
     * 이런 개념하에. jdbcMemberContextWithStatementStrategy을 JdbcMemberRepository에서 사용할 수 있도록 주입했다는 것은 DI의 원리를 따른다고 볼 수 있다.
     * */

    public JdbcMemberRepository(DataSource dataSource, JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy) {
        this.jdbcMemberContextWithStatementStrategy = jdbcMemberContextWithStatementStrategy;
        this.JdbcTemplate = new JdbcTemplate(dataSource);
    }


    /*
     * [템플릿/콜백 패턴으로의 전환]
     * Context(템플릿)에 전략을 주입하기 위한 클래스를 따로 두지 않고, DI 대상이 되는 인터페이스를 구현한 익명 내부 클래스로 클래스 불필요한 생성을 방지합니다.
     * 이는 다음 두 가지의 순기능을 가져옵니다.
     * 1. 불필요한 클래스 파일 생성의 방지
     * 2. 메서드 내부 변수의 사용
     * : 기존에는 클래스 내에 생성자로 필요한 데이터를 넘겨받아 사용했지만, 지역 변수를 활용해 사용 가능하다는 장점이 있다.
     *
     * 클래스로 따로 분리하지 않고, 익명 내부 함수로 구현을 변경한 이유
     * : 오로지 하나의 메서드의 수행을 위해서만 필요되어지는 코드이기때문에 클래스로 분리하는 것에 불필요함을 느낌.
     *

    * */

    @Override
    public void save(String name){
        jdbcMemberContextWithStatementStrategy.jdbcMemberContext(new JdbcMemberSave(name));
    }

    @Override
    public Member save(Member member){
        JdbcTemplate.update("insert into member(name) values(?)", member.getName());
        return member;
    }

    @Override
    public void deleteAll() {
        JdbcTemplate.update("delete from member");
    }

    @Override
    public Optional<Member> findById(long id) {
        return Optional.ofNullable(JdbcTemplate.queryForObject("select * from member where id = ?", memberRowMapper(), id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        try{
            return Optional.ofNullable(JdbcTemplate.queryForObject("select * from member where name = ?", memberRowMapper(), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return JdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper () {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

}
