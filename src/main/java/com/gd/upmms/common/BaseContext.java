package com.gd.upmms.common;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 14:07:20
 */
public class BaseContext {

    private static ThreadLocal<Integer> threadLocal=new InheritableThreadLocal<>();

    public static void setCurrentId(Integer id){
        threadLocal.set(id);
    }

    public static Integer getCurrentId(){
        return threadLocal.get();
    }
}
