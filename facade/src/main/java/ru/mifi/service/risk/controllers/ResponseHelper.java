package ru.mifi.service.risk.controllers;

import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {

    public static Map<String, Object> successResponse(Object object) {
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("data", object);
        return response;
    }

    public static Map<String, Object> emptyResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        return response;
    }

    public static Map<String, Object> errorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("result", "error");
        response.put("message", errorMessage);
        return response;
    }
}
