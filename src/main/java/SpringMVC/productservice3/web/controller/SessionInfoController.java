package SpringMVC.productservice3.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


//세션 정보 제공 컨트롤러
@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 존재하지 않습니다.";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId()); //jsessionid 값
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); //세션의 유효 시간(초)
        log.info("creationTime={}", new Date(session.getCreationTime())); //세션 생성일시

        //세션과 연결된 사용자가 서버에 접근한 가장 최근 시간
        //클라이언트가 서버로 sessionID를 요청한 경우 갱신
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew()); ///새로 만든 세션인가 조회한 세션인가

        return "세션 출력";
    }
}
