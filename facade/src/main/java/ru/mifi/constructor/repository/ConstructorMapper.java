package ru.mifi.constructor.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.model.Rule;
import ru.mifi.service.risk.domain.Formula;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface ConstructorMapper {

    @Select("SELECT * from company")
    List<Company> getAll() throws SQLException;

    //    -------------------- Model Repository --------------------
    @Select("SELECT * from model")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "rules", javaType = List.class, column = "id",
                            many = @Many(select = "getRules"))
            }
    )
    List<Model> getAllModels() throws SQLException;

    @Update("UPDATE model m SET m.name=#{name} where m.id=#{id}")
    void updateModel(Model model);

    @Insert("INSERT INTO model(ID, NAME)" +
            " values (#{id},#{name}) ")
    void createModel(Model model);

    @Delete("Delete from model m where m.id = #{id}")
    void deleteModel(String id);

//        -------------------- Rule Repository --------------------
    @Select("SELECT * from rule r where r.model_id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "formulas", javaType = List.class, column = "id",
                            many = @Many(select = "getFormulas"))
            }
    )
    List<Rule> getRules(String id);

    @Update("UPDATE rule r SET r.name=#{name} where r.id=#{id}")
    void updateRule(Rule rule);

    @Insert("INSERT INTO rule(ID, NAME, MODEL_ID)" +
            " values (#{id},#{name},#{model_id}) ")
    void createRule(Rule rule);

    @Delete("Delete from rule r where r.id = #{id}")
    void deleteRule(String id);

    //  -------------------- Formula Repository --------------------
    @Select("SELECT f.id,f.descr,f.calculation," +
            "f.formula_type,f.A,f.B,f.C,f.D,f.XB,f.rule_id from formula f where f.rule_id = #{id}")
    List<Formula> getFormulas(String id);

    @Update("UPDATE formula f SET f.descr=#{descr} where f.id=#{id}")
    void updateFormula(Formula formula);

    @Insert("INSERT INTO formula(id, DESCR, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, RULE_ID)" +
            " values (#{id},#{descr},#{calculationFormula},#{formulaType},#{a},#{b},#{c},#{d},#{_XB},#{rule_id}) ")
    void createFormula(Formula formula);

//    TODO DELETE  CASCADE
    @Delete("Delete from formula f where f.node = #{id}")
    void deleteFormula(String id);

}
