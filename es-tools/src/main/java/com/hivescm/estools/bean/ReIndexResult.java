package com.hivescm.estools.bean;

public class ReIndexResult {

    /**
     * 耗时，毫秒
     */
    private Long took;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 成功创建的条数
     */
    private Long created;

    /**
     * 修改的条数
     */
    private Long updated;

    /**
     * 错误信息条数
     */
    private Integer failures;

    /**
     * 错误信息
     */
    private String cause;

    /**
     * 详细错误信息
     */
    private String causeDetailMessage;

    public ReIndexResult() {
    }

    public ReIndexResult(Long took, Long total, Long created) {
        this.took = took;
        this.total = total;
        this.created = created;
    }

    public ReIndexResult(Long took, Long total, Long created, Integer failures) {
        this.took = took;
        this.total = total;
        this.created = created;
        this.failures = failures;
    }

    public ReIndexResult(Long took, Long total, Long created, Long updated, Integer failures, String cause, String causeDetailMessage) {
        this.took = took;
        this.total = total;
        this.created = created;
        this.updated = updated;
        this.failures = failures;
        this.cause = cause;
        this.causeDetailMessage = causeDetailMessage;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCauseDetailMessage() {
        return causeDetailMessage;
    }

    public void setCauseDetailMessage(String causeDetailMessage) {
        this.causeDetailMessage = causeDetailMessage;
    }
}
