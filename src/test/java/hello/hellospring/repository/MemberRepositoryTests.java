package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberRepositoryTests {
    MemberRepository memberRepository = new MemoryMemberRepository();

    @Test
    void save() {
        String memberName = "happy spring";

        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(0);
        memberRepository.save(memberName);
        Assertions.assertThat(memberRepository.findByName(memberName).get().getName()).isEqualTo(memberName);
    }

    @Test
    void findByName() {
        String memberName1 = "happy spring1";
        String memberName2 = "happy spring2";
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(0);

        memberRepository.save(memberName1);
        memberRepository.save(memberName2);
        Assertions.assertThat(memberRepository.findByName(memberName1).get().getName()).isEqualTo(memberName1);
        Assertions.assertThat(memberRepository.findByName(memberName1).get().getName()).isNotEqualTo(memberName2);
    }

    @Test
    void findAll() {
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(0);

        memberRepository.save("happy spring1");
        memberRepository.save("happy spring2");
        memberRepository.save("happy spring3");
        memberRepository.save("happy spring4");

        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(4);
    }

}
