package com.example.swapidemo.service;

import com.example.swapidemo.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class CacheServiceImpl implements CacheService<String, Person> {

    @Value("${cache.size}")
    private int maxSize;

    private final ReentrantLock lock = new ReentrantLock();
    private LRUCache lruCache;

    @PostConstruct
    void init() {
        lruCache = new LRUCache(maxSize);
    }

    @Override
    public Person get(String key) {
        lock.lock();
        try {
            return lruCache.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(String key, Person value) {
        lock.lock();
        try {
            lruCache.put(key, value);
        } finally {
            lock.unlock();
        }
    }
}
