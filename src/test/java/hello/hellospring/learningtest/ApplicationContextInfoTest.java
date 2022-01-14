package hello.hellospring.learningtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextInfoTest {
    /*
        TODO: 왜 datasource 는 빈 등록이 안되는가
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(OrderFactory.class);
    * */

    // 각 테스트에서 새로운 ApplicationContext 오브젝트 생성 ->  매 테스트마다 각 테스트들에 독립적인 테스트 실행을 위해 테스트 object를 생성하기 때문
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LearningTestConfig.class);

    @BeforeEach
    public void setUp() {
        System.out.println(ac);
    }

    @Test
    @DisplayName("ApplicationContext 생성 확인")
    public void checkApplicationContextIsCreated() {
        assertThat(ac).isInstanceOf(ApplicationContext.class);

    }

    @Test
    @DisplayName("스프링 빈 저장소에 담긴 모든 빈 출력하기")
    public void findAllBean() {
        // command + option + v
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("beanDefinitionName = " + beanDefinitionName + "bean obj = " + bean);
        }

    }
    @Test
    @DisplayName("스프링 빈 저장소에 담긴 Application 빈 출력하기")
    public void findApplicationBean() {
        // command + option + v
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("beanDefinitionName = " + beanDefinitionName + "bean obj = " + bean);
            }
        }
    }


}
