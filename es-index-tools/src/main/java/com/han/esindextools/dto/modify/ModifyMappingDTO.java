package com.han.esindextools.dto.modify;

import java.util.List;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:06
 */
public class ModifyMappingDTO {
    private String indexName;
    private String typeName;
    private List<MappingDTO> dataList;

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

    public List<MappingDTO> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<MappingDTO> dataList) {
        this.dataList = dataList;
    }

    public String toString()
    {
        return "{indexName: '" + this.indexName + '\'' + ", typeName: '" + this.typeName + '\'' + ", dataList: " + this.dataList + '}';
    }
}
