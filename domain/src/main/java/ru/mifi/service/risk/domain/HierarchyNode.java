package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class HierarchyNode {
    private final String id;
    private final Double weight;
    private final Double interpretationK;
    private final List<String> comments;
}
