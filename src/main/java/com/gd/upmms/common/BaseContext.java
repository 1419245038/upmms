package com.gd.upmms.common;

public class BaseContext {

    private static ThreadLocal<Integer> threadLocal=new InheritableThreadLocal<>();

    public static void setCurrentId(Integer id){
        threadLocal.set(id);
    }

    public static Integer getCurrentId(){
        return threadLocal.get();
    }
}
