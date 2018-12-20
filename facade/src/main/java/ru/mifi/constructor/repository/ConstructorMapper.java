package ru.mifi.constructor.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.mifi.constructor.model.*;
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
                    @Result(property = "modelCalcs", javaType = List.class, column = "id",
                            many = @Many(select = "getModelCalcs"))
            }
    )
    List<Model> getAllModels() throws SQLException;


    @Select("SELECT * from model")
    List<Model> getAllModelsOnly() throws SQLException;

    @Select("SELECT * FROM model m WHERE m.id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "id", column = "id"),
                    @Result(property = "modelCalcs", javaType = List.class, column = "id",
                            many = @Many(select = "getModelCalcs"))
            }
    )
    Model getModel(String id);

    @Update("UPDATE model m SET m.descr=#{descr} where m.id=#{id}")
    void updateModel(Model model);

    @Insert("INSERT INTO model(ID, DESCR)" +
            " values (#{id},#{descr}) ")
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
//    @Select("SELECT f.id,f.descr,f.calculation," +
//            "f.formula_type,f.A,f.B,f.C,f.D,f.XB,f.rule_id,f.model_calc_id from formula f where f.model_calc_id = #{id}")
//    List<Formula> getFormulas(String id);

    @Select("SELECT f.id,f.descr,f.calculation," +
            "f.formula_type,f.A,f.B,f.C,f.D,f.XB from formula f")
    @Results({
            @Result(property = "calculationFormula", column = "calculation"),
            @Result(property = "formulaType", column = "formula_type"),
            @Result(property = "_XB", column = "xb")
    })
    List<Formula> getAllFormulas();

    @Update("UPDATE formula f SET f.descr=#{descr}" +
            ",f.calculation = #{calculation}" +
            ",f.formula_type = #{formulaType}" +
            ",f.A = #{a}" +
            ",f.B = #{b}" +
            ",f.C = #{c}" +
            ",f.D = #{d}" +
            ",f.XB = #{xb}" +
            " where f.id=#{id}")
    void updateFormula(Formula formula);

    @Insert("INSERT INTO formula(id, DESCR, CALCULATION, FORMULA_TYPE, A, B, C, D, XB)" +
            " values (#{id},#{descr},#{calculationFormula},#{formulaType},#{a},#{b},#{c},#{d},#{_XB}) ")
    void createFormula(Formula formula);

    //    TODO DELETE  CASCADE
    @Delete("Delete from formula f where f.id = #{id}")
    void deleteFormula(String id);

    @Select("SELECT * FROM normative_parameters np ")
    List<NormParam> getAllNormParams();

    //        -------------------- ModelCalc Repository --------------------
    @Select("SELECT * from model_calc mc where mc.model_id = #{id}")
    @Results(
            {
                    @Result(id = true, property = "node", column = "node"),
            }
    )
    List<ModelCalc> getModelCalcs(String id);

    @Update("UPDATE model_calc mc SET mc.descr=#{descr}, mc.weight=#{weight} where mc.node=#{node}")
    void updateModelCalc(ModelCalc modelCalc);

    @Insert("INSERT INTO model_calc(NODE, DESCR, MODEL_ID, PARENT_ID, WEIGHT, IS_LEAF)" +
            " values (#{node},#{descr},#{model_id},#{parent_id}, #{weight},#{is_leaf}) ")
    void createModelCalc(ModelCalc modelCalc);

    @Delete("Delete from model_calc mc where mc.node = #{node}")
    void deleteModelCalc(String id);

//        -------------------- CompanyList Repository --------------------

    @Select("SELECT * from company_list")
    List<CompanyList> getAllCompanyLists();

    @Insert("INSERT INTO company_list(id,company_ids,descr)" +
            " values (#{id},#{company_ids},#{descr})")
    void createCompanyList(CompanyList companyList);

    @Select("SELECT * FROM company_business_data cbd WHERE cbd.company_id = #{id}")
    List<CompanyData> getAllDataForCompany(String id);

    //        -------------------- Company Repository --------------------
    @Select("SELECT * FROM company")
    List<Company> getAllCompanies();

    @Select("SELECT * FROM industries")
    List<Industry> getAllIndustries();

    @Delete("DELETE FROM company_list cl where cl.id = #{id}")
    void deleteCompanyList(String id);
}

