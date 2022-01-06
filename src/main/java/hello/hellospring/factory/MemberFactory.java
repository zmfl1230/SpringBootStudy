package hello.hellospring.factory;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberFactory {

    @Bean
    public MemberService memberService() {
        return new MemberService(new MemoryMemberRepository());
    }

//
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

}
