package com.example.swapidemo.service;

import com.example.swapidemo.model.Person;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache extends LinkedHashMap<String, Person>{
    int maxSize;
    public LRUCache(int maxSize) {
        super(maxSize, .75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
}
