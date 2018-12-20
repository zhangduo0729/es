package com.han.esindextools.dto.modify;

import com.han.esindextools.tools.DataType;

import java.util.List;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:06
 */
public class MappingDTO {
    private String oldField;
    private String oldType;
    private String newField;
    private String newType;
    private List<MappingDTO> properties;

    public MappingDTO()
    {
    }

    public MappingDTO(String oldField, String newField, String newType)
    {
        this.oldField = oldField;
        this.newField = newField;
        this.newType = newType;
    }

    public MappingDTO(String oldField, String oldType, String newField, String newType) {
        this.oldField = oldField;
        this.oldType = oldType;
        this.newField = newField;
        this.newType = newType;
    }

    public String getOldField() {
        return this.oldField;
    }

    public void setOldField(String oldField) {
        this.oldField = oldField;
    }

    public String getOldType() {
        return this.oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewField() {
        return this.newField;
    }

    public void setNewField(String newField) {
        this.newField = newField;
    }

    public String getNewType() {
        return this.newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public List<MappingDTO> getProperties() {
        return this.properties;
    }

    public void setProperties(List<MappingDTO> properties) {
        this.properties = properties;
    }

    public static DataType getDataType(String type) {
        DataType dataType = null;
        if (StringUtils.isEmpty(type)) {
            return dataType;
        }
        switch (type) {
            case "boolean":
                dataType = DataType.BOOLEAN;
                break;
            case "byte":
                dataType = DataType.BYTE;
                break;
            case "short":
                dataType = DataType.SHORT;
                break;
            case "integer":
                dataType = DataType.INTEGER;
                break;
            case "long":
                dataType = DataType.LONG;
                break;
            case "float":
                dataType = DataType.FLOAT;
                break;
            case "double":
                dataType = DataType.DOUBLE;
                break;
            case "keyword":
                dataType = DataType.KEYWORD;
                break;
            case "text":
                dataType = DataType.TEXT;
                break;
            case "nested":
                dataType = DataType.NESTED;
                break;
            default:
                dataType = DataType.KEYWORD;
        }

        return dataType;
    }

    public String toString()
    {
        return "{oldField: '" + this.oldField + '\'' + ", oldType: '" + this.oldType + '\'' + ", newField: '" + this.newField + '\'' + ", newType: '" + this.newType + '\'' + ", properties: " + this.properties + '}';
    }
}
