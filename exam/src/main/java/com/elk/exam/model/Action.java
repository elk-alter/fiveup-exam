package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 前端操作比如增删改查等的权限表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_action")
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 前端页面操作表主键id
     */
        @TableId(value = "action_id", type = IdType.AUTO)
      private Integer actionId;

      /**
     * 前端操作的名字
     */
      private String actionName;

      /**
     * 页面操作的描述
     */
      private String actionDescription;

      /**
     * 当前操作是否需要校验,true为1,0为false
     */
      private Boolean defaultCheck;


}
