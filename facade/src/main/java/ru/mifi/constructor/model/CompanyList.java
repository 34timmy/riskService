package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class CompanyList {
    private String id;
    private String descr;
    private String company_ids;
}
