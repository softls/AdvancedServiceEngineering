package at.ac.tuwien.infosys.ui;

/**
 * Created by lenaskarlat on 4/10/17.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is used to inject a model attribute containing the current view name, which is used in the Thymeleaf
 * templates (in order to implement the navigation correctly: the navigation link for the current (active) view is
 * highlighted using a CSS class).
 */
@Configuration
public class ViewNameInterceptor extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
                    modelAndView.addObject("viewName", modelAndView.getViewName());
                }
            }
        });
        super.addInterceptors(registry);
    }
}