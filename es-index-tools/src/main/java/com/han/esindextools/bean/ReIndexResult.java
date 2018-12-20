package com.han.esindextools.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 13:47
 */
@Data
public class ReIndexResult implements Serializable {
    private static final long serialVersionUID = -6554892954122771657L;
    private Long took;
    private Long total;
    private Long created;
    private Long updated;
    private Integer failures;
    private String cause;
    private String causeDetailMessage;

    public ReIndexResult()
    {
    }

    public ReIndexResult(Long took, Long total, Long created)
    {
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
}
