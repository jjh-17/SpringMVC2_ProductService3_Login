package SpringMVC.productservice3.domain.service;

import SpringMVC.productservice3.domain.member.Member;
import SpringMVC.productservice3.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))      //필터 설정: password와 같은 것
                .orElse(null);                                  //password가 같은게 없으면 null 반환
    }
}
