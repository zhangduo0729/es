package com.hivescm.estools.dto.modify;

import com.hivescm.estools.tools.DataType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MappingDTO {

    private String oldField;

    private String oldType;

    private String newField;

    private String newType;

    private List<MappingDTO> properties;

    public MappingDTO() {
    }

    public MappingDTO(String oldField, String newField, String newType) {
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
        return oldField;
    }

    public void setOldField(String oldField) {
        this.oldField = oldField;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewField() {
        return newField;
    }

    public void setNewField(String newField) {
        this.newField = newField;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public List<MappingDTO> getProperties() {
        return properties;
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
            case "boolean" :
                dataType = DataType.BOOLEAN;
                break;
            case "byte" :
                dataType = DataType.BYTE;
                break;
            case "short" :
                dataType = DataType.SHORT;
                break;
            case "integer" :
                dataType = DataType.INTEGER;
                break;
            case "long" :
                dataType = DataType.LONG;
                break;
            case "float" :
                dataType = DataType.FLOAT;
                break;
            case "double" :
                dataType = DataType.DOUBLE;
                break;
            case "keyword" :
                dataType = DataType.KEYWORD;
                break;
            case "text" :
                dataType = DataType.TEXT;
                break;
            case "nested" :
                dataType = DataType.NESTED;
                break;
            default:
                dataType = DataType.KEYWORD;
                break;
        }
        return dataType;
    }

    @Override
    public String toString() {
        return "{" +
                "oldField: '" + oldField + '\'' +
                ", oldType: '" + oldType + '\'' +
                ", newField: '" + newField + '\'' +
                ", newType: '" + newType + '\'' +
                ", properties: " + properties +
                '}';
    }
}
