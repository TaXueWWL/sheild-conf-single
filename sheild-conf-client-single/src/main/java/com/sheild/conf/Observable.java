package com.sheild.conf;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 被观察者接口
 */
interface Observable {

    // 增加一个观察者
    void addObserver(Observer observer);

    // 删除一个观察者
    void deleteObserver(Observer observer);

    // 通知观察者
    void notifyObservers(Observer observer, List<Object> context);
}
