package com.gobestsoft.common.modular.dao.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("org_democracy")
public class OrgDemocracyPojo extends Model<OrgDemocracyPojo> {

    private static final long serialVersionUID = 1L;

	@Override
	protected Serializable pkVal() {
		return this.democracyId;
	}
    
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.democracy_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String democracyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.init_date
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String initDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.createtime
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private String createtime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.is_workers_congress
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private Short isWorkersCongress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.is_democracy_consultative
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private Short isDemocracyConsultative;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_democracy.dept_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    private Integer deptId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.democracy_id
     *
     * @return the value of org_democracy.democracy_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getDemocracyId() {
        return democracyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.democracy_id
     *
     * @param democracyId the value for org_democracy.democracy_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setDemocracyId(String democracyId) {
        this.democracyId = democracyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.init_date
     *
     * @return the value of org_democracy.init_date
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getInitDate() {
        return initDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.init_date
     *
     * @param initDate the value for org_democracy.init_date
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.createtime
     *
     * @return the value of org_democracy.createtime
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.createtime
     *
     * @param createtime the value for org_democracy.createtime
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.is_workers_congress
     *
     * @return the value of org_democracy.is_workers_congress
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public Short getIsWorkersCongress() {
        return isWorkersCongress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.is_workers_congress
     *
     * @param isWorkersCongress the value for org_democracy.is_workers_congress
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setIsWorkersCongress(Short isWorkersCongress) {
        this.isWorkersCongress = isWorkersCongress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.is_democracy_consultative
     *
     * @return the value of org_democracy.is_democracy_consultative
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public Short getIsDemocracyConsultative() {
        return isDemocracyConsultative;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.is_democracy_consultative
     *
     * @param isDemocracyConsultative the value for org_democracy.is_democracy_consultative
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setIsDemocracyConsultative(Short isDemocracyConsultative) {
        this.isDemocracyConsultative = isDemocracyConsultative;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_democracy.dept_id
     *
     * @return the value of org_democracy.dept_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public Integer getDeptId() {
        return deptId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_democracy.dept_id
     *
     * @param deptId the value for org_democracy.dept_id
     *
     * @mbg.generated Tue Dec 05 12:46:32 CST 2017
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}