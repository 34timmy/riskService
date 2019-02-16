package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class Model {
    private String id;
    private String descr;
    private String role;
    private List<ModelCalc> modelCalcs;
}
