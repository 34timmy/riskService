package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Company {

    private Long id;
    private String INN;
    private String descr;
}
