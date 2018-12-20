package com.hivescm.estools.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hivescm.common.domain.DataResult;
import com.hivescm.estools.dto.AnalyzerDTO;
import com.hivescm.estools.dto.create.ReceiveMappingDTO;
import com.hivescm.estools.dto.modify.ModifyMappingDTO;
import com.hivescm.estools.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "index/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    /**
     * 创建映射
     * @param bean
     */
    @PostMapping(value = "mappings")
    public DataResult receiveMapping(@RequestBody ReceiveMappingDTO bean) {
        logger.info("recevice bean content: {}", bean);
        if (StringUtils.isAnyBlank(bean.getIndexName(), bean.getTypeName())
                || null == bean.getDataList() || 0 == bean.getDataList().size()) {
            throw new IllegalArgumentException("请求参数dataList缺失或格式错误");
        }
        return indexService.createMapping(bean.getIndexName(), bean.getTypeName(), bean.getDataList());
    }

    /**
     * 修改映射
     * @param bean 接收待修改映射的字段及其类型信息
     */
    @PutMapping(value = "mappings")
    public DataResult updateMapping(@RequestBody ModifyMappingDTO bean) {
        logger.info("recevice bean content: {}", bean);
        if (StringUtils.isAnyBlank(bean.getIndexName(), bean.getTypeName())
                || null == bean.getDataList() || 0 == bean.getDataList().size()) {
            throw new IllegalArgumentException("请求参数dataList缺失或格式错误");
        }
        return indexService.modifyMapping(bean.getIndexName(), bean.getTypeName(), bean.getDataList());
    }

    /**
     * 获取所有的索引名称
     * @return
     */
    @GetMapping(value = "all")
    public DataResult queryAllIndexName() {
        return indexService.queryAllIndexName();
    }

    /**
     * 根据指定索引指定字段的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    @PostMapping(value = "specify/analyzer")
    public DataResult specifyAnalyzer(@RequestBody AnalyzerDTO analyzerDTO) {
        logger.info("-----AnalyzerDTO------ {} ", analyzerDTO);
        if (StringUtils.isAnyBlank(analyzerDTO.getIndex(), analyzerDTO.getField(), analyzerDTO.getText())) {
            throw new IllegalArgumentException("请求参数错误");
        }
        return indexService.specifyFieldAnalyzer(analyzerDTO);
    }

    /**
     * 根据指定的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    @PostMapping(value = "customer/analyzer")
    public DataResult customerAnalyzer(@RequestBody AnalyzerDTO analyzerDTO) {
        logger.info("-----AnalyzerDTO------ {} ", analyzerDTO);
        if (StringUtils.isAnyBlank(analyzerDTO.getAnalyzer(), analyzerDTO.getText())) {
            throw new IllegalArgumentException("请求参数错误");
        }
        return indexService.customerAnalyzer(analyzerDTO);
    }

    /**
     * 批量删除索引
     * @param indexNames
     * @return
     */
    @DeleteMapping(value = "multiple")
    public DataResult deleteIndexByNames(@RequestBody String indexNames) {
        if (StringUtils.isBlank(indexNames)) {
            throw new IllegalArgumentException("请求参数错误");
        }
        JSONObject jsonObject = JSONObject.parseObject(indexNames);
        JSONArray indexArray = jsonObject.getJSONArray("indexNames");
        if (null == indexArray || 0 == indexArray.size()) {
            throw new IllegalArgumentException("请指定要删除的索引名");
        }
        return indexService.deleteIndexByNames(indexArray.toArray(new String[indexArray.size()]));
    }

}
