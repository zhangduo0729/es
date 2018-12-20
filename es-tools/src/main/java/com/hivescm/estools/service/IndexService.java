package com.hivescm.estools.service;

import com.hivescm.common.domain.DataResult;
import com.hivescm.estools.dto.AnalyzerDTO;
import com.hivescm.estools.dto.create.BasicMappingDTO;
import com.hivescm.estools.dto.modify.MappingDTO;
import com.hivescm.essdkservice.common.bean.QueryObject;

import java.util.List;

public interface IndexService {

    DataResult createMapping(String index, String type, List<BasicMappingDTO> list);

    /**
     * 修改映射
     * @param index
     * @param type
     * @param list 待修改映射的字段集合
     * @return
     */
    DataResult modifyMapping(String index, String type, List<MappingDTO> list);

    /**
     * 获取所有的索引名称
     * @return
     */
    DataResult queryAllIndexName();

    /**
     * 根据指定索引指定字段的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    DataResult specifyFieldAnalyzer(AnalyzerDTO analyzerDTO);

    /**
     * 根据指定的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    DataResult customerAnalyzer(AnalyzerDTO analyzerDTO);

    /**
     * 批量删除索引
     * @param indexNames
     * @return
     */
    DataResult deleteIndexByNames(String[] indexNames);

    /**
     * 根据条件删除指定index下的数据
     * @param index
     * @param queryObject
     * @return
     */
    DataResult deleteByQuery(String index, QueryObject queryObject);
}
