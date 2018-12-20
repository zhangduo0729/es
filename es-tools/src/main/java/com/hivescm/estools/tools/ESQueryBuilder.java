package com.hivescm.estools.tools;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import com.hivescm.essdkservice.common.bean.QueryObject;
import com.hivescm.essdkservice.common.bean.MultiQueryObject;

import java.util.Arrays;
import java.util.List;

public class ESQueryBuilder {

    /**
     * 将QueryObject对象转换成QueryBuilder对象
     * @param queryObject
     * @return
     */
    public static void objectToQueryBuilder(List<QueryBuilder> queryBuilderList, QueryObject queryObject) {
        if (null == queryObject) {
            return;
        }
        String flag = queryObject.getFlag();
        String key = queryObject.getKey();
        Object value = queryObject.getValue();
        MultiQueryObject multiQueryObject = queryObject.getMultiQueryObject();
        if (!StringUtils.isEmpty(flag) && !StringUtils.isEmpty(key) && null != value && null != multiQueryObject) {
            System.out.println("格式不正确.....");
            return;
        }
        if (!StringUtils.isEmpty(flag) && !StringUtils.isEmpty(key) && null != value && null == multiQueryObject) {
            queryBuilderList.add(builder(flag, key, value));
        }
        if (StringUtils.isEmpty(flag) && StringUtils.isEmpty(key) && null == value && null != multiQueryObject) {
            List<QueryObject> list = multiQueryObject.getQueryObjectList();
            createMultiQueryObject(queryBuilderList,list, multiQueryObject);
        }
    }

    public static void createMultiQueryObject(List<QueryBuilder> queryBuilderList, List<QueryObject> list, MultiQueryObject multiQueryObject) {
        int len = list.size();
        if (0 == len) {
            return;
        }
        String f = multiQueryObject.getFlag();
        if ("and".equals(f)) {
            for (int i = 0; i < len; i++) {
                QueryObject queryObject = list.get(i);
                objectToQueryBuilder(queryBuilderList, queryObject);
            }
        } else if ("or".equals(f)) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            List<QueryBuilder> shouldList = boolQueryBuilder.should();
            for (int i = 0; i < len; i++) {
                QueryObject queryObject = list.get(i);
                objectToQueryBuilder(shouldList, queryObject);
            }
            if (2 <= len) {
                boolQueryBuilder.minimumShouldMatch(1);
            }
            queryBuilderList.add(boolQueryBuilder);
        }
    }

    /**
     * 将构建的bool查询再包装到filter中，适用于不评分的Filter查询
     * @param baseObject
     * @return
     */
    public static QueryBuilder queryObjectToFilterQueryBuilder(QueryObject baseObject) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> queryBuilderList = boolQueryBuilder.filter();
        objectToQueryBuilder(queryBuilderList, baseObject);
        return boolQueryBuilder;
    }

    /**
     * 构建基础QueryBuilder语句
     * @param flag
     * @param key
     * @param value
     * @return
     */
    public static QueryBuilder builder(String flag, String key, Object value) {
        QueryBuilder queryBuilder = null;
        if (null == value) {
            return queryBuilder;
        }
        switch (flag) {
            case "eq" :
                queryBuilder = QueryBuilders.termQuery(key, value);
                break;
            case "ne" :
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(key, value));
                break;
            case "lt" :
                queryBuilder = QueryBuilders.rangeQuery(key).lt(value);
                break;
            case "lte" :
                queryBuilder = QueryBuilders.rangeQuery(key).lte(value);
                break;
            case "gt" :
                queryBuilder = QueryBuilders.rangeQuery(key).gt(value);
                break;
            case "gte" :
                queryBuilder = QueryBuilders.rangeQuery(key).gte(value);
                break;
            case "like" :
                queryBuilder = QueryBuilders.wildcardQuery(key, "*" + value + "*");
                break;
            case "in" :
                if (value instanceof List) {
                    List list = (List) value;
                    queryBuilder = QueryBuilders.termsQuery(key, list);
                } else if (value instanceof Object[]) {
                    Object[] obj = (Object[]) value;
                    queryBuilder = QueryBuilders.termsQuery(key, Arrays.asList(obj));
                }
                break;
            case "nin" :
                if (value instanceof List) {
                    List list = (List) value;
                    queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(key, list));
                } else if (value instanceof Object[]) {
                    Object[] obj = (Object[]) value;
                    queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(key, Arrays.asList(obj)));
                }
                break;
            default :
                break;
        }
        return queryBuilder;
    }
}
