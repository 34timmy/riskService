package ru.mifi.service.risk.database.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import ru.mifi.service.risk.domain.CompanyParam;
import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.domain.FormulaParam;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface CalculationAccessorMapper {

    @Select("SELECT " +
            "   f.node, f.name, f.calculation, f.formula_type, f.a, f.b, f.c, f.d, f.xb, f.comments " +
            "FROM " +
            "   model_calc mc " +
            "JOIN formula f ON (f.node = mc.node)" +
            "WHERE mc.model_id = #{modelId} AND mc.is_leaf = '1'")
    Formula getFormulaForCalc(String modelId);

    @Select("SELECT " + "   company_ids" +
            "FROM " + "   company_list " +
            "WHERE id = #{companyListId}")
    List<Integer> getCompanyIds(String companyListId);


    //    @Select("SELECT " +
//            "   cbp.company_id, cbp.param_code, cbp.year, cbp.param_value " +
//            "FROM " +
//            "   company_business_params cbp " +
//            "JOIN formula_params fp ON (fp.param_code = cbp.param_code) " +
//            "WHERE fp.node = #{formulaNode} AND cbp.company_id IN #{companyIds}")
//    List<FormulaParam> getFormulaParams(String formulaNode, Set<String> companyIds);

    @Select({
            "<script>", "select", " cbp.company_id, cbp.param_code, cbp.year, cbp.param_value ",
            "FROM company_business_params cbp ",
            "JOIN formula_param fp ON (fp.param_code = cbp.param_code) ",
            "WHERE  fp.node = #{formulaNode} AND cbp.company_id IN  " +
                    "<foreach item='item' index='index' collection='set' open='(' separator=',' close=')'> #{item} </foreach>" +
                    "</script>"})
    @Results({})
    List<CompanyParam> getFormulaParamsScript(@Param("formulaNode")String formulaNode, @Param("set") Set<String> companyIds);
}
