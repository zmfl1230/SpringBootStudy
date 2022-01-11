package hello.hellospring.factory;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class MemberFactory {
    @Autowired DataSource dataSource;
    @Autowired EntityManager entityManager;

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

//
    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(jdbcMemberContextWithStatementStrategy(), dataSource);
//        return new JdbcMemberRepository(dataSource);
        return new JpaMemberRepository(entityManager);
    }

    @Bean
    public JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy() {
        return new JdbcMemberContextWithStatementStrategy(dataSource);
    }

}
