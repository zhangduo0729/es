package com.hivescm.estools.dto.modify;

import java.util.List;

public class ModifyMappingDTO {

    private String indexName;

    private String typeName;

    private List<MappingDTO> dataList;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<MappingDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<MappingDTO> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "{" +
                "indexName: '" + indexName + '\'' +
                ", typeName: '" + typeName + '\'' +
                ", dataList: " + dataList +
                '}';
    }
}
