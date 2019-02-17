package ru.mifi.service.risk.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс с результами расчета, к оторый отдаем на клиент
 * Created by DenRUS on 06.10.2018.
 */
@EqualsAndHashCode
@Getter
public class CalcResultDto implements Serializable {
    private static final String ROOT_NODE = "root";
    private final String companyId;
    private final String node;
    private final String parentNode;
    private final Double weight;
    private final Double interpretationIndex;
    private final Double value;
    private final String normalizedValue;
    private final String lineadValue;
    private final Integer isLeaf;
    private final String comment;
    private final Set<CalcResultDto> children = new HashSet<>();
    private final String nodeName;
    private Integer level = null;


    @SneakyThrows
    public CalcResultDto(ResultSet resultSet) {
        this.companyId = resultSet.getString(1);
        this.node = resultSet.getString(2);
        this.parentNode = resultSet.getString(3);
        this.weight = resultSet.getDouble(4);
        this.isLeaf = resultSet.getInt(5);
        this.comment = resultSet.getString(6);
        this.value = resultSet.getDouble(7);
        this.normalizedValue = String.valueOf(resultSet.getDouble(8));
        this.lineadValue = String.valueOf(resultSet.getDouble(9));
        this.interpretationIndex = resultSet.getDouble(10);
        this.nodeName = resultSet.getString(11);
    }

    /**
     * Отдает результаты вложенно
     * Проставляет связи между объектами на основании полей node/parentNode.
     *
     * @param dtos список объектов dto
     * @return мапа <companyId, результат>
     */
    public static Map<String, CalcResultDto> setLinksAmongDtos(Set<CalcResultDto> dtos) {
        Map<String, CalcResultDto> resultDtoMap = new HashMap<>();
        Map<String, Map<String, CalcResultDto>> idToElemMap = new HashMap<>();
        for (CalcResultDto dto : dtos) {
            Map<String, CalcResultDto> nodesMap = idToElemMap.getOrDefault(dto.getCompanyId(), new HashMap<>());
            nodesMap.put(dto.getNode(), dto);
            idToElemMap.put(dto.getCompanyId(), nodesMap);
        }
        for (Map<String, CalcResultDto> nodesMap : idToElemMap.values()) {
            for (CalcResultDto dto : nodesMap.values()) {
                boolean curDtoIsRoot = dto.getNode().equalsIgnoreCase(ROOT_NODE);
                if (curDtoIsRoot) {
                    resultDtoMap.put(dto.getCompanyId(), dto);
                    continue;
                }

                CalcResultDto parentDto = nodesMap.get(dto.getParentNode());
                if (parentDto == null) {
                    parentDto = nodesMap.get(ROOT_NODE);
                }
                parentDto.addChild(dto);
            }
        }
        return resultDtoMap;
    }

    /**
     * Отдает результаты списком
     * Проставляет связи между объектами на основании полей node/parentNode.
     *
     * @param dtos список объектов dto
     * @return мапа <companyId, результат>
     */
    public static Map<String, List<CalcResultDto>> setDtosAsList(Set<CalcResultDto> dtos) {
        Map<String, CalcResultDto> resultDtoMap = new HashMap<>();

        Map<String, List<CalcResultDto>> idToElemMap = new HashMap<>();
        for (CalcResultDto dto : dtos) {
            List<CalcResultDto> nodesList = idToElemMap.getOrDefault(dto.getCompanyId(), new ArrayList<CalcResultDto>());
            setLevels(dtos);
            nodesList.add(dto);
            idToElemMap.put(dto.getCompanyId(), nodesList);
        }

        return idToElemMap;
    }

    public void addChild(CalcResultDto child) {
        if (
                !this.node.equalsIgnoreCase(child.parentNode)
                        && !(this.getNode().equalsIgnoreCase(ROOT_NODE) && child.getParentNode() == null)
        ) {
            throw new IllegalArgumentException("Переданный объект не является потомком по иерархии! " +
                    "\ncurNode=" + this.node + ", notChildNode=" + child.node);
        }
        children.add(child);
    }

//    public void setLevel(Set<CalcResultDto> dtos) {
//        CalcResultDto obj = this;
//        if (this.getNode().equals("root")) {
//            this.level = 0;
//
//        } else {
//            String parNode;
//            while ((parNode = obj.getParentNode()) != null) {
//                String finalParNode = parNode;
////                if ()
//                obj = dtos.stream().filter(x -> x.getNode().equals(finalParNode)).findFirst().get();
//                this.level++;
//            }
//        }
//    }


    private static void setLevels(Set<CalcResultDto> dtos) {
        dtos.forEach(dto ->
        {
            if (dto.getNode().equals("root")) {
                dto.level = 0;
            } else {
                if (dto.getLevel() == null) {
                    dto.level = 1;
                    while (dto.getParentNode() != null) {
                        Integer tempDtoLvl;
                        CalcResultDto finalDto = dto;
                        CalcResultDto calcResultDto = dtos.stream()
                                .filter(x -> finalDto.getParentNode().equals(x.getNode()))
                                .findFirst().get();
                        if ((tempDtoLvl = calcResultDto.getLevel()) == null) {
                            dto.level++;
                            dto = calcResultDto;
                        } else {
                            dto.level += tempDtoLvl;
                            break;
                        }
                    }
                }
            }
        });
    }
}
