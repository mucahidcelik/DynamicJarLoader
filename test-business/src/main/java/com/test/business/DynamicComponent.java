package com.test.business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

@Component
public class DynamicComponent {
    private String processor1 = "/home/mucahid/Desktop/processor1/target/processor1-1.0-SNAPSHOT.jar";
    private String processor2 = "/home/mucahid/Desktop/processor2/target/processor2-1.0-SNAPSHOT.jar";
    private boolean flag = true;
    private DefaultListableBeanFactory beanRegistry;

    @Autowired
    public DynamicComponent(ApplicationContext applicationContext) {
        ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
        this.beanRegistry = (DefaultListableBeanFactory) configContext.getBeanFactory();
    }

    @Scheduled(fixedRate = 10000)
    private void switchProcessor() throws Exception {
        destroyPreviousProcessor();
        flag = !flag;
        loadProcessor();
    }

    void destroyPreviousProcessor() {
        if (beanRegistry.isBeanNameInUse("com.test.processor.Processor" + (flag ? "1" : "2"))) {
            beanRegistry.removeBeanDefinition("com.test.processor.Processor" + (flag ? "1" : "2"));
        }
    }

    public void loadProcessor() throws Exception {
        File file = new File(flag ? processor1 : processor2);
        URLClassLoader child = new URLClassLoader(
                new URL[]{file.toURI().toURL()}
        );
        Class classToLoad = Class.forName("com.test.processor.Processor" + (flag ? "1" : "2"), true, child);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(classToLoad);
        beanRegistry.registerBeanDefinition(classToLoad.getCanonicalName(), beanDefinition);
    }


}