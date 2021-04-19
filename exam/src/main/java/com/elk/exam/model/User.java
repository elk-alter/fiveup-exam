package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 用户id,主键，字符串型
     */
        private String userId;

      /**
     * 用户名
     */
      private String userUsername;

      /**
     * 用户昵称
     */
      private String userNickname;

      /**
     * 用户秘密
     */
      private String userPassword;

      /**
     * 当前用户的角色的id
     */
      private Integer userRoleId;

      /**
     * 用户的头像地址
     */
      private String userAvatar;

      /**
     * 用户的自我描述
     */
      private String userDescription;

      /**
     * 用户邮箱
     */
      private String userEmail;

      /**
     * 用户手机号
     */
      private String userPhone;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 更新时间
     */
      private Date updateTime;


}
