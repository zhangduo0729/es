package com.han.esindextools.service;

import com.han.esindextools.dto.AnalyzerDTO;
import com.han.esindextools.dto.create.BasicMappingDTO;
import com.han.esindextools.dto.modify.MappingDTO;
import com.han.esindextools.tools.QueryObject;
import com.hivescm.common.domain.DataResult;

import java.util.List;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:09
 */
public interface IndexService {
    public abstract DataResult createMapping(String paramString1, String paramString2, List<BasicMappingDTO> paramList);

    public abstract DataResult modifyMapping(String paramString1, String paramString2, List<MappingDTO> paramList);

    public abstract DataResult queryAllIndexName();

    public abstract DataResult specifyFieldAnalyzer(AnalyzerDTO paramAnalyzerDTO);

    public abstract DataResult customerAnalyzer(AnalyzerDTO paramAnalyzerDTO);

    public abstract DataResult deleteIndexByNames(String[] paramArrayOfString);

    public abstract DataResult deleteByQuery(String paramString, QueryObject paramQueryObject);
}
