package com.gobestsoft.mamage.dialect;

import com.gobestsoft.mamage.dialect.form.*;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;

import java.util.Set;

/**
 * create by gutongwei
 * on 2018/8/3 下午11:55
 */
public class FormDialect extends SpringStandardDialect {

    public final String prefix = "form";

    @Override
    public String getPrefix() {
        return prefix;
    }


    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = super.getProcessors();
        processors.add(new TextProcessor());
        processors.add(new NumberProcessor());
        processors.add(new DateTimeProcessor());
        processors.add(new CheckProcessor());
        processors.add(new SelectProcessor());
        processors.add(new AreaProcessor());
        processors.add(new InteractProcessor());
        processors.add(new ImgChgProcessor());
        processors.add(new ImgProcessor());
        processors.add(new FileProcessor());
        processors.add(new PasswordProcessor());
        return processors;
    }
}