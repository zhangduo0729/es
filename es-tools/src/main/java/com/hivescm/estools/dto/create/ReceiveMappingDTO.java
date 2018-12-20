package com.hivescm.estools.dto.create;

import java.util.List;

public class ReceiveMappingDTO {

    private String indexName;

    private String typeName;

    private List<BasicMappingDTO> dataList;

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

    public List<BasicMappingDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<BasicMappingDTO> dataList) {
        this.dataList = dataList;
    }
}
