package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shuting.flowEdaLearn.validation.DeleteGroup;
import com.shuting.flowEdaLearn.validation.PostGroup;
import com.shuting.flowEdaLearn.validation.UpdateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Date;

@Data
@TableName ("flow_eda")
public class Flow {
    @NotNull(message = "id can't be null", groups = {UpdateGroup.class, DeleteGroup.class, PostGroup.class})
    private String id;

    @NotNull(message = "name can't be null", groups = {UpdateGroup.class, PostGroup.class})
    private String name;

    @NotNull(message = "description can't be null", groups = {UpdateGroup.class, PostGroup.class})
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