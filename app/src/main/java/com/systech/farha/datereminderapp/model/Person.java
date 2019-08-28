package com.systech.farha.datereminderapp.model;

public class Person {

    private Integer id;
    private String name;
    private String phoneNo;
    private String friendDate;
    private String loanDate;
    private String borrowDate;
    private String timeFriend;
    private String timeBorrower;
    private String timeLoner;
    private Double amountLoan;
    private Double amountBorrow;
    private String loan;
    private String friend;
    private String borrow;
    private Integer userId;
    private Boolean loanHasPaid;
    private Boolean borrowHasPaid;
    private byte[] profile;

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public String getTimeFriend() {
        return timeFriend;
    }

    public void setTimeFriend(String timeFriend) {
        this.timeFriend = timeFriend;
    }

    public String getTimeBorrower() {
        return timeBorrower;
    }

    public void setTimeBorrower(String timeBorrower) {
        this.timeBorrower = timeBorrower;
    }

    public String getTimeLoner() {
        return timeLoner;
    }

    public void setTimeLoner(String timeLoner) {
        this.timeLoner = timeLoner;
    }

    public String getFriendDate() {
        return friendDate;
    }

    public void setFriendDate(String friendDate) {
        this.friendDate = friendDate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }


    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Double getAmountLoan() {
        return amountLoan;
    }

    public void setAmountLoan(Double amountLoan) {
        this.amountLoan = amountLoan;
    }

    public Double getAmountBorrow() {
        return amountBorrow;
    }

    public void setAmountBorrow(Double amountBorrow) {
        this.amountBorrow = amountBorrow;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getLoanHasPaid() {
        return loanHasPaid;
    }

    public void setLoanHasPaid(Boolean loanHasPaid) {
        this.loanHasPaid = loanHasPaid;
    }

    public Boolean getBorrowHasPaid() {
        return borrowHasPaid;
    }

    public void setBorrowHasPaid(Boolean borrowHasPaid) {
        this.borrowHasPaid = borrowHasPaid;
    }
}
