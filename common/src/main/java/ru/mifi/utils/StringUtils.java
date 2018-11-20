package ru.mifi.utils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс со статическими методами для работы со строками.
 * Created by DenRUS on 02.11.2018.
 */
public class StringUtils {
    public static Boolean isNullOrEmpty(String val) {
        return val == null || val.isEmpty();
    }
    /**
     * Достаем по строке сет комментарием
     * @param commentsAsString комметарии через ;
     * @return сет комментов
     */
    public static Set<String> extractComments(String commentsAsString) {
        if (commentsAsString == null) {
            return null;
        }
        return Arrays.stream(commentsAsString.split(";")).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
