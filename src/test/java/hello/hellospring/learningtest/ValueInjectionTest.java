package hello.hellospring.learningtest;

import hello.hellospring.lifecycle.NetworkClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 의존관계 주입 및 값 주입은 @Bean 이 붙은 메서드 내에 정의한 의존관계 주입만이 오브젝트 생성 후, 주입이 일어날 수 있다.
 * @Test setValueOuter(): 값 삽입이 외부(클라이언트)에서 발생, 삽입 전에는 bean.url == null
 * @Test setValueInner(): 값 삽입이 내부(@Bean 메서드)에서 발생, 생성 직후, bean.url != null
 *
 * ```
 *  스프링 컨테이너가 @Bean NetworkClient를 보고 NetworkClient 객체를 생성하기 위해서 networkClient()메서드를 호출한다.
 *  이 시점에 지금 코드에 보이는 것처럼 new NetworkClient()로 NetworkClient를 생성하고 다음 코드에서
 *  setUrl를 호출해서 url 값을 값 주입한다.
 * ```
 *
 * ```
 * @Value, @Autowired 등은 BeanPostProcessor 구현체로부터 수행되고
 * BeanPostProcessor 구현체들의 수행들이 모두 끝난 후에 초기화 라이프사이클이 일어난다.
 * 전반적인 싱글톤 빈의 라이프 사이클을 다움과 같은데.
 * 스프링컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
 * 이 초기화 콜백 전에 의존관계 주입 뿐 아니라 값 주입 등 수 많은 처리를 하고 있다.
 * ```
 * */

public class ValueInjectionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);

    @Test
    public void setValueOuter() {

        NetworkClient bean = ac.getBean("NetworkClientOuter", NetworkClient.class);

        Assertions.assertThat(bean.getUrl()).isNull();
        bean.setUrl("localhost:8080");
        Assertions.assertThat(bean.getUrl()).isNotNull();

        ac.close();

    }

    @Test
    public void setValueInner() {
        NetworkClient bean = ac.getBean("NetworkClientInner", NetworkClient.class);

        Assertions.assertThat(bean.getUrl()).isNotNull();

        ac.close();

    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClient NetworkClientOuter() {
            return new NetworkClient();
        }

        @Bean
        public NetworkClient NetworkClientInner() {
            // 생성
            NetworkClient networkClient = new NetworkClient();

            // 값 주입
            networkClient.setUrl("localhost:8080");

            // return 후, 생성 콜백 살행
            return networkClient;
        }
    }
}
