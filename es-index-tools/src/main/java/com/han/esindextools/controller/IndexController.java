package com.han.esindextools.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hivescm.common.domain.DataResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:04
 */
@RestController
@RequestMapping({"index/"})
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    @PostMapping({"mappings"})
    public DataResult receiveMapping(@RequestBody ReceiveMappingDTO bean)
    {
        logger.info("recevice bean content: {}", bean);
        if ((StringUtils.isAnyBlank(new CharSequence[] { bean.getIndexName(), bean.getTypeName() })) ||
                (null == bean
                        .getDataList()) || (0 == bean.getDataList().size())) {
            throw new IllegalArgumentException("请求参数dataList缺失或格式错误");
        }
        return this.indexService.createMapping(bean.getIndexName(), bean.getTypeName(), bean.getDataList());
    }

    @PutMapping({"mappings"})
    public DataResult updateMapping(@RequestBody ModifyMappingDTO bean)
    {
        logger.info("recevice bean content: {}", bean);
        if ((StringUtils.isAnyBlank(new CharSequence[] { bean.getIndexName(), bean.getTypeName() })) ||
                (null == bean
                        .getDataList()) || (0 == bean.getDataList().size())) {
            throw new IllegalArgumentException("请求参数dataList缺失或格式错误");
        }
        return this.indexService.modifyMapping(bean.getIndexName(), bean.getTypeName(), bean.getDataList());
    }

    @GetMapping({"all"})
    public DataResult queryAllIndexName()
    {
        return this.indexService.queryAllIndexName();
    }

    @PostMapping({"specify/analyzer"})
    public DataResult specifyAnalyzer(@RequestBody AnalyzerDTO analyzerDTO)
    {
        logger.info("-----AnalyzerDTO------ {} ", analyzerDTO);
        if (StringUtils.isAnyBlank(new CharSequence[] { analyzerDTO.getIndex(), analyzerDTO.getField(), analyzerDTO.getText() })) {
            throw new IllegalArgumentException("请求参数错误");
        }
        return this.indexService.specifyFieldAnalyzer(analyzerDTO);
    }

    @PostMapping({"customer/analyzer"})
    public DataResult customerAnalyzer(@RequestBody AnalyzerDTO analyzerDTO)
    {
        logger.info("-----AnalyzerDTO------ {} ", analyzerDTO);
        if (StringUtils.isAnyBlank(new CharSequence[] { analyzerDTO.getAnalyzer(), analyzerDTO.getText() })) {
            throw new IllegalArgumentException("请求参数错误");
        }
        return this.indexService.customerAnalyzer(analyzerDTO);
    }

    @DeleteMapping({"multiple"})
    public DataResult deleteIndexByNames(@RequestBody String indexNames)
    {
        if (StringUtils.isBlank(indexNames)) {
            throw new IllegalArgumentException("请求参数错误");
        }
        JSONObject jsonObject = JSONObject.parseObject(indexNames);
        JSONArray indexArray = jsonObject.getJSONArray("indexNames");
        if ((null == indexArray) || (0 == indexArray.size())) {
            throw new IllegalArgumentException("请指定要删除的索引名");
        }
        return this.indexService.deleteIndexByNames((String[])indexArray.toArray(new String[indexArray.size()]));
    }

}
