package com.shuting.flowEdaLearn.entity.user;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName oauth2_third_account
 */
@TableName(value = "oauth2_third_account")
@Data
public class Oauth2ThirdAccount implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    private Integer uniqueId;

    /** */
    private String login;

    /** */
    private String name;

    /** */
    private String avatarUrl;

    /** */
    private String credentials;

    /** */
    private Date credentialsExpiresAt;

    /** */
    private String registrationId;

    /** */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
