package com.mininglamp.currencySys.common.listener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.mininglamp.currencySys.user.bean.MemoryData;

public class SessionCounter implements  HttpSessionListener  {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        System.out.println("有session诞生了，ID是："+se.getSession().getId()+"---"+se.getSession().getMaxInactiveInterval());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
//        System.out.println("有session失效了，ID是："+se.getSession().getId()+"--"+se.getSession().getMaxInactiveInterval());
        MemoryData.getSessionIDMap().remove(se.getSession().getAttribute("username"));
    }

}