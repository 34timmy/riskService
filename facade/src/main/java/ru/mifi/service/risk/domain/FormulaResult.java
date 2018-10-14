package ru.mifi.service.risk.domain;

public class FormulaResult {
    private String nodeId;
    private String parentNode;
    private String companyId;
    private Double result;

    public String getCompanyId() {
        return companyId;
    }
    public String getNodeId() {
        return nodeId;
    }

    public String getParentNode() {
        return parentNode;
    }

    public Double getResult() {
        return result;
    }

    public FormulaResult(String nodeId, String parentNode, Double result, String companyId) {
        this.nodeId = nodeId;
        this.parentNode = parentNode;
        this.companyId = companyId;
        this.result = result;
    }

    @Override
    public int hashCode() {
        return nodeId.hashCode() * 251
                + parentNode.hashCode() * 43
                + result.hashCode() * 3;
    }
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof FormulaResult)) {
            return false;
        }
        FormulaResult otherResult = (FormulaResult) other;
        return otherResult.nodeId.equals(this.nodeId)
                || otherResult.parentNode.equals(this.parentNode)
                || otherResult.result.equals(this.result);
    }

    public String asFinalResult() {
        return
                String.format(
                        "Результат расчета для компании: %s составил: %s.",
                        companyId,
                        result
                );
    }
}
