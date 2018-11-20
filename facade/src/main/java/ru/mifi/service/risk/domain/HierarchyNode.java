package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class HierarchyNode {
    private final String id;
    private final Double weight;
    private final Set<String> comments;
}
