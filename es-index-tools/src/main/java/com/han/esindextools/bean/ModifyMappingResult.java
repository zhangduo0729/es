package com.han.esindextools.bean;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 13:47
 */
public class ModifyMappingResult {
    private Boolean getMappingResult = Boolean.valueOf(false);
    private Boolean createTmpIndexResult;
    private ReIndexResult reIndexResult;

    public ModifyMappingResult()
    {
    }

    public ModifyMappingResult(Boolean getMappingResult)
    {
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
        return this.getMappingResult;
    }

    public void setGetMappingResult(Boolean getMappingResult) {
        this.getMappingResult = getMappingResult;
    }

    public Boolean getCreateTmpIndexResult() {
        return this.createTmpIndexResult;
    }

    public void setCreateTmpIndexResult(Boolean createTmpIndexResult) {
        this.createTmpIndexResult = createTmpIndexResult;
    }

    public ReIndexResult getReIndexResult() {
        return this.reIndexResult;
    }

    public void setReIndexResult(ReIndexResult reIndexResult) {
        this.reIndexResult = reIndexResult;
    }
}
