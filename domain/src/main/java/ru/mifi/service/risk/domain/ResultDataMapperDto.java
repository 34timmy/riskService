package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class ResultDataMapperDto {

    private final String userStarted;
    private final String calculatedTime;
    private String modelId;
    private String companyListId;
    private String allCompanyListId;
    private String year;
    private String tableName;
    private String modelName;
    private String modelDescr;

    public ResultDataMapperDto(ResultSet rs) throws SQLException {
        this.modelId = rs.getString("model_id");
        this.companyListId = rs.getString("company_list_id");
        this.allCompanyListId = rs.getString("all_company_list_id");
        this.year = rs.getString("year");
        this.tableName = rs.getString("table_name");
        this.calculatedTime = rs.getString("calculated_time");
        this.userStarted = rs.getString("user_started");
        this.modelName = rs.getString("model_name");
        this.modelDescr = rs.getString("model_descr");

    }
}
