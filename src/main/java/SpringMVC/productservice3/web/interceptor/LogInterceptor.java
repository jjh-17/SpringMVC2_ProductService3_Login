package SpringMVC.productservice3.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    //핸들러 어앱터 호출 전에 호출: preHandle이 True면 다음으로 진행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        //@RequestMapping: HandlerMethod
        //정적 리소스: ResourceHttpRequestHandler
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보를 포함
        }
        log.info("로그 Interceptor 요청 [{}], [{}], [{}]", uuid, requestURI, handler);
        return true;
    }

    //핸들러 어댑터 호출 후 호출
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("로그 Interceptor postHandle [{}]", modelAndView);
    }

    //뷰 렌더링 이후 호출
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);

        log.info("로그 Interceptor 응답 [{}], [{}], [{}]", uuid, requestURI, handler);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
