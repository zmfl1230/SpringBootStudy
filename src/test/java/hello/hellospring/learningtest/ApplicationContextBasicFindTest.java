package hello.hellospring.learningtest;

import hello.hellospring.repository.order.DiscountPolicy;
import hello.hellospring.repository.order.FixDiscountPolicy;
import hello.hellospring.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class ApplicationContextBasicFindTest {
    ApplicationContext ac = new AnnotationConfigApplicationContext(LearningTestConfig.class);

    @Test
    @DisplayName("올바른 빈 이름으로 조회")
    public void findByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("이름 없이 타입만으로 조회")
    public void findByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회 - 지양(구체성에 의존하게 됨)")
    public void findByConcreteType() {
        FixDiscountPolicy fixDiscountPolicy = ac.getBean(FixDiscountPolicy.class);
        assertThat(fixDiscountPolicy).isInstanceOf(FixDiscountPolicy.class);
    }

    @Test
    @DisplayName("올바르지 않는 이름으로 조회")
    public void findByUnCorrectName() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("unCorrectName"));
    }

    // 복수의 빈 관리
    @Test
    @DisplayName("타입만으로 조회 시, 복수의 빈이 탐색될 때 " +
            "(일반적으로 상속관계의 클래스 타입으로 조회시 부모를 상속받은 자식 클래스가 모두 조사되기 때문에 복수의 빈이 반환되는 것이다.)")
    public void findByTypeWithMultipleBeans() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("타입만으로 조회 시, 복수의 빈이 탐색될 때 빈 이름 지정으로 해결")
    public void findByOnlyNameAndTypeWithMultipleBeans() {
        DiscountPolicy fixDiscountPolicy = ac.getBean("fixDiscountPolicy", DiscountPolicy.class);
        assertThat(fixDiscountPolicy).isInstanceOf(DiscountPolicy.class);

        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(DiscountPolicy.class);
    }

    @Test
    @DisplayName("타입만으로 조회 시, 복수의 빈 관리")
    public void findBeansByTypeWithMultipleBeans() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        for (String key : beansOfType.keySet()) {
            System.out.println(key);
        }

        assertThat(beansOfType.size()).isEqualTo(2);
    }
}
