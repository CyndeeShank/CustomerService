package com.ws.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.ws.customerservice" })
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean
//    public InternalResourceViewResolver jspViewResolver() {
//        InternalResourceViewResolver bean = new InternalResourceViewResolver();
//        bean.setPrefix("/WEB-INF/views/");
//        bean.setSuffix(".jsp");
//        return bean;
//    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:messages");
        resource.setDefaultEncoding("UTF-8");
        return resource;
    }

    @Bean
    DataSource getEMSDataSource() {
        Object dataSource = null;

        try {
            dataSource = new DriverManagerDataSource("jdbc:jtds:sqlserver://DEV02:1444;databaseName=EMS", "esssyn", "wtsla64");
            //dataSource = new DriverManagerDataSource("jdbc:jtds:sqlserver://DEV02:1444;databaseName=EMS", "wsdemandware", "wsdemandware");
            ((DriverManagerDataSource)dataSource).setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource)dataSource;
    }

    @Bean
    JdbcTemplate getEMSJdbcTemplate() {
        return new JdbcTemplate(this.getEMSDataSource());
    }

    @Bean
    DataSource getEMSProdDataSource() {
        Object dataSource = null;

        try {
            //dataSource = new DriverManagerDataSource ("jdbc:jtds:sqlserver://LVVMEMS01:1433;instanceName:EMS01;databaseName=EMS", "app_ems", "n5$qjEMS");
            dataSource = new DriverManagerDataSource("jdbc:jtds:sqlserver://WSEMS01:1433;databaseName=EMS", "esssyn", "wtsla64");
            //dataSource = new DriverManagerDataSource("jdbc:jtds:sqlserver://WSEMS01:1433;databaseName=EMS", "wsdemandware", "wsdemandware");
            ((DriverManagerDataSource)dataSource).setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource)dataSource;
    }

    @Bean
    JdbcTemplate getEMSProdJdbcTemplate() {
        return new JdbcTemplate(this.getEMSProdDataSource());
    }

    @Bean
    DataSource getXBRDataSource() {
        Object dataSource = null;

        try {
            dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@lvoxbrdbp1:1521:xbrprd", "customer_service_updt", "K33pS3cr3t!");
            ((DriverManagerDataSource)dataSource).setDriverClassName("oracle.jdbc.driver.OracleDriver");
            /**
             dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@lvoxbrdbd1:1521:xbrdev", "cshank", "Wetseal1!");
             dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@lvoxbrdbp1:1521:xbrprd", "query", "readonly");
             <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"/>
             <property name="url" value="jdbc:oracle:thin:@lvormsdbp1:1521:ormsprd"/>
             <property name="url" value="jdbc:oracle:thin:@lvoxbrdbp1:1521:xbrprd" />
             <property name="username" value="query"/>
             <property name="password" value="readonly"/>
             **/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource)dataSource;
    }

    @Bean
    JdbcTemplate getXBRJdbcTemplate() {
        return new JdbcTemplate(this.getXBRDataSource());
    }

    @Bean
    DataSource getORMSDataSource() {
        Object dataSource = null;

        try {
            dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@lvormsdbp1:1521:ormsprd", "query", "readonly");
            ((DriverManagerDataSource)dataSource).setDriverClassName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource)dataSource;
    }

    @Bean
    JdbcTemplate getORMSJdbcTemplate() {
        return new JdbcTemplate(this.getORMSDataSource());
    }

    @Bean
    AppConfig getAppConfig() {
        AppConfig appConfig = new AppConfig();

        appConfig.setLocalDirectory("/Users/cshank/Projects/Repo/CustomerService/src/main/webapp/resources");
        appConfig.setGcHistoryUrl("https://svcs.wetseal.com/GiftCardService/dispatcher/getHistoryAndBalance");
        appConfig.setGcActivateUrl("https://svcs.wetseal.com/GiftCardService/dispatcher/activateEcomCard");

        return appConfig;
    }

    @Bean
    RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
