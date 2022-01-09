package hello.hellospring.service;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTests {

    @Autowired MemberService memberService;

    @BeforeEach
    public void setUp() {
        memberService.deleteAll();
    }

    @Test
    public void 회원가입() {
        //given
        String memberName = "happy spring!!";

        //when
        memberService.join(memberName);

        //then
        Assertions.assertThat(memberName).isEqualTo(memberService.findOneByName(memberName).get().getName());

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
