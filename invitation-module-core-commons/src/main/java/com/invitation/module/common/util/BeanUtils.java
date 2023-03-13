package com.invitation.module.common.util;

import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BeanUtils {

    public static <T> T getBean(Class<T> classType){
        return (T) ApplicationContextProvider.getApplicationContext().getBean(classType);
    }
}
