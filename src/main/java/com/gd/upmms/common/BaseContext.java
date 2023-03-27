package com.gd.upmms.common;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 14:07:20
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new InheritableThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
