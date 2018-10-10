package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Params {
    private Long id;
    private String name;
}
