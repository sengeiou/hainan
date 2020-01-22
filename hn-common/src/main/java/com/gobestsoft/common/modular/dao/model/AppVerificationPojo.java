package com.gobestsoft.common.modular.dao.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("app_verification")
public class AppVerificationPojo extends Model<AppVerificationPojo> {

    private static final long serialVersionUID = 1L;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
    
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app_verification.id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app_verification.mobile
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app_verification.verification
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String verification;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app_verification.create_time
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app_verification.id
     *
     * @return the value of app_verification.id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app_verification.id
     *
     * @param id the value for app_verification.id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app_verification.mobile
     *
     * @return the value of app_verification.mobile
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app_verification.mobile
     *
     * @param mobile the value for app_verification.mobile
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app_verification.verification
     *
     * @return the value of app_verification.verification
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getVerification() {
        return verification;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app_verification.verification
     *
     * @param verification the value for app_verification.verification
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setVerification(String verification) {
        this.verification = verification;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app_verification.create_time
     *
     * @return the value of app_verification.create_time
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app_verification.create_time
     *
     * @param createTime the value for app_verification.create_time
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}