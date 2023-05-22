package SpringMVC.productservice3.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("로그 필터 시작");
    }

    @Override
    public void destroy() {
        log.info("로그 필터 종료");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //HTTP를 사용하므로 다운 캐스트
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        StringBuffer requestURL = httpRequest.getRequestURL();

        //HTTP 요청 구분을 위한 uuid 생성
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("로그 필터 요청 [{}][{}]", uuid, requestURL);

            //다음 필터 혹은 서블릿 호출
            chain.doFilter(request, response);
        } catch (Exception e){
            throw e;
        } finally {
            log.info("로그 필터 응답 [{}][{}]", uuid, requestURL);
        }

    }
}
