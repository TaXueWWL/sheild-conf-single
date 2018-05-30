package com.sheild.conf;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 观察者接口
 */
interface Observer {

    // 接收到通知发起更新操作
    void update(List<Object> context);
}
