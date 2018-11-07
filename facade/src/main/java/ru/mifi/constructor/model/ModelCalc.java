package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mifi.service.risk.domain.Formula;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ModelCalc {
    private String model_id;
    private String descr;
    private String node;
    private String parent_node;
    private double weight;
    private int level;
    private boolean is_leaf;
}
