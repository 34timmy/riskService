package ru.mifi.service.risk.dto;

public class ModelDto {

    private final String modelId;
    private final String modelName;
    private final String modelDescr;

    public ModelDto(String modelId, String modelName, String modelDescr) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelDescr = modelDescr;
    }
}
