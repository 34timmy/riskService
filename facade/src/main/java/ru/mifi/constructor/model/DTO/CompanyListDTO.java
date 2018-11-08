package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.CompanyList;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CompanyListDTO {
    private String id;
    private String descr;
    private List<Company> companyList;

    public CompanyListDTO(CompanyList companyList) {
        this.id = companyList.getId();
        this.descr = companyList.getDescr();
    }
}
