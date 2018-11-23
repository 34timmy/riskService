package ru.mifi.service.risk.dto;

import lombok.Getter;

@Getter
public class ModelCalcDto {
    private final String nodeId;
    private final String parentNodeId;
    private final String descr;
    private final Double expertValue;
    private final Double weight;
    private String oldTypeId;

    public ModelCalcDto(String nodeId, String parentNodeId, String descr, Double expertValue, Double weight) {
        this.nodeId = nodeId;
        this.parentNodeId = parentNodeId;
        this.descr = descr;
        this.expertValue = expertValue;
        this.weight = weight;
    }

}
