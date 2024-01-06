package com.example.swapidemo.service;

import java.util.List;

public interface HttpService {
    String sendSingleGetRequest(String id);

    List<String> sendMultipleGetRequest(List<String> urls);
}
