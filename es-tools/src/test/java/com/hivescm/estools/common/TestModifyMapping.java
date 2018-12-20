package com.hivescm.estools.common;

import com.google.gson.Gson;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.estools.EsToolsApplication;
import com.hivescm.estools.bean.ModifyMappingResult;
import com.hivescm.estools.dto.modify.MappingDTO;
import com.hivescm.estools.tools.DataType;
import com.hivescm.estools.tools.IndexField;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EsToolsApplication.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class TestModifyMapping {

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ModifyMapping modifyMapping;

    /**
     * 修改索引映射，实现思路：
     * 1. 新建一个新的Index，并将源Index中修改后的mapping使用于新的Index；
     * 2. 为其创建和源Index相同的别名；
     * 3. 删除源Index的别名；
     */
    @Test
    public void testModify4() {
        long st = System.currentTimeMillis();
        String sourceIndex = "my_index_v5";
        String targetIndex = "my_index_v5_tmp";
        String type = "my_index";
        List<MappingDTO> list = new ArrayList<>();
        list.add(new MappingDTO("name", "name", "keyword"));
//        list.add(new MappingBean("", "", "integer"));
        try {
            ModifyMappingResult result = modifyMapping.modifyMappingByTmpIndex(sourceIndex, targetIndex, type, list, new ModifyMapping.ModifyProperties() {

                @Override
                public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
                    for (MappingDTO bean : list) {
                        String type = bean.getNewType();
                        Map<String, Object> value = null;
                        if ("text".equals(type)) {
                            value = IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeywordWithIgnore().getResult();
                        } else if ("date".equals(type)) {
                            value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
                        } else {
                            value = IndexField.make().setType(MappingDTO.getDataType(bean.getNewType())).getResult();
                        }
                        if (properties.containsKey(bean.getOldField())) {
                            properties.put(bean.getNewField(), value);
                        } else {
                            properties.put(bean.getOldField(), value);
                        }
                    }
                    String json = GsonSerialize.INSTANCE.encode(properties);
                    return GsonSerialize.INSTANCE.decode(json, Map.class);
                }
            });
            System.out.println("重建索引结果为：" + new Gson().toJson(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long et = System.currentTimeMillis();
        System.out.println("花费时长：" + (et-st));
    }

    @Test
    public void testModify3() {
        long st = System.currentTimeMillis();
        String sourceIndex = "tms-dispatcher-detail";
        String targetIndex = "tms-dispatcher-detail-tmp";
        String type = "tms-dispatcher-detail-list";
        List<MappingDTO> list = new ArrayList<>();
        list.add(new MappingDTO("acceptDate", "acceptDate", "long"));
        list.add(new MappingDTO("backOrderNum", "backOrderNum", "long"));
        list.add(new MappingDTO("backType", "backType", "keyword"));
        list.add(new MappingDTO("busFee", "busFee", "long"));
        list.add(new MappingDTO("cancelReleaseTime", "cancelReleaseTime", "long"));
        list.add(new MappingDTO("exception", "exception", "boolean"));
        list.add(new MappingDTO("cancelReleaseUser", "cancelReleaseUser", "long"));
        list.add(new MappingDTO("cancelReleaseUserName", "cancelReleaseUserName", "keyword"));
        list.add(new MappingDTO("carrierGlobalName", "carrierGlobalName", "keyword"));
        list.add(new MappingDTO("createUserId", "createUserId", "long"));
        list.add(new MappingDTO("dealerGlobalName", "dealerGlobalName", "keyword"));
        list.add(new MappingDTO("deliveryCharge", "deliveryCharge", "long"));
        list.add(new MappingDTO("destOrgId", "destOrgId", "long"));
        list.add(new MappingDTO("destOrgName", "destOrgName", "keyword"));
        list.add(new MappingDTO("detailStatus", "detailStatus", "long"));
        list.add(new MappingDTO("directSend", "directSend", "boolean"));
        list.add(new MappingDTO("dischargedCode", "dischargedCode", "keyword"));
        list.add(new MappingDTO("dischargedCost", "dischargedCost", "long"));
        list.add(new MappingDTO("dischargedTime", "dischargedTime", "long"));
        list.add(new MappingDTO("idelete", "idelete", "boolean"));
        list.add(new MappingDTO("invoiceCountryId", "invoiceCountryId", "long"));
        list.add(new MappingDTO("invoiceCountryName", "invoiceCountryName", "keyword"));
        list.add(new MappingDTO("invoiceTelNo", "invoiceTelNo", "keyword"));
        list.add(new MappingDTO("invoiceWxNo", "invoiceWxNo", "keyword"));
        list.add(new MappingDTO("istorage", "istorage", "boolean"));
        list.add(new MappingDTO("iwaitNotice", "iwaitNotice", "boolean"));
        list.add(new MappingDTO("isDismantling", "isDismantling", "boolean"));
        list.add(new MappingDTO("isUpdate", "isUpdate", "boolean"));
        list.add(new MappingDTO("lineId", "lineId", "long"));
        list.add(new MappingDTO("lineName", "lineName", "keyword"));
        list.add(new MappingDTO("merchandGlobalName", "merchandGlobalName", "keyword"));
        list.add(new MappingDTO("outputValue", "outputValue", "long"));
        list.add(new MappingDTO("receiptIdentityCard", "receiptIdentityCard", "keyword"));
        list.add(new MappingDTO("shippingCode", "shippingCode", "keyword"));
        list.add(new MappingDTO("shippingCost", "shippingCost", "long"));
        list.add(new MappingDTO("shippingId", "shippingId", "long"));
        list.add(new MappingDTO("skuidCount", "skuidCount", "long"));
        list.add(new MappingDTO("specialRequire", "specialRequire", "keyword"));
        list.add(new MappingDTO("storage", "storage", "boolean"));
        list.add(new MappingDTO("taskId", "taskId", "long"));
        list.add(new MappingDTO("taskName", "taskName", "keyword"));
        list.add(new MappingDTO("vehicleModelName", "vehicleModelName", "keyword"));
        list.add(new MappingDTO("vip", "vip", "boolean"));
        list.add(new MappingDTO("waybillLineId", "waybillLineId", "long"));
        list.add(new MappingDTO("waybillSplit", "waybillSplit", "boolean"));
        list.add(new MappingDTO("waybillType", "waybillType", "long"));
//        list.add(new MappingBean("", "", ""));
        try {
            ModifyMappingResult result = modifyMapping.modifyMappingByTmpIndex(sourceIndex, targetIndex, type, list, new ModifyMapping.ModifyProperties() {
                @Override
                public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
                    for (MappingDTO bean : list) {
                        String type = bean.getNewType();
                        Map<String, Object> value = null;
                        if ("text".equals(type)) {
                            value = IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeywordWithIgnore().getResult();
                        } else if ("date".equals(type)) {
                            value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
                        } else {
                            value = IndexField.make().setType(MappingDTO.getDataType(bean.getNewType())).getResult();
                        }
                        if (properties.containsKey(bean.getOldField())) {
                            properties.put(bean.getNewField(), value);
                        } else {
                            properties.put(bean.getOldField(), value);
                        }
                    }
                    String json = GsonSerialize.INSTANCE.encode(properties);
                    return GsonSerialize.INSTANCE.decode(json, Map.class);
                }
            });
            System.out.println("重建索引结果为：" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long et = System.currentTimeMillis();
        System.out.println("花费时长：" + (et-st));
    }

    @Test
    public void testModify2() {
        long st = System.currentTimeMillis();
        String sourceIndex = "tms-dispatcher-goods";
        String targetIndex = "tms-dispatcher-goods-tmp";
        String type = "tms-dispatcher-goods-list";
        List<MappingDTO> list = new ArrayList<>();
        list.add(new MappingDTO("boxNum", "boxNum", "integer"));
        list.add(new MappingDTO("createUser", "createUser", "long"));
        list.add(new MappingDTO("deliveryNum", "deliveryNum", "long"));
        list.add(new MappingDTO("deliveryVolume", "deliveryVolume", "double"));
        list.add(new MappingDTO("deliveryWeight", "deliveryWeight", "double"));
        list.add(new MappingDTO("dispatcherDetailId", "dispatcherDetailId", "long"));
        list.add(new MappingDTO("dispatcherNum", "dispatcherNum", "long"));
        list.add(new MappingDTO("lockNum", "lockNum", "long"));
        list.add(new MappingDTO("packageNums", "packageNums", "long"));
        list.add(new MappingDTO("packageVolume", "packageVolume", "double"));
        list.add(new MappingDTO("packageWeight", "packageWeight", "double"));
        list.add(new MappingDTO("packages", "packages", "keyword"));
        list.add(new MappingDTO("packingNum", "packingNum", "long"));
        list.add(new MappingDTO("packingType", "packingType", "long"));
        list.add(new MappingDTO("prodType", "prodType", "long"));
        list.add(new MappingDTO("sendNum", "sendNum", "long"));
        list.add(new MappingDTO("sparePartNum", "sparePartNum", "long"));
        list.add(new MappingDTO("stockDetailId", "stockDetailId", "long"));
        list.add(new MappingDTO("stockId", "stockId", "long"));
        list.add(new MappingDTO("stockNum", "stockNum", "long"));
        list.add(new MappingDTO("stockVolume", "stockVolume", "double"));
        list.add(new MappingDTO("stockWeight", "stockWeight", "double"));
        list.add(new MappingDTO("storageLockNum", "storageLockNum", "long"));
        list.add(new MappingDTO("storageNum", "storageNum", "long"));
        list.add(new MappingDTO("unitPriceType", "unitPriceType", "long"));
        list.add(new MappingDTO("updateUser", "updateUser", "long"));
        list.add(new MappingDTO("updateTime", "updateTime", "long"));
        list.add(new MappingDTO("waybillGoodsId", "waybillGoodsId", "long"));
        list.add(new MappingDTO("waybillStockDetailId", "waybillStockDetailId", "long"));
        list.add(new MappingDTO("volumeUnit", "volumeUnit", "keyword"));
        list.add(new MappingDTO("weightUnit", "weightUnit", "keyword"));
        try {
            ModifyMappingResult result = modifyMapping.modifyMappingByTmpIndex(sourceIndex, targetIndex, type, list, new ModifyMapping.ModifyProperties() {
                @Override
                public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
                    for (MappingDTO bean : list) {
                        String type = bean.getNewType();
                        Map<String, Object> value = null;
                        if ("text".equals(type)) {
                            value = IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeywordWithIgnore().getResult();
                        } else if ("date".equals(type)) {
                            value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
                        } else {
                            value = IndexField.make().setType(MappingDTO.getDataType(bean.getNewType())).getResult();
                        }
                        if (properties.containsKey(bean.getOldField())) {
                            properties.put(bean.getNewField(), value);
                        } else {
                            properties.put(bean.getOldField(), value);
                        }
                    }
                    String json = GsonSerialize.INSTANCE.encode(properties);
                    return GsonSerialize.INSTANCE.decode(json, Map.class);
                }
            });
            System.out.println("重建索引结果为：" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long et = System.currentTimeMillis();
        System.out.println("花费时长：" + (et-st));
    }

    /**
     * 修改索引映射，实现思路：
     * 1. 新建一个新的Index，并将源Index中修改后的mapping使用于新的Index；
     * 2. 为其创建和源Index相同的别名；
     * 3. 删除源Index的别名；
     */
    @Test
    public void testModify() {
        long st = System.currentTimeMillis();
        String sourceIndex = "tms-waybill-goods-details";
        String targetIndex = "tms-waybill-goods-details-tmp";
        String type = "tms-waybill-goods-details-list";
        List<MappingDTO> list = new ArrayList<>();
        list.add(new MappingDTO("boxNum", "boxNum", "integer"));
        list.add(new MappingDTO("createUser", "createUser", "integer"));
        list.add(new MappingDTO("createUserName", "createUserName", "keyword"));
        list.add(new MappingDTO("dispatcherNum", "dispatcherNum", "integer"));
        list.add(new MappingDTO("lockNum", "lockNum", "integer"));
        list.add(new MappingDTO("packingNum", "packingNum", "integer"));
        list.add(new MappingDTO("sparePartNum", "sparePartNum", "integer"));
        list.add(new MappingDTO("stockDetailId", "stockDetailId", "long"));
        list.add(new MappingDTO("stockId", "stockId", "long"));
        list.add(new MappingDTO("stockNum", "stockNum", "integer"));
        list.add(new MappingDTO("stockVolume", "stockVolume", "double"));
        list.add(new MappingDTO("stockWeight", "stockWeight", "double"));
        list.add(new MappingDTO("storageLockNum", "storageLockNum", "integer"));
        list.add(new MappingDTO("storageNum", "storageNum", "integer"));
        list.add(new MappingDTO("unitPriceType", "unitPriceType", "integer"));
        list.add(new MappingDTO("updateUser", "updateUser", "integer"));
        list.add(new MappingDTO("updateUserName", "updateUserName", "keyword"));
        list.add(new MappingDTO("volumeUnit", "volumeUnit", "keyword"));
        list.add(new MappingDTO("weightUnit", "weightUnit", "keyword"));
//        list.add(new MappingBean("", "", "integer"));
        try {
            ModifyMappingResult result = modifyMapping.modifyMappingByTmpIndex(sourceIndex, targetIndex, type, list, new ModifyMapping.ModifyProperties() {

                @Override
                public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
                    for (MappingDTO bean : list) {
                        String type = bean.getNewType();
                        Map<String, Object> value = null;
                        if ("text".equals(type)) {
                            value = IndexField.make().setType(DataType.TEXT).setIKAnalyzer().getResult();
                        } else if ("date".equals(type)) {
                            value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
                        } else {
                            value = IndexField.make().setType(MappingDTO.getDataType(bean.getNewType())).getResult();
                        }
                        if (properties.containsKey(bean.getOldField())) {
                            properties.put(bean.getNewField(), value);
                        } else {
                            properties.put(bean.getOldField(), value);
                        }
                    }
//                     for (Map.Entry<String, Object> entry : properties.entrySet()) {
//                        String field = entry.getKey();
//                        Map<String, Object> value = (Map<String, Object>)entry.getValue();
//                        String type = value.get("type").toString();
//                        if ("volume".equals(field) && "keyword".equals(type)) {
//                            System.out.println("field = " + field + "; type = " + type);
//                            entry.setValue(IndexField.make().setType(DataType.FLOAT).getResult());
//                        }
//                        if ("weight".equals(field) && "keyword".equals(type)) {
//                            System.out.println("field = " + field + "; type = " + type);
//                            entry.setValue(IndexField.make().setType("double").getResult());
//                        }
//                        if ("".equals(field) && "long".equals(type)) {
//                            System.out.println("field = " + field + "; type = " + type);
//                            entry.setValue(IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeyword().getResult());
//                        }
//                        if ("".equals(field) && "nested".equals(type)) {
//                            System.out.println("field = " + field + "; type = " + type);
//                            Map<String, Object> childrenProperties = (Map<String, Object>) value.get("properties");
//                            childrenProperties = updateFieldMapping(childrenProperties);
//                            value.put("properties", childrenProperties);
//                        }
//                    }
//                    可增加额外的字段映射
//                    properties.put("collectPayment", IndexField.make().setType("long").getResult());
                    String json = GsonSerialize.INSTANCE.encode(properties);
                    return GsonSerialize.INSTANCE.decode(json, Map.class);
                }
            });
          System.out.println("重建索引结果为：" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long et = System.currentTimeMillis();
        System.out.println("花费时长：" + (et-st));
    }

    /**
     * 测试Scroll查询返回结果
     */
    @Test
    public void queryTypeDataByScroll() {
        String _index = "tms-sign-details";
        String _type = "tms-sign-details-list";
//        QueryBuilder query = QueryBuilders.rangeQuery("age").gte(10).lte(40);
        SearchResponse scrollRes = transportClient.prepareSearch(_index)
                .setTypes(_type)
                .setScroll(new TimeValue(10000))
                .setFetchSource(false)
//                .setQuery(query)
                .setSize(1000)
                .get();
        int i = 0;
        int count = 0;
        do {
            i++;
            if (scrollRes.getHits().getHits().length < 1) {
                return;
            }
            System.out.println("第" + i + "次查询结果如下：");
            for (SearchHit hit : scrollRes.getHits().getHits()) {
                count++;
//                System.out.println(hit.getSourceAsString());
            }

            scrollRes = transportClient.prepareSearchScroll(scrollRes.getScrollId())
                    .setScroll(new TimeValue(10000))
                    .execute().actionGet();
        } while (scrollRes.getHits().getHits().length > 0);
        System.out.println("总条数为：" + count);
    }
}
