package com.shuting.flowEdaProgram.flow.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shuting.flowEdaProgram.commons.validation.DeleteGroup;
import com.shuting.flowEdaProgram.commons.validation.PostGroup;
import com.shuting.flowEdaProgram.commons.validation.UpdateGroup;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.util.Date;

@Data
@TableName("eda_flow")
public class Flow {
    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class, PostGroup.class})
    private String id;

    @NotNull(groups = {UpdateGroup.class, PostGroup.class})
    private String name;

    @NotNull(groups = {UpdateGroup.class, PostGroup.class})
    private String description;

    private Status status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public enum Status {
        INIT,
        RUNNING,
        FINISHED,
        FAILED,
    }
}
