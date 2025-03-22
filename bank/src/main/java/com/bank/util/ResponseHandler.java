package com.bank.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(HttpStatus status, boolean error, String message, Object response) {
        Map<String, Object> map = new HashMap<>();

        map.put("error", error);
        map.put("message", message);
        map.put("data", response);

        return new ResponseEntity<Object>(map, status);
    }
}
