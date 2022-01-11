package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository{
    private final Map<Long, Member> members = new HashMap<>();
    private long sequence = 0;

    @Override
    public void save(String name) {
        Member member = new Member();
        member.setId(++sequence);
        member.setName(name);
        members.put(member.getId(), member);
    }

    @Override
    public Member save(Member member){
        member.setId(++sequence);
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return members.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(members.values());
    }

    @Override
    public void deleteAll() {
        members.clear();
    }
}
