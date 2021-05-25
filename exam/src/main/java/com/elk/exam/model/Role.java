package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ex_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 角色表主键id
     */
        @TableId(value = "role_id", type = IdType.AUTO)
      private Integer roleId;

      /**
     * 角色名称
     */
      private String roleName;

      /**
     * 角色的描述
     */
      private String roleDescription;

      /**
     * 角色的详细功能阐述
     */
      private String roleDetail;

      /**
     * 当前角色所能访问的页面的id集合
     */
      private String rolePageIds;


}
