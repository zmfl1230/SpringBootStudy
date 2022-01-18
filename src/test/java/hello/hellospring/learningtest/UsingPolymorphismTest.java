package hello.hellospring.learningtest;

import hello.hellospring.repository.order.DiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Map;

public class UsingPolymorphismTest {

    @Test
    @DisplayName("다형성을 활용하여 유연하게 할인 정책 주입")
    public void usePolymorphismOfDiscountPolicy() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(LearningTestConfig.class, DiscountService.class);
        System.out.println(Arrays.toString(ac.getBeanDefinitionNames()));

        DiscountService discountService = ac.getBean("usingPolymorphismTest.DiscountService", DiscountService.class);
        discountService.setDiscountPolicies(ac.getBeansOfType(DiscountPolicy.class));

        Assertions.assertThat(discountService.discount(20000, "fixDiscountPolicy")).isEqualTo(19000);
        Assertions.assertThat(discountService.discount(20000, "rateDiscountPolicy")).isEqualTo(18000);

    }

    static public class DiscountService {
        Map<String, DiscountPolicy> discountPolicies;

        public void setDiscountPolicies(Map<String, DiscountPolicy> discountPolicies) {
            this.discountPolicies = discountPolicies;
        }

        public int discount(int price, String policyName) {
            DiscountPolicy policy = discountPolicies.get(policyName);
            return policy.discount(price);
        }
    }

}
