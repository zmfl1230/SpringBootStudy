package hello.hellospring.factory;

import hello.hellospring.repository.JdbcMemberContextWithStatementStrategy;
import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MemberFactory {
    @Autowired DataSource dataSource;

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

//
    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
        return new JdbcMemberRepository(jdbcMemberContextWithStatementStrategy());
    }

    @Bean
    public JdbcMemberContextWithStatementStrategy jdbcMemberContextWithStatementStrategy() {
        return new JdbcMemberContextWithStatementStrategy(dataSource);
    }

}
