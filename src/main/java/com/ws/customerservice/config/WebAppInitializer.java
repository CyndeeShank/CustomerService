package com.ws.customerservice.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        //cyndee
        //rootContext.register(ServiceConfig.class, JPAConfig.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register(MvcConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherServlet));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }

    public static class RequestMappingHandlerMapping extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping {
        public RequestMappingHandlerMapping() {
        }

        protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
            RestController restControllerAnnotation = (RestController) AnnotationUtils.findAnnotation(method.getDeclaringClass(), RestController.class);
            if (restControllerAnnotation != null) {
                String[] patterns = (String[]) mapping.getPatternsCondition().getPatterns().toArray(new String[0]);

                for (int i = 0; i < patterns.length; ++i) {
                    patterns[i] = "/rest" + patterns[i];
                }
                mapping = new RequestMappingInfo(mapping.getName(), new PatternsRequestCondition(patterns, this.getUrlPathHelper(), this.getPathMatcher(), this.useSuffixPatternMatch(), this.useTrailingSlashMatch()), mapping.getMethodsCondition(), mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(), mapping.getProducesCondition(), mapping.getCustomCondition());
            }
            super.registerHandlerMethod(handler, method, mapping);
        }
    }

}
