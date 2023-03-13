package com.invitation.module.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.invitation.module.common.model.configuration.Configuration;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CommonsUtil {

    private final Authority authority;

    public static String toJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return objMapper.writeValueAsString(obj);
    }


    public static boolean isEmpty(Object obj) {

        if (obj instanceof String) {
            return obj == null || "".equals(obj.toString().trim());
        } else if (obj instanceof List) {
            return obj == null || ((List<?>)obj).isEmpty();
        } else if (obj instanceof Map) {
            return obj == null || ((Map<?, ?>) obj).isEmpty();
        } else if (obj instanceof Object[]) {
            return obj == null || Array.getLength(obj) == 0;
        } else {
            return obj == null;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String createConfigurationKey(Configuration configuration) {

        if (CommonsUtil.isEmpty(configuration) || CommonsUtil.isEmpty(configuration.getDatabaseName()) || CommonsUtil.isEmpty(configuration.getTableName())) {
            throw new NullPointerException("키를 만들기 위한 데이터가 부족합니다.");
        }

        String databaseName = configuration.getDatabaseName();
        String tableName = configuration.getTableName();

        return databaseName.concat("-").concat(tableName);
    }
}
