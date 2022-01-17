package hello.hellospring.learningtest;

import hello.hellospring.annotation.ExcludeComponent;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.repository.order.DiscountPolicy;
import hello.hellospring.repository.order.FixDiscountPolicy;
import hello.hellospring.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 빈 등록시, 오브젝트 생성 과정에서 의존성 주입에 필요한 오브젝트가 아직 빈으로 등록되지 않은 경우에는 어떻게 할까?
 * 설정 클래스를 통해 수동으로 DI를 하든, @Autowired를 통해 자동으로 DI를 받든 `생성자 주입`에 한에서는
 * 오브젝트 생성 과정에서 DI할 오브젝트가 아직 생성되지 않았다면 해당 오브젝트부터 먼서 생성해 빈으로 등록해 둔 뒤, 해당 오브젝트를 가져와 주입한다.
 * "빈 생성과 동시에 의존관계 주입이 동시에 일어난다."
 *
 * */
public class BeanRegisterOrder {
    @Test
    @DisplayName("오브젝트 생성과정에서 DI할 오브젝트가 아직 생성되지 않은 경우에는 해당 오브젝트를 먼서 생성해 빈으로 등록한 뒤 가져와 사용한다.")
    public void beanRegisterOrder() {

        /** [출력 순서 - BeanRegisterOrderConfig1]
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberService'
         * memberService
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberRepository'
         * memberRepository
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'fixDiscountPolicy'
         * fixDiscountPolicy
         *
         * [출력 순서 - BeanRegisterOrderConfig2]
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberRepository'
         * memberRepository
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'memberService'
         * memberService
         * [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'fixDiscountPolicy'
         * fixDiscountPolicy
         *
         * * */
        new AnnotationConfigApplicationContext(BeanRegisterOrderConfig1.class);
        new AnnotationConfigApplicationContext(BeanRegisterOrderConfig2.class);
    }

    @Configuration
    @ExcludeComponent
    static class BeanRegisterOrderConfig1 {

        @Bean
        public MemberService memberService() {
            System.out.println("memberService");
            return new MemberService(memberRepository());
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            System.out.println("fixDiscountPolicy");
            return new FixDiscountPolicy();
        }

        @Bean
        public MemberRepository memberRepository() {
            System.out.println("memberRepository");
            return new MemoryMemberRepository();
        }
    }

    @Configuration
    @ExcludeComponent
    static class BeanRegisterOrderConfig2 {

        @Bean
        public MemberRepository memberRepository() {
            System.out.println("memberRepository");
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberService memberService() {
            System.out.println("memberService");
            return new MemberService(memberRepository());
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            System.out.println("fixDiscountPolicy");
            return new FixDiscountPolicy();
        }
    }
}
