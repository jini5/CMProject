package com.example.cmproject.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration //해당 클래스가 설정을 담당하는 클래스임을 나타낸다.
public class QuerydslConfiguration { //Querydsl을 사용하기 위한 설정을 담당하는 클래스

    @PersistenceContext //엔티티 매니저를 주입받기 위한 어노테이션
    private EntityManager entityManager; //엔티티 매니저는 JPA를 사용하여 데이터베이스와 상호작용하기 위한 핵심 객체

    @Bean
    public JPAQueryFactory jpaQueryFactory() { //JPAQueryFactory 빈을 생성하는 메서드
        return new JPAQueryFactory(entityManager); //entityManager을 매개변수로 전달하여 JPAQueryFactory 객체를 생성하고 반환
    }


    //Querydsl 관련 빈은 다른 컴포넌트나 서비스에서 JPAQueryFactory를 주입받아 Querydsl을 사용할 수 있게 된다.
    //Querydsl은 객체지향적인 방식으로 쿼리를 작성하고, 타입 안정성을 제공하여 데이터베이스와의 상호작용을 편리하게 만들어준다.
}