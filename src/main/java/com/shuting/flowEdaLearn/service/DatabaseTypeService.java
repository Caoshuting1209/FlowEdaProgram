package com.shuting.flowEdaLearn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuting.flowEdaLearn.entity.NodeType;
import com.shuting.flowEdaLearn.entity.NodeTypeParams;
import com.shuting.flowEdaLearn.mapper.NodeTypeMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DatabaseTypeService {
    private SqlSessionFactory sqlSessionFactory;
    private ObjectMapper objectMapper;

    public DatabaseTypeService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.objectMapper = new ObjectMapper(); // 初始化 ObjectMapper
    }

    public void saveNodeDataToDatabase(NodeType nodeType) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String paramsJson = objectMapper.writeValueAsString(nodeType.getParams());
            NodeTypeMapper mapper = session.getMapper(NodeTypeMapper.class);
            mapper.insertParams(nodeType.getId(), paramsJson);
            session.commit();
        } catch (JsonProcessingException e) {
            session.rollback();
            throw new RuntimeException("Error converting NodeDataParams to JSON", e);
        } finally {
            session.close();
        }
    }

    public void getNodeTypeFromDatabase(NodeType nodeType) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            NodeTypeMapper mapper = session.getMapper(NodeTypeMapper.class);
            String paramsJson = mapper.findById(nodeType.getId());
            if (paramsJson != null) {
                List<NodeTypeParams> list = objectMapper.readValue(paramsJson, new TypeReference<>(){});
                nodeType.setParams(list);
            }
            session.commit();
        } catch(Exception e){
            throw new RuntimeException("Error parsing JSON from database", e);
        }finally{
            session.close();
        }
    }
}


