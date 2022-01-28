package hello.hellospring.learningtest.provider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrototypeProviderTest {

    @Test
    void notUsingProviderTest() {
        AnnotationConfigApplicationContext ac = new
                AnnotationConfigApplicationContext(BasicClientBean.class, PrototypeBean.class);

        BasicClientBean clientBean1 = ac.getBean(BasicClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);


        BasicClientBean clientBean2 = ac.getBean(BasicClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);

        // 프로토 타입 빈이지만 새로 생성해도 동일성 비교가 일치한다. (쓰임의 목적이 모호해진 상황)
        assertThat(clientBean1.getPrototypeBean()).isSameAs(clientBean2.getPrototypeBean());
    }

    @Test
    void usingProviderTest() {
        AnnotationConfigApplicationContext ac = new
                AnnotationConfigApplicationContext(ProviderClientBeanWrongUsing.class, PrototypeBean.class);

        ProviderClientBeanWrongUsing clientBean1 = ac.getBean(ProviderClientBeanWrongUsing.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);


        ProviderClientBeanWrongUsing clientBean2 = ac.getBean(ProviderClientBeanWrongUsing.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

        // 프로토 타입 빈의 목적에 맞게 새로 생성되며 동일성 비교가 불일치한다. (쓰임의 목적이 명확해진 상황)
        assertThat(clientBean1.getPrototypeBean()).isNotSameAs(clientBean2.getPrototypeBean());
    }



    static class BasicClientBean {

        @Autowired
        private PrototypeBean  prototypeBean;

        public PrototypeBean getPrototypeBean() {
            return prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
    /**
     * ProviderClientBeanWrongUsing 사례는
     * 1. 싱글톤 빈과 프로토타입 빈을 동시에 사용할 경우 발생하는 문제
     * 2. 싱글톤 빈 내부적으로 상태를 가지고 있는 경우 발생하기 쉬운 무서운 오류의 실례를 보여준다(잡기가 어렵다).
     *
     * [문제 - 싱글톤 빈과 프로토타입 빈을 동시에 사용할 경우 발생하는 문제]
     * ObjectProvider를 사용해서 싱글톤 내부적으로 사용되는 prototype 빈이지만 클라이언트가 요청할 때마다 새로운 빈을 생성해 반환하고자 했다.
     * 그래서 생성 및 의존성 주입 후 호출되는 init() 이라는 생성 콜백함수를 만들고,
     * 함수 안에 provider에서 getObject()을 호출해 반환된 프로토타입 빈을 `멤벼 변수`에 담아주었다.
     * 그랬더니 두번의 클라이언트의 count 값이 2라며, 기대값과 실제값이 다르다고 테스트가 실패했다.
     *
     * [원인]
     * 분명 ObjectProvider를 이용해 새로운 prototype 빈을 생성해 주도록 하였는데 무엇이 문제인지 찾아보기 위해 init() 내부적으로 주입된 prototypeBean 값을 찍어보니
     * 기대와는 다르게 저 init 함수가 한번만 출력되었다. 즉, 싱글톤의 본질을 잊은채 생성 후 호출되는 init() 함수 안에서 prototype 빈을 새롭게 생성해 멤벼 변수에 담아준 것이다.
     * 그렇다면 당연히 이 싱글톤 빈에 접근해 프로토타입 빈 오브젝트에 값을 얻고자 하는 클라이언트는 최초에 한번 생성된 prototype 빈에만 계속해서 접근을 하게 되는 것이다.
     * (싱글톤 빈이 최초에 한번만 호출되기 때문에 생성 콜백안에 담긴 프로토타입 빈 생성 호출도 한번만 호출될 수 밖에 없다.)
     *
     * 어째서 이런 바보같은 짓을 했는지 한탄하며 바보같은 짓을 한번 더 하게 된다..!
     *
     * [해결 - 완전한 해결이 아닌 위 원인에 대한 조치를 취한... 해결]
     * 한번만 호출되는게 문제였다면, 두번 호출되도록 해주면 된다! 그래서 logic() 함수 안에 "prototypeBean 은 멤버 필드로 남겨둔 채" 이곳에서 초기화 하는 방식으로 두번 호출되도록 변경했다.
     * ```
     *         public int logic() {
     *             prototypeBean = prototypeBeanObjectProvider.getObject(); <- 이 부분
     *             prototypeBean.addCount();
     *             return prototypeBean.getCount();
     *         }
     * ```
     * 두번 호출이 되도록 변경했으니 해결된 줄 알았으나,,, 이 역시 싱글톤의 본질은 잊은 어리석인 짓이였다,,,
     *
     * [문제 - 싱글톤 빈 내부적으로 상태를 가지고 있는 경우 발생하는 문제]
     * 이제는 성공하겠지라는 생각으로 테스트를 돌렸더니, 또 실패 했다. 실패한 부분은 다음 줄이였다.
     * assertThat(clientBean1.getPrototypeBean()).isNotSameAs(clientBean2.getPrototypeBean());
     * 문제는 clientBean1.getPrototypeBean()을 통해 불러온 prototype bean과 clientBean2.getPrototypeBean()을 통해 불러온  prototype bean이 같다는 것이였다.
     *
     * [원인]
     * 해당 문제의 원인을 찾기 위해 도대체 어느 시점부터 prototype이 같아지는 건지(count 케이스는 통과했으므로 분명히 달랐다가 어느 순간부터 같아지는 것은 명확하다.)
     * 다양한 위치에서 prototype을 출력해 보았다.
     * 1. 우선, Prototype 위치에 생성 콜백함수인 init()에 현재 prototype 개체를 출력할 수 있도록 했다.
     * ```
     *     @Scope("prototype")
     *     static class PrototypeBean {
     *     ...
     *         // 해당 부분
     *         @PostConstruct
     *         public void init() {
     *             System.out.println("PrototypeBean.init " + this);
     *         }
            ...
     *     }
     * ```
     * 2. 테스트 내부적으로 prototype 빈 오브젝트 동일성 비교 직전에 출력해 볼 수 있도록 했다.
     * ```
     *     @Test
     *     void usingProviderTest() {
            ...

     *         System.out.println("clientBean1 = " + clientBean1.getPrototypeBean());
     *         System.out.println("clientBean2 = " + clientBean2.getPrototypeBean());
     *
     *         assertThat(clientBean1.getPrototypeBean()).isNotSameAs(clientBean2.getPrototypeBean());
     *     }
     * ```
     *
     * 그랬더니, 다음과 같은 결과가 출력되었다.
     * ```
     * // 1번 출력
     * PrototypeBean.init hello.hellospring.learningtest.provider.PrototypeProviderTest$PrototypeBean@5efa40fe
     * PrototypeBean.init hello.hellospring.learningtest.provider.PrototypeProviderTest$PrototypeBean@6736fa8d
     *
     * // 2번 출력
     * clientBean1 = hello.hellospring.learningtest.provider.PrototypeProviderTest$PrototypeBean@6736fa8d
     * clientBean2 = hello.hellospring.learningtest.provider.PrototypeProviderTest$PrototypeBean@6736fa8d
     * ```
     * 분명 다른 값이 생성되었는데, 왜 반환값이 동일할까을 두고 고민하던 찰나에... 이런 멤버 변수... 싱글톤인데 상태를 가지고 있었구나... 생각이 떠올랐다.
     *
     * [해결 - 완전 해결]
     * 실글톤 빈에 상태를 없애고, 멤벼 변수를 메서드 내 지역 변수로 만들어 해당 값을 반환해 주는 식으로 해결했다.
     * 하지만, 이렇게 되면 한명의 클라이언트당 두개의 prototype 빈이 생성될 것이다.
     * **ProviderClientBeanCorrectUsing 참조

     * */

    static class ProviderClientBeanWrongUsing {

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;
        private PrototypeBean prototypeBean;

        public PrototypeBean getPrototypeBean() {
            return prototypeBeanObjectProvider.getObject();
        }

        @PostConstruct
        public void init() {
            //prototypeBean = prototypeBeanObjectProvider.getObject();
            System.out.println("PrototypeBean.init " + prototypeBean +" this " + this);
        }


        public int logic() {
            prototypeBean = prototypeBeanObjectProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    static class ProviderClientBeanCorrectUsing {

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;
        private PrototypeBean prototypeBean;

        public PrototypeBean getPrototypeBean() {
            return prototypeBeanObjectProvider.getObject();
        }

        @PostConstruct
        public void init() {
            //prototypeBean = prototypeBeanObjectProvider.getObject();
            System.out.println("PrototypeBean.init " + prototypeBean +" this " + this);
        }


        public int logic() {
            prototypeBean = getPrototypeBean();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }


    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}