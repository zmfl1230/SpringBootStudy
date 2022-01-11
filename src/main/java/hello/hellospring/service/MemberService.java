package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class MemberService {
    MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void join (String name) {
        // 중복 이름 방지
        validateDuplicate(name);
        Member member = new Member();
        member.setName(name);
        memberRepository.save(member);
    }

    private void validateDuplicate(String name) {
        memberRepository.findByName(name)
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 존재하는 이름입니다. " + member);
                });
    }

    public List<Member> findMembers () {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findOneByName(String name) {
        try {
            return memberRepository.findByName(name);
        } catch (EmptyResultDataAccessException e) {
            return Optional
                    .empty();
        }
    }

    public void deleteAll() {
        memberRepository.deleteAll();
    }
}
