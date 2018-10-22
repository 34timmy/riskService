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
    @Select("SELECT * from models")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "rules", javaType = List.class, column = "id",
                            many = @Many(select = "getRules"))
            }
    )
    List<Model> getAllModels() throws SQLException;

    @Update("UPDATE models m SET m.name=#{model.name} where m.id=#{model.id}")
    void updateModel(Model model);

    @Insert("INSERT INTO models(ID, NAME)" +
            " values (#{id},#{name}) ")
    void createModel(Model model);

    @Delete("Delete from models m where m.id = #{id}")
    void deleteModel(String id);

//        -------------------- Rule Repository --------------------
    @Select("SELECT * from rules r where r.model_id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "formulas", javaType = List.class, column = "id",
                            many = @Many(select = "getFormulas"))
            }
    )
    List<Rule> getRules(String id);

    @Update("UPDATE models m SET m.name=#{model.name} where m.id=#{model.id}")
    void updateRule(Rule rule);

    @Insert("INSERT INTO models(ID, NAME)" +
            " values (#{id},#{name}) ")
    void createRule(Rule rule);

    @Delete("Delete from rules r where r.id = #{id}")
    void deleteRule(String id);

    //  -------------------- Formula Repository --------------------
    @Select("SELECT f.node,f.name,f.calculation," +
            "f.formula_type,f.A,f.B,f.C,f.D,f.XB,f.comments from formulas f where f.rule_id = #{id}")
    List<Formula> getFormulas(String id);

    @Update("UPDATE formulas f SET f.name=#{formula.name} where f.id=#{formula.id}")
    void updateFormula(Formula formula);

    @Insert("INSERT INTO formulas(NODE, NAME, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS, RULE_ID)" +
            " values (#{node},#{name},#{calculation},#{formulaType},#{a},#{b},#{c},#{d},#{xb},#{comments},#{rule_id}) ")
    void createFormula(Formula formula);

//    TODO DELETE  CASCADE
    @Delete("Delete from formulas f where f.node = #{id}")
    void deleteFormula(String id);

}
