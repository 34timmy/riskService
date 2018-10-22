package ru.mifi.constructor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.constructor.repository.ConstructorMapper;
import ru.mifi.service.risk.domain.Formula;

import java.util.Random;

@Service
public class FormulaService {

    @Autowired
    ConstructorMapper constructorMapper;

    @Transactional
    public void updateFormula(Formula formula) {

        constructorMapper.updateFormula(formula);
    }

    @Transactional
    public void createFormula(Formula formula) {
//        TODO node =? (id) must be autogenerated
        formula.setNode(new Random().nextInt() + "");
        constructorMapper.createFormula(formula);
    }

    @Transactional
    public void deleteFormula(String id) {
        constructorMapper.deleteFormula(id);
    }
}
