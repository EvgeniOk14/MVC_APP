package com.example.daocrud.config;

import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@ComponentScan("com.example.daocrud")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer
{
    private final ApplicationContext applicationContext;


    @Autowired
    public SpringConfig(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    /** задаёт параметры БД для соединения с проектом **/
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

    /** создаёт соединение с БД **/
    @Bean
    public JdbcTemplate jdbcTemplate()
    {
        return new JdbcTemplate(dataSource());
    }

    /** задаём путь для css файла **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**") // добавил для css
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/fonts/**") // добавил для шриштов
                .addResourceLocations("classpath:/static/fonts/");

        registry.addResourceHandler("/img/**") // добавил для фотографий
                .addResourceLocations("classpath:/static/img/");
    }

}

