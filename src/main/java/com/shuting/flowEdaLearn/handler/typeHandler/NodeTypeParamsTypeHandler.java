package com.shuting.flowEdaLearn.handler.typeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuting.flowEdaLearn.entity.NodeTypeParams;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NodeTypeParamsTypeHandler extends BaseTypeHandler<List<NodeTypeParams>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(
            PreparedStatement ps, int i, List<NodeTypeParams> parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setString(i, null);
        }
        if (parameter != null) {
            try {
                ps.setString(i, objectMapper.writeValueAsString(parameter));
            } catch (JsonProcessingException e) {
                throw new SQLException("Error converting parameter list to JSON string", e);
            }
        }
    }

    @Override
    public List<NodeTypeParams> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public List<NodeTypeParams> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public List<NodeTypeParams> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private List<NodeTypeParams> parseJson(String json) throws SQLException {
        try {
            if (json != null && !json.isEmpty()) {
                // Deserialize the JSON string to a list of Param objects
                return objectMapper.readValue(json, new TypeReference<>() {});
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new SQLException("Error parsing JSON string to parameter list", e);
        }
    }
}

