package com.test.printer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class Printer {
    @Autowired
    ApplicationContext applicationContext;

    @Scheduled(fixedRate = 3000)
    public void printBeans() {
        String beans = Arrays.stream(applicationContext.getBeanDefinitionNames()).filter(beanName -> applicationContext.getBean(beanName)
                .getClass().getPackage().getName().startsWith("com.test.processor")).collect(Collectors.joining("\n"));
        System.out.println(beans);
        System.out.println("-----------------");
    }
}
