package SpringMVC.productservice3.web.config;

import SpringMVC.productservice3.web.argumentresolver.LoginMemberArgumentResolver;
import SpringMVC.productservice3.web.filter.LogFilter;
import SpringMVC.productservice3.web.filter.LoginCheckFilter;
import SpringMVC.productservice3.web.interceptor.LogInterceptor;
import SpringMVC.productservice3.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //로그 인터셉터 사용을 위해 필터 등록 해제
    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        filterFilterRegistrationBean.setFilter(new LogFilter()); //적용할 필터 설정
        filterFilterRegistrationBean.setOrder(1); //순서 설정
        filterFilterRegistrationBean.addUrlPatterns("/*"); //적용 URL 설정 ==> 전체

        return filterFilterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        filterFilterRegistrationBean.setFilter(new LoginCheckFilter()); //적용할 필터 설정
        filterFilterRegistrationBean.setOrder(2); //순서 설정
        filterFilterRegistrationBean.addUrlPatterns("/*"); //적용 URL 설정 ==> 전체

        return filterFilterRegistrationBean;
    }

    //인터셉터 override
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //모든 URL
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") //모든 URL
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/",
                        "/members/add", "/login", "/logout");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
