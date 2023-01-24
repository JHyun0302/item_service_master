package hello;

import hello.web.argumentresolver.LoginMemberArgumentResolver;
import hello.web.filter.LogFilter;
import hello.web.interceptor.LogInterceptor;
import hello.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * argumentResolver 작동을 위해 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    /**
     * 스프링 인터셉터 작동을 위해 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //LogInterceptor
        registry.addInterceptor(new LogInterceptor())
                .order(1) //인터셉터 호출 순서
                .addPathPatterns("/**") //인터셉터를 적용할 URL 패턴
                .excludePathPatterns("/css/**", ",*.ico", "/error", "/spring/upload", "/uploaditems/*", "/images/*", "/attach/*"); //인터셉터에서 제외할 패턴

        //LoginCheckInterceptor
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2) //인터셉터 호출 순서
                .addPathPatterns("/**") //인터셉터를 적용할 URL 패턴
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error", "/spring/upload", "/uploaditems/*", "/images/*", "/attach/*"); //인터셉터에서 제외할 패턴
    }

    /**
     * 서블릿 필터를 작동을 위해 등록
     */
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);

        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    /*@Autowired
    LoginCheckFilter loginCheckFilter; //스프링 빈으로 의존관계 주입 형식 - @component 붙이기

    @Bean
    public FilterRegistrationBean LoginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginCheckFilter);
        filterRegistrationBean.setOrder(2); //로그 필터 다음에 로그인 필터 적용

        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
    */

}
