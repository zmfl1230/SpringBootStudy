package hello.hellospring;

import hello.hellospring.annotation.ExcludeComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * [문제]
 * 등록되는 모든 빈들이 스프링에 의해서 싱글톤으로 관리되도록 하기위해 모든 의존관계 설정 클래스 위에 @Configuration을 붙였다.
 * 그랬더니 모든 테스트(@SpringBootTest가 붙은)를 돌릴 때 @Configuration이 붙은 모든 의존관계 설정 클래스의 빈들을 등록하다보다
 * 다음과 같은 에러가 발생했다.
 * ```
 * The bean 'memberService', defined in class path resource [hello/hellospring/factory/MemberFactory.class], could not be registered.
 * A bean with that name has already been defined in class path resource [hello/hellospring/learningtest/LearningTestConfig.class] and overriding is disabled.
 * ```
 * 요약하자면, 등록하고자 하는 빈이 이미 등록돼있고 해당 빈으로의 등록이 불가능하다는 메세지였다.
 *
 * [원인]
 * 결국 문제가 발생한 원인은 @SpringBootTest에 의해 모든 @Configuration 을 단 설정 클래스(비즈니스를 위한 설정 클래스와 테스트를 위한 설정 클래스)들을 찾고
 * 이를 모두 등록하려고 할 때, 같은 이름으로 설정해 둔 내용이 있어 예외가 발생하는 것이다.
 * (=> 어노테이션을 이용한 자동 DI를 하지 않았기 때문에 내가 @Configuration을 달아준 것만 빈으로 등록된다),
 *
 * [해결 방법]
 * 해결 방법으로는 결국 이미 등록된 빈이 재 등록이 되는 것을 막아야 하는데 이를 위해선 다음과 같은 해결방법을 생각해 볼 수 있을 것 같다.
 * 컴포넌트 스캔시, 필요한 의존관계 설정 클래스를 제외한 나머지 클래스는 컴포넌트 스캔 대상에서 제외되도록 하는 것이다. 이는 component scan의 filter를 이용하면 구현이 가능하다.
 * 어차피 @SpringBootTest를 붙이는건 직접 ApplicationContext 오브젝트를 생성해 필요한 의존관계를 등록하고자 하는 것이 아니라 필요한 모든 의존관계를 등록해 두고 테스트 해보고 싶은 것임으로
 * 테스트를 위한 설정 클래스들은 component scan의 filter를 이용해 component scan 대상에서 제외시키고, 그 외 나머지 설정 클래스들만 scan 대상이 되도록 하면 된다.
 * @ExcludeComponent 이 붙은 설정 클래스는 scan 대상에서 제외되도록 처리
 * */
@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ExcludeComponent.class))
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

}
