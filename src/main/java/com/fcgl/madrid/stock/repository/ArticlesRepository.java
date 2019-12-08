package com.fcgl.madrid.stock.repository;

import com.fcgl.madrid.stock.model.IArticleBody;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticlesRepository {
    private HashOperations hashOperations;
    private RedisTemplate redisTemplate;
    private final static String LAST_ARTICLE = "LAST";

    public ArticlesRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void put(IArticleBody articleBody) {
        hashOperations.put(articleBody.getName(), LAST_ARTICLE, articleBody.getArticleUrl());
    }

    public String getLastArticle(String name) {
        return (String) hashOperations.get(name, LAST_ARTICLE);
    }

}
