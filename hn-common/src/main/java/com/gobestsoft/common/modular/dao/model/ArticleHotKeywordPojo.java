package com.gobestsoft.common.modular.dao.model;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

public class ArticleHotKeywordPojo extends Model<ArticleHotKeywordPojo> {

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_hot_keyword.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_hot_keyword.keyword
     *
     * @mbggenerated
     */
    private String keyword;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_hot_keyword.type
     *
     * @mbggenerated
     */
    private Short type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_hot_keyword.id
     *
     * @return the value of article_hot_keyword.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_hot_keyword.id
     *
     * @param id the value for article_hot_keyword.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_hot_keyword.keyword
     *
     * @return the value of article_hot_keyword.keyword
     *
     * @mbggenerated
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_hot_keyword.keyword
     *
     * @param keyword the value for article_hot_keyword.keyword
     *
     * @mbggenerated
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_hot_keyword.type
     *
     * @return the value of article_hot_keyword.type
     *
     * @mbggenerated
     */
    public Short getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_hot_keyword.type
     *
     * @param type the value for article_hot_keyword.type
     *
     * @mbggenerated
     */
    public void setType(Short type) {
        this.type = type;
    }
}