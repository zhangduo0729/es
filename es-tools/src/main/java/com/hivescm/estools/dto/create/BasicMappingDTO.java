package com.hivescm.estools.dto.create;

import java.util.List;

public class BasicMappingDTO {

    private String field;

    private String type;

    private String analyzer;

    private String format;

    private List<BasicMappingDTO> props;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<BasicMappingDTO> getProps() {
        return props;
    }

    public void setProps(List<BasicMappingDTO> props) {
        this.props = props;
    }
}
