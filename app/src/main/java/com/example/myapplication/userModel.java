package com.example.myapplication;

public class userModel {
    String uId;
    String addMoney;
    String sendNumber;

    public String getEamoney() {
        return Eamoney;
    }

    public void setEamoney(String eamoney) {
        Eamoney = eamoney;
    }

    String Eamoney;

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    String recharge;

    public String getRechargeOperator() {
        return rechargeOperator;
    }

    public void setRechargeOperator(String rechargeOperator) {
        this.rechargeOperator = rechargeOperator;
    }

    String rechargeOperator;

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    String rechargeNumber;
    String rechargeAmount;

    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    String receiveNumber;
    String sendMoney;
    String paymentMoney;
    String type;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    String timeStamp;


    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public userModel() {
        // empty const ructor
        // required for Firebase.
    }

    // Constructor for all variables.
//    public userModel(String addMoney,String uId) {
//        this.addMoney = addMoney;
//
//    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(String addMoney) {
        this.addMoney = addMoney;
    }

    public String getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(String sendMoney) {
        this.sendMoney = sendMoney;
    }
    public String getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(String paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public String getCheckBalance() {
        return checkBalance;
    }

    public void setCheckBalance(String checkBalance) {
        this.checkBalance = checkBalance;
    }

    String checkBalance;

}
