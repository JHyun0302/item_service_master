package hello.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long seqence = 0L;

    public Member save(Member member) {
        member.setId(++seqence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(String id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        /*List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();*/

        return findAll().stream() //List를 stream으로 바꿈.
                .filter(m -> m.getLoginId().equals(loginId))//m(member) == loginId랑 같으면 다음 단계로 이동
                .findFirst(); //먼저 찾은 얘를 받아서 보냄
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }


}
