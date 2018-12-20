package com.han.esindextools.dto;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:06
 */
public class AnalyzerDTO {
    private String index;
    private String field;
    private String text;
    private String analyzer;

    public AnalyzerDTO()
    {
    }

    public AnalyzerDTO(String index, String field, String text)
    {
        this.index = index;
        this.field = field;
        this.text = text;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnalyzer() {
        return this.analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String toString()
    {
        return "AnalyzerDTO{index='" + this.index + '\'' + ", field='" + this.field + '\'' + ", text='" + this.text + '\'' + '}';
    }
}
