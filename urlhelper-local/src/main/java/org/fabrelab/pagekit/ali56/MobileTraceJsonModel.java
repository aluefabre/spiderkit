/**
 * Project: alp.web
 * 
 * File Created at 2011-06-10
 * $Id: MobileTraceJsonModel.java 438870 2011-07-19 01:47:40Z pansg.wangj $
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package org.fabrelab.pagekit.ali56;

import java.util.List;

/**
 * 手机跟踪信息查询Json Model
 * 
 * @author jiansheng.xujs
 */
public class MobileTraceJsonModel {

    private String                status;

    private List<MobileTraceStep> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MobileTraceStep> getData() {
        return data;
    }

    public void setData(List<MobileTraceStep> data) {
        this.data = data;
    }

}
