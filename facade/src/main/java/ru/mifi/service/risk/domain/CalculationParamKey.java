package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Сущность, поля которой явлются ключом расчета.
 * Created by DenRUS on 04.11.2018.
 */
@Getter
@RequiredArgsConstructor
public class CalculationParamKey {
    private final String modelId;
    private final String companyListId;
    private final String allCompaniesListId;
    private final Integer year;

    @Override
    public String toString() {
        return String.format(
                "\nmodelId=%s, \ncompanyListId=%s, \nindustryCompanyListId=%s, \nyear=%s",
                modelId, companyListId, allCompaniesListId, year
        );
    }
}
