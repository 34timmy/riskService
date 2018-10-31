package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Model {
    private String id;
    private String descr;
    private List<Rule> rules;
}
