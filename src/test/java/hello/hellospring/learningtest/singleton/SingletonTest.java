package hello.hellospring.learningtest.singleton;

import hello.hellospring.learningtest.LearningTestConfig;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * `싱글톤 팬턴` 은 어떤 클래스를 애플리케이션 내에서 제한된 수(일반적으로 하나)로만 존재하도록 규정하는 패턴이다.
 * 이러한 원칙 때문에, 클래스 내에 생성자를 private으로 만들고, 프로그램 시작과 동시에 오브젝트를 생성한뒤, 해당 오브젝트의 추가적인 생성을 막는다.
 *
 * 하지만, 이 패턴의 원칙과 이 원칙을 지키기 위한 조치(생성자를 private으로 제한하는 것 등)로 인해 다음과 같은 단점들이 존재한다.
 * - 생성자를 private으로 선언했기 때문에 클래스의 상속, 다형성인 객체지향의 장점을 이용한 설계를 할 수 없다.
 * - 테스트하기 어렵다. 싱글톤을 초기화 과정에서 인터페이스를 다이내믹하게 주입하기도 어렵고, 테스트용 목 오브젝트를 생성하는 것 또한 어렵기 때문이다.
 * - 그 밖에도, 분산 서버 환경에서 완전한 싱글톤이 보장되지 못한다는 한계와 전역 상태로 유지되어야 한다는 점등의 단점들이 존재한다.
 *
 * 싱글톤 패턴의 원칙을 준수하면서도 단점을 해결하는 스프링의 싱글톤 레지스트리?
 * 처음 스프링의 싱글톤 레지스트리를 공부할 때, 싱글톤 패턴의 원칙은 준수하면서도 장점은 챙기고, 단점을 보완한 것이 싱글톤 레지스트리가 아닐까라고 생각했다.
 * IOC 원칙하에, 스프링 컨테이너가 동작에 필요한 오브젝트를 애플리케이션에 오로지 하나만 생성하고, 관리해주면서 오브젝트의 라이프 사이클을 책임지기 때문에
 * 싱글톤 패턴이 가지고 있던 장정은 취하되, 단점은 해결해준다고 느꼈기 떄문이다.
 *
 * 스프링의 듬직한 역할로 싱글톤 패턴의 걸림돌이였던 생성자를 제한하지 않고서도 싱글톤 오브젝트를 만들 수 있게 되었다.
 * 싱글톤 패턴에서는 생성자를 제한함으로써 싱글톤 패턴을 적용한 클래스가 전 서비스에 거쳐 오로지 하나만 생성됨을 보장할 수 있었다.
 * 하지만, 싱글톤 레지스트리는 그렇지 않다. 자유롭게 생성되고, 생성되면 빈에 등록한 인스턴스와 엄연히 다른 인스턴스가 생성된다.
 *
 * 그렇다면,
 * 1. 싱글톤 오브젝트라고 불릴 수 있을까?
 * 2. 애플리케이션 내에 오로지 하나의 오브젝트로만 생성된다는 것은 어떻게 보장할 수 있을까?
 * 3. 오히려 필요에 의한다면 인스턴스가 생성될 수 있게 하기 위함인가? 그래서 자유롭게 생성되는 것에 대한 위험을 감수하는 것인가?
 * 라는 의문이 생긴다.
 *
 * 위와 같은 의문은 어쨋든 기존의 싱글톤 패턴의 단점이었던 `테스트의 어려움`을 해결하고,
 * 필요하다면 자유롭게 목 오브젝트를 생성해서 사용할 수 았다는 편리함을 제공한다는 장점의 가치가
 * 오로지 단 하나의 오브젝트를 생성해야 한다는 원칙의 중요성 보다 더 크기때문에 아무 문제 없이 사용하는 것이 아닐까라는 생각이 든다.
 *
 * -- 추가 --
 * 보면 이전까지 스프링이 싱글톤을 온전히 구현하는 것이 맞는가라는 의문하에 그 의문이 들게된 코드를 아래와 같이 테스트 코드로 만들어보았다. 의문을 해결하기 위해 이것 저것을 찾아보다
 * 사실 아래처럼 새로운 인스턴스를 생성해 쓸 수 있게 하는 것은 사실 `스프링의 영역 밖`이라는 사실을 알게 되었다.
 * 즉, 스프링의 기능을 이용한 것이 아니라 `스프링 없는 단순 DI`를 통해 테스트 해 본 것이다.
 * 이렇게 되면 스프링을 자체적으로는 싱글톤 오브젝트를 관리하는 것은 맞되, 테스트할 떄나 필요한 경우 해당 오브젝트를 자유롭게 생성해 사용할 수 있게 했다는 말이 된다.
* */
public class SingletonTest {
    ApplicationContext ac = new AnnotationConfigApplicationContext(LearningTestConfig.class);

    @Test
    @DisplayName("Bean으로 등록이 돼 있다고 하더라도 객체를 생성하다면 다른 인스턴스를 생성한다. - 직접 인스턴스 생성")
    public void createSingletonObjectUsingNew() {
        MemberRepository memberRepositoryByNew = new MemoryMemberRepository();

        MemberRepository memberRepositoryByContainer = ac.getBean("memberRepository", MemberRepository.class);
        Assertions.assertThat(memberRepositoryByNew).isNotSameAs(memberRepositoryByContainer);
    }

    @Test
    @DisplayName("Bean으로 등록이 돼 있다고 하더라도 객체를 생성하다면 다른 인스턴스를 생성한다. - 설정 클래스 오브젝트 생성 후, 여기서 접근")
    public void createSingletonObjectUsingConfig() {
        LearningTestConfig learningTestConfig = new LearningTestConfig();
        MemberRepository memberRepositoryByConfig = learningTestConfig.memberRepository();

        MemberRepository memberRepositoryByContainer = ac.getBean("memberRepository", MemberRepository.class);
        Assertions.assertThat(memberRepositoryByConfig).isNotSameAs(memberRepositoryByContainer);
    }

}
