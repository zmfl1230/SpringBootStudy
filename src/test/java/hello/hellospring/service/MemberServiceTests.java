package hello.hellospring.service;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MemberServiceTests {
    MemberService memberService = new MemberService();
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
