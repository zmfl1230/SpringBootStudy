package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberRepositoryTests {
    MemberRepository memberRepository = new MemoryMemberRepository();

    @Test
    void save() {
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(0);

        Member member = memberRepository.save("happy spring");
        Assertions.assertThat(memberRepository.findById(member.getId()).get()).isEqualTo(member);
    }

    @Test
    void findByName() {
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(0);

        Member member1 = memberRepository.save("happy spring1");
        Member member2 = memberRepository.save("happy spring2");
        Assertions.assertThat(memberRepository.findByName(member1.getName()).get()).isEqualTo(member1);
        Assertions.assertThat(memberRepository.findByName(member1.getName()).get()).isNotEqualTo(member2);
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
