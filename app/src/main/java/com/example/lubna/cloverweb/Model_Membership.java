package com.example.lubna.cloverweb;

public class Model_Membership {

    private String issue_date;
    private String expiry_date;
    private String account_title;
    private String branch_name;
    private String membership_type;
    private String account_number;
    private String account_balance;
    private String account_id;

    public String getIssue_date()
    {
        return issue_date;
    }
    public String getExpiry_date()
    {
        return expiry_date;
    }
    public String getAccount_title()
    {
        return account_title;
    }
    public String getBranch_name()
    {
        return branch_name;
    }
    public String getMembership_type()
    {
        return membership_type;
    }
    public String getAccount_number()
    {
        return account_number;
    }
    public String getAccount_balance()
    {
        return account_balance;
    }

    public String getAccount_id() {
        return account_id;
    }

    public Model_Membership(String issue_date, String expiry_date, String account_title, String branch_name, String account_number, String account_balance, String account_id) {
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.account_title = account_title;
        this.branch_name = branch_name;
        this.account_number = account_number;
        this.account_balance = account_balance;
        this.account_id = account_id;
    }
}
