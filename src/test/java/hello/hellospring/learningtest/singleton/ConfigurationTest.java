package hello.hellospring.learningtest.singleton;

import hello.hellospring.learningtest.LearningTestConfig;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Configuration 이 붙지 않는다면, 싱글톤이 보장되지 않지 않을까?
 * 싱글톤이 보장되지 않는다면, 등록된 빈은 어디에 등록되는 것이며, 어떻게 관리되는 것일까?
 *
 * @configuration 과 싱글톤과의 관계
 * configuration 어노테이션은 아래의 역할을 한다.
 * 1. 해당 클래스 내에 담긴 설정 정보들이 component scan의 대상이 되야 한다고 스프링에게 고지한다. 자신 또한 component scan의 대상이고, 빈으로 등록된다.
 * 2. 해당 클래스 내에 메서드 형으로 정의된 빈들을 CGLIB(클래스의 바이트 코드를 조작하는 라이브러리)로 오로지 하나의 오브젝트로만 생성되도록 보장한다.
 * 2번의 이유로 @Configuration을 붙였다면, 여러번의 오브젝트를 생성하는 메서드를 호출하더라도 하나의 오브젝트를 공유하도록 스프링에서 손을 써줌으로써 실글톤 오브젝트가 보장되는 것이다.
 *
 * 그헐다면 configuration 어노테이션을 때더라도 잘 등록되는 이유는 무엇일까?
 * configuration 어노테이션은 해당 빈이 싱글톤 오브젝트로 남아있을 수 있도록 보장하는 역할을 담당하기 떄문이다. @Bean 어노테이션만으로도 충분히 빈으로 등록이 가능하다.
 * 하지만, 헤당 오브젝트가 싱글톤으로 관리되는 것은 보장하지 못한다.
 *
 * configuration 어노테이션을 붙이지 않더라고 똑같이 스프링 컨테이너(싱글톤 레지스트리)에 등록되는 것이라면 같은 이름이 중복해서 등장할 가능성이 있을텐데?
 * 결론만 이야기 하자면 사전에 configuration 어노테이션이 붙지 않더라도 싱글톤 레지스트리에는 하나의 빈만 저장된다.
 * 즉, 빈으로 등록될 오브젝트을 생성하기 위해 그 과정에서 생성된 다른 오브젝트들은 일반 오브젝트로써 생성이 될뿐, 빈에 등록되지 않는다.
 * 설정 정보 내에 담긴 여러 메서드들을 실행 전 사전에 BeanMethodMap에 저장해 두고, 해당 Map을 돌면서 실행해서 반환된 오브젝트만을 싱글톤 레지스트리에 저장하기 때문에
 * `ConflictingBeanDefinitionException`과 같은 예외가 발생하지 않는 것이다.
 * @see <a href="https://www.inflearn.com/questions/323589?re_comment_id=159873"/>
 *
* */
public class ConfigurationTest {

    ApplicationContext acNotUsingConfiguration = new AnnotationConfigApplicationContext(SingletonLearningTestNotUsingConfigurationConfig.class);
    ApplicationContext acUsingConfiguration = new AnnotationConfigApplicationContext(LearningTestConfig.class);


    @Test
    @DisplayName("@Configuration을 붙이지 않는다면, 싱글톤이 보장되지 않는다")
    public void notEnsureSingletonNotUsingConfiguration() {
        MemberService memberService = acNotUsingConfiguration.getBean("memberService", MemberService.class);
        MemberRepository memberRepository = acNotUsingConfiguration.getBean("memberRepository", MemberRepository.class);

        Assertions.assertThat(memberRepository).isNotSameAs(memberService.getMemberRepository());
    }

    @Test
    @DisplayName("@Configuration을 붙이면, 싱글톤이 보장된다")
    public void ensureSingletonUsingConfiguration() {
        MemberService memberService = acUsingConfiguration.getBean("memberService", MemberService.class);
        MemberRepository memberRepository = acUsingConfiguration.getBean("memberRepository", MemberRepository.class);

        Assertions.assertThat(memberRepository).isSameAs(memberService.getMemberRepository());
    }
}
