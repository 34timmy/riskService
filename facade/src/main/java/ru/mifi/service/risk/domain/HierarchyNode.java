package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HierarchyNode {
    private final String id;
    private final Double weight;
}
