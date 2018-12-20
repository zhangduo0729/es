package com.hivescm.estools.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.estools.tools.DataType;
import com.hivescm.estools.tools.IndexField;
import com.hivescm.estools.tools.IndexStruct;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestCreateMapping {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testMapping() {
        IndexStruct struct = IndexStruct.make("a", "b");
        struct.addColumn("id", IndexField.make().setType(DataType.LONG));
        struct.addColumn("name", IndexField.make().setType(DataType.KEYWORD));
        struct.addColumn("desc", IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeywordWithIgnore());
        struct.addColumn("createTime", IndexField.make().setType(DataType.DATE).setFormat("yyyy-MM-dd HH:mm:ss"));
        {
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", IndexField.make().setType(DataType.TEXT).getResult());
            properties.put("time", IndexField.make().setType(DataType.DATE).getResult());
            properties.put("stars", IndexField.make().setType(DataType.INTEGER).getResult());
            struct.addColumn("comments", IndexField.make().setType(DataType.NESTED).put("properties", properties));
        }
        Map<String, Object> map = struct.getMappings();
        System.out.println("创建映射结果为：" + map);
    }
}
