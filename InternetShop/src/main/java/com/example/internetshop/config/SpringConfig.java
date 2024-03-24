package com.example.internetshop.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.sql.DataSource;

@ComponentScan("com.example.internetshop")
@Configuration
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer
{
    private final ApplicationContext applicationContext;

    public SpringConfig(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
    @Bean
   public DataSource dataSource()
   {
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       dataSource.setDriverClassName("org.postgresql.Driver");
       dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
       dataSource.setUsername("postgres");
       dataSource.setPassword("oew");
       return dataSource;

   }
    @Bean
    public JdbcTemplate jdbcTemplate()
    {
        return new JdbcTemplate(dataSource());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
