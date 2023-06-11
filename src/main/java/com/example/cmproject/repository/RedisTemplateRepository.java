package com.example.cmproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;


@Configuration //Spring의 구성 클래스임을 나타내는 어노테이션
@RequiredArgsConstructor //필드들을 기반으로 생성자를 자동으로 생성해주는 Lombok 어노테이션
public class RedisTemplateRepository { //Redis 데이터베이스와 상호작용하기 위한 메서드를 제공하는 리포지토리(데이터 접근 계층) 클래스
//클래스는 Spring의 RedisTemplate을 사용하여 Redis 데이터베이스와 상호작용하는 메서드를 제공하여 데이터의 저장, 검색, 삭제 등의 작업을 수행한다.
    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) { //주어진 키를 사용하여 Redis에서 데이터를 가져온다.
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) { //주어진 키와 값을 사용하여 Redis에 데이터를 설정한다.
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        //주어진 키와 값을 사용하여 Redis에 데이터를 설정하고, 지정된 기간 후에 데이터가 만료된다.
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {//주어진 키를 사용하여 Redis에서 데이터를 삭제한다.
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {// 주어진 키가 Redis에 존재하는지 확인
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
