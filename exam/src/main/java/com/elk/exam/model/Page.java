package com.elk.exam.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 前端页面表
 * </p>
 *
 * @author elk
 * @since 2021-04-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("page")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 前端页面表主键id
     */
        @TableId(value = "page_id", type = IdType.AUTO)
      private Integer pageId;

      /**
     * 页面的名称,要唯一
     */
      private String pageName;

      /**
     * 页面的功能性描述
     */
      private String pageDescription;

      /**
     * 页面对应的操作权限列表，用-连接action的id
     */
      private String actionIds;


}
