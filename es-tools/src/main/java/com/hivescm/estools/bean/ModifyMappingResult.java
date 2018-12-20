package com.hivescm.estools.bean;


public class ModifyMappingResult {

    /**
     * 获取映射结果
     */
    private Boolean getMappingResult = false;

    /**
     * 临时索引创建结果
     */
    private Boolean createTmpIndexResult;

    /**
     * reIndex的结果封装
     */
    private ReIndexResult reIndexResult;

    public ModifyMappingResult() {
    }

    public ModifyMappingResult(Boolean getMappingResult) {
        this.getMappingResult = getMappingResult;
    }

    public ModifyMappingResult(Boolean getMappingResult, Boolean createTmpIndexResult) {
        this.getMappingResult = getMappingResult;
        this.createTmpIndexResult = createTmpIndexResult;
    }

    public ModifyMappingResult(Boolean getMappingResult, Boolean createTmpIndexResult, ReIndexResult reIndexResult) {
        this.getMappingResult = getMappingResult;
        this.createTmpIndexResult = createTmpIndexResult;
        this.reIndexResult = reIndexResult;
    }

    public Boolean getGetMappingResult() {
        return getMappingResult;
    }

    public void setGetMappingResult(Boolean getMappingResult) {
        this.getMappingResult = getMappingResult;
    }

    public Boolean getCreateTmpIndexResult() {
        return createTmpIndexResult;
    }

    public void setCreateTmpIndexResult(Boolean createTmpIndexResult) {
        this.createTmpIndexResult = createTmpIndexResult;
    }

    public ReIndexResult getReIndexResult() {
        return reIndexResult;
    }

    public void setReIndexResult(ReIndexResult reIndexResult) {
        this.reIndexResult = reIndexResult;
    }
}
