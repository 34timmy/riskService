package ru.mifi.service.risk.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Конкретный параметр компании
 * Created by DenRUS on 08.10.2018.
 */
public class CompanyParam {
    String paramCode;
    String companyId;
    Integer year;
    String paramValue;

    public CompanyParam(String paramCode, String companyId, Integer year, String paramValue) {
        this.paramCode = paramCode;
        this.companyId = companyId;
        this.year = year;
        this.paramValue = paramValue;
    }

    public CompanyParam(ResultSet resultSet) throws SQLException {
        this.paramCode = resultSet.getString("param_code");
        this.companyId = resultSet.getString("company_id");
        this.year = resultSet.getInt("year");
        this.paramValue = resultSet.getString("param_value");
    }
}
