package SpringMVC.productservice3.web.session;

import SpringMVC.productservice3.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() throws Exception {
        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse(); //테스트용 가짜 서블렛
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);
        System.out.println("result = " + result);

        //세션 만료
        sessionManager.expireCookie(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
        System.out.println("expired = " + expired);

    }

}