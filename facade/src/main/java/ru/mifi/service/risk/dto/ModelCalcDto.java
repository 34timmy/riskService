package ru.mifi.service.risk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
public class ModelCalcDto {
    private final String nodeId;
    private final String parentNodeId;
    private final String descr;
    private final Double expertValue;
    private final Double weight;
    private final Integer level;
    @Setter
    private String indexCode;

    public ModelCalcDto(
            String nodeId,
            String parentNodeId,
            String descr,
            Double expertValue,
            Double weight,
            Integer level
    ) {
        this.nodeId = nodeId;
        this.parentNodeId = parentNodeId;
        this.descr = descr;
        this.expertValue = expertValue;
        this.weight = weight;
        this.level = level;
    }


    /**
     * Проставляем идентификаторы старого образца.
     * @param dtos коллекция объектов, которым проставляем.
     */
    public static Collection<ModelCalcDto> initIndexCode(Collection<ModelCalcDto> dtos) {
        Map<String, ModelCalcDto> idToDto = dtos.stream()
                .collect(Collectors.toMap(
                        ModelCalcDto::getNodeId,
                        dto -> dto
                ));
        AtomicInteger cacheNum = new AtomicInteger(1);
        AtomicReference<String> curParent = new AtomicReference<>();
        dtos.stream()
                .sorted(ModelCalcDto::safeCompare)
                .forEach(dto -> {
                    if (dto.getParentNodeId() == null) {
                        dto.setIndexCode(cacheNum.getAndIncrement() + "");
                        return;
                    }
                    String parentNodeId = dto.getParentNodeId();
                    if (!Objects.equals(parentNodeId, curParent.get())) {
                        curParent.set(parentNodeId);
                        cacheNum.set(1);
                    }
                    dto.setIndexCode(idToDto.get(parentNodeId).getIndexCode() + "." + cacheNum.getAndIncrement());
                });
        return dtos;

    }

    private static int safeCompare(ModelCalcDto a, ModelCalcDto b) {
        if (a.level == null && b.level == null) {
            return 0;
        }
        if (a.level == null && b.level != null) {
            return 1;
        }
        if (a.level != null && b.level == null) {
            return -1;
        }
        return Integer.compare(a.level, b.level);
    }

}
