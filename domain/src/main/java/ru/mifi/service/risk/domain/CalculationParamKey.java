package ru.mifi.service.risk.domain;

import lombok.Getter;

/**
 * Сущность, поля которой явлются ключом расчета.
 * Created by DenRUS on 04.11.2018.
 */
@Getter
public class CalculationParamKey {
    private final String modelId;
    private final String companyListId;
    private String allCompaniesListId = null;
    private final Integer year;
    private String industry = null;

    /**
     * Конструктор по обоим спискам компаний.
     *
     * @param modelId            идентификатор модели
     * @param companyListId      идентификатор списка компаний для участия в расчете
     * @param allCompaniesListId идентификатор списка всех компаний (отрасли/общего списка)
     * @param year               год расчета
     */
    public CalculationParamKey(String modelId, String companyListId, String allCompaniesListId, Integer year) {
        this.modelId = modelId;
        this.companyListId = companyListId;
        this.allCompaniesListId = allCompaniesListId;
        this.year = year;
    }

    /**
     * /**
     * Конструктор по обоим спискам компаний.
     *
     * @param modelId       идентификатор модели
     * @param companyListId идентификатор списка компаний для участия в расчете
     * @param year          год расчета
     * @param industry      идентификатор отрасли
     */
    public CalculationParamKey(String modelId, String companyListId, Integer year, String industry) {
        this.modelId = modelId;
        this.companyListId = companyListId;
        this.year = year;
        this.industry = industry;
    }

    @Override
    public String toString() {
        return String.format(
                "\nmodelId=%s, \ncompanyListId=%s, \nindustryCompanyListId=%s, \nyear=%s",
                modelId, companyListId, allCompaniesListId, year
        );
    }
}
