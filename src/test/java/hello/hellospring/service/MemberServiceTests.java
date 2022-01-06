package hello.hellospring.service;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class MemberServiceTests {

    @Autowired MemberService memberService;

    @AfterEach
    public void setup() {
        memberService.deleteAll();
    }

    @Test
    public void 회원가입() {
        //given
        String memberName = "happy spring";

        //when
        Member joinedMember = memberService.join(memberName);

        //then
        Assertions.assertThat(memberService.findOneByName(memberName).get()).isEqualTo(joinedMember);


    }

    @Test
    public void 회원가입_예외() {

        //given
        String memberName = "happy spring";
        memberService.join(memberName);

        //when
        assertThatThrownBy(() -> memberService.join(memberName))
                .isInstanceOf(IllegalStateException.class);


    }
}
