package com.han.esindextools.dto.create;

import java.util.List;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:05
 */
public class ReceiveMappingDTO {
    private String indexName;
    private String typeName;
    private List<BasicMappingDTO> dataList;

    public String getIndexName()
    {
        return this.indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<BasicMappingDTO> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<BasicMappingDTO> dataList) {
        this.dataList = dataList;
    }
}
