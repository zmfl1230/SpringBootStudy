package hello.hellospring.factory;

import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;

public class MemberFactory {
    public MemberService memberService() {
        return new MemberService(new MemoryMemberRepository());
    }
}
