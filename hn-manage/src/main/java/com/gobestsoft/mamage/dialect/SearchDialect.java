package com.gobestsoft.mamage.dialect;

import com.gobestsoft.mamage.dialect.search.SearchDateTimeProcessor;
import com.gobestsoft.mamage.dialect.search.SearchInputProcessor;
import com.gobestsoft.mamage.dialect.search.SearchSelectProcessor;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;

import java.util.HashSet;
import java.util.Set;

/**
 * create by gutongwei
 * on 2018/8/3 下午9:15
 */
public class SearchDialect extends SpringStandardDialect {

    public final String prefix = "search";

    @Override
    public String getPrefix() {
        return prefix;
    }


    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new SearchInputProcessor());
        processors.add(new SearchSelectProcessor());
        processors.add(new SearchDateTimeProcessor());
        return processors;
    }
}