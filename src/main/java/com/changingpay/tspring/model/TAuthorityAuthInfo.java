package com.changingpay.tspring.model;

public class TAuthorityAuthInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_authority_auth_info.auth_id
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    private String authId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_authority_auth_info.auth_desc
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    private String authDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_authority_auth_info.status
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    private String status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_authority_auth_info.auth_id
     *
     * @return the value of t_authority_auth_info.auth_id
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_authority_auth_info.auth_id
     *
     * @param authId the value for t_authority_auth_info.auth_id
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_authority_auth_info.auth_desc
     *
     * @return the value of t_authority_auth_info.auth_desc
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public String getAuthDesc() {
        return authDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_authority_auth_info.auth_desc
     *
     * @param authDesc the value for t_authority_auth_info.auth_desc
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc == null ? null : authDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_authority_auth_info.status
     *
     * @return the value of t_authority_auth_info.status
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_authority_auth_info.status
     *
     * @param status the value for t_authority_auth_info.status
     *
     * @mbggenerated Tue Oct 18 17:05:20 CST 2016
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}