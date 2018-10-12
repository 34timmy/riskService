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
public interface CompanyMapper {

    @Select("SELECT * from company")
    List<Company> getAll() throws SQLException;

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

    @Select("SELECT * from rule where rule.model_id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "name", column = "name"),
                    @Result(property = "formulas", javaType = List.class, column = "id",
                            many = @Many(select = "getFormulas"))
            }
    )
    List<Rule> getRules(String id);

    @Select("SELECT f.node,f.name,f.calculation," +
            "f.formula_type,f.A,f.B,f.C,f.D,f.XB,f.comments from formula f where f.rule_id = #{id}")
    List<Formula> getFormulas(String id);

    @Update("UPDATE formula f SET f.name=#{formula.name} where f.id=#{formula.id}")
    void updateFormula(Formula formula);

    @Insert("INSERT INTO formula(NODE, NAME, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS, RULE_ID)" +
            " values (#{node},#{name},#{calculation},#{formulaType},#{a},#{b},#{c},#{d},#{xb},#{comments},#{rule_id}) ")
    void createFormula(Formula formula);
}
