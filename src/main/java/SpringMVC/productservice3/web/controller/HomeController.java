package SpringMVC.productservice3.web.controller;

import SpringMVC.productservice3.web.argumentresolver.Login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import SpringMVC.productservice3.domain.member.Member;
import SpringMVC.productservice3.domain.repository.MemberRepository;
import SpringMVC.productservice3.web.SessionConst;
import SpringMVC.productservice3.web.session.SessionManager;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        log.info("called home");
        return "home";
    }

    //memberID 쿠키 정보를 활용하는 홈 화면
    //@GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                        Model model) {
        if (memberId == null) {
            log.info("no cookie ==> call home");
            return "home";
        }

        //로그인
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            log.info("can't find member with memberId={}", memberId);
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //memberID 쿠키 정보를 활용하는 홈 화면
    //@GetMapping("/")
    public String homeLogin2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }

        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    //memberID 쿠키 정보를 활용하는 홈 화면
    //@GetMapping("/")
    public String homeLogin3(HttpServletRequest request, Model model) {

        //세션 조회
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        //세션에 저장된 회원 정보 조회
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (member == null) {
            return "home";
        }

        //세션이 유지되면 로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    /*
    [서블릿 HTTP 세션2]
    @SessionAttribute: 스프링이 제공하는 세션 사용 용이 어노테이션
    세션을 생성하지 않음
    @SessionAttribute의 name과 변수의 이름이 동일해야함....?
    */
    //@GetMapping("/")
    public String homeLogin3Spring(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                               Model model) {

        if (loginMember == null) {
            log.info("세션에 회원 정보가 없다");
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        log.info("로그인 성공, 세션 생성");
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /*
    [ArgumentResolver]
    @Login 어노테이션을 직접 만들어 로그인 간소화
    */
    @GetMapping("/")
    public String homeLogin3ArgumentResolver(@Login Member loginMember, Model model) {

        if (loginMember == null) {
            log.info("세션에 회원 정보가 없다");
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        log.info("로그인 성공, 세션 생성");
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
