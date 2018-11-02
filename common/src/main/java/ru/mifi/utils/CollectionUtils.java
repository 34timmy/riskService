package ru.mifi.utils;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс со статическими методами для работы с коллекциями.
 * Created by DenRUS on 02.11.2018.
 */
public class CollectionUtils {

    @SneakyThrows
    @SafeVarargs
    public static <T> List<T> mergeLists(Class<? extends List> classForList, List<T>... lists) {
        List<T> newList = classForList.newInstance();
        for (List<T> list : lists) {
            newList.addAll(list);
        }
        return newList;
    }
    @SneakyThrows
    @SafeVarargs
    public static <T> List<T> mergeLists(List<T>... lists) {
        return mergeLists(ArrayList.class, lists);
    }
}
