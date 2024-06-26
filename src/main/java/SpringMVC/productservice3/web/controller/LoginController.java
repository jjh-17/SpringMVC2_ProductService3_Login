package SpringMVC.productservice3.web.controller;

import SpringMVC.productservice3.domain.member.Member;
import SpringMVC.productservice3.domain.service.LoginService;
import SpringMVC.productservice3.web.SessionConst;
import SpringMVC.productservice3.web.login.LoginForm;
import SpringMVC.productservice3.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
[Form 전송 객체 분리]
1. 폼 데이터 전달에 동일한 도메인 객체 사용
    -동일한 객체를 컨트롤러, 리포지토리까지 직접 전달 ==> 중간에 도메인 객체 생성이 없어 간단
    -수정 시 중복 가능, groups 필요 ==> 간단한 경우에만 적용 가능

2. 폼 데이터 전달에 별도의 객체 사용
    -별도의 폼 객체를 만들어 검증 중복 X
    -컨트롤러에서 도메인 객체를 생성하는 변환 과정이 추가됨
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        log.info("called LoginForm");
        return "login/loginForm";
    }

    // 로그인 성공 시 세션 쿠키를 생성하여 브라우저 종료 시까지 로그인 정보 유지
    // @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                         BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        log.info("login 성공 - {}", member);

        //세션 쿠키 생성: 시간정보를 주지 않아 브라우저 종료시 까지 유지
        Cookie cookie = new Cookie("memberId", String.valueOf(member.getMemberId()));
        response.addCookie(cookie);
        log.info("cookie = {}", cookie);

        return "redirect:/";
    }

    // 로그인 처리 - 세션 동작 방식
    // @PostMapping("/login")
    public String login2(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                         BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        log.info("로그인 성공, 세션 생성");
        sessionManager.createSession(member, response);

        return "redirect:/";
    }

    //서블릿 HTTP 세션1
//    @PostMapping("/login")
    public String login3(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                         BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        log.info("로그인 성공, 세션 생성");

        // 세션 생성 후 로그인 회원 정보 보관
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/";
    }

    // URL에 적힌 redirectURL을 이용하여 로그인 성공 시 접근 실패 페이지로 이동
    @PostMapping("/login")
    public String login4(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                         BindingResult bindingResult, HttpServletRequest request,
                         @RequestParam(defaultValue = "/") String redirectURL) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        log.info("로그인 성공, 세션 생성");
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:" + redirectURL;
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //로그아웃 시 세션 정보 제거
    //@PostMapping("/logout")
    public String logout2(HttpServletRequest request) {
        sessionManager.expireCookie(request);
        return "redirect:/";
    }

    // 로그아웃 시 HttpSession 정보 제거
    @PostMapping("/logout")
    public String logout3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)    session.invalidate();
        return "redirect:/";
    }
}
