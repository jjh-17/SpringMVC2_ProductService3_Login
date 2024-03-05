package SpringMVC.productservice3.domain.repository;

import SpringMVC.productservice3.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;


//동시성 문제 고려 X ==> ConcurrentHashMap, AtomicLong 사용 고려
@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    //멤버 저장
    public Member save(Member member) {
        member.setMemberId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getMemberId(), member);

        return member;
    }

    //전체 멤버 찾기
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    //멤버 아이디 기반 멤버 찾기
    public Member findByMemberId(Long id) {
        return store.get(id);
    }

    //로그인 아이디 기반 멤버 찾기
    public Optional<Member> findByLoginId(String id) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(id))
                .findFirst();
    }

    public void clearStore() {
        store.clear();
        sequence = 0L;
    }

}
