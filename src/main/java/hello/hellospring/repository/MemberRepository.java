package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(String name);
    Optional<Member> findById(long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
    void deleteAll();
}
