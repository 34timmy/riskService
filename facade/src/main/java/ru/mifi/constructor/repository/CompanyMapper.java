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
                    @Result( property = "name", column = "name"),
                    @Result(property = "rules", javaType = List.class, column = "id",
                            many = @Many(select = "getRules"))
            }
    )
    List<Model> getAllModels() throws SQLException;

    @Select("SELECT * from rule where rule.model_id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result( property = "name", column = "name"),
                    @Result(property = "formulas", javaType = List.class, column = "id",
                            many = @Many(select = "getFormulas"))
            }
    )
    List<Rule> getRules(String id);

    @Select("SELECT * from formula where formula.rule_id = #{id}")
    List<Formula> getFormulas(String id);

}
