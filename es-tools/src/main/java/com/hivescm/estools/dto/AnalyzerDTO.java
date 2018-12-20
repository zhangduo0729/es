package com.hivescm.estools.dto;

public class AnalyzerDTO {

    private String index;

    private String field;

    private String text;

    private String analyzer;

    public AnalyzerDTO() {
    }

    public AnalyzerDTO(String index, String field, String text) {
        this.index = index;
        this.field = field;
        this.text = text;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String toString() {
        return "AnalyzerDTO{" +
                "index='" + index + '\'' +
                ", field='" + field + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
