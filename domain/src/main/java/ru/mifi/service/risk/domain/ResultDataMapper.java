package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
public class ResultDataMapper {

    private String modelId;
    private String companyListId;
    private String allCompanyListId;
    private String year;
    private String tableName;

    public ResultDataMapper(ResultSet rs) throws SQLException {
        this.modelId = rs.getString(1);
        this.companyListId = rs.getString(2);
        this.allCompanyListId = rs.getString(3);
        this.year = rs.getString(4);
        this.tableName = rs.getString(5);

    }
}
