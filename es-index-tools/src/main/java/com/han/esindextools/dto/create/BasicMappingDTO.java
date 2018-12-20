package com.han.esindextools.dto.create;

import java.util.List;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:05
 */
public class BasicMappingDTO {
    private String field;
    private String type;
    private String analyzer;
    private String format;
    private List<BasicMappingDTO> props;

    public String getField()
    {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnalyzer() {
        return this.analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<BasicMappingDTO> getProps() {
        return this.props;
    }

    public void setProps(List<BasicMappingDTO> props) {
        this.props = props;
    }
}
