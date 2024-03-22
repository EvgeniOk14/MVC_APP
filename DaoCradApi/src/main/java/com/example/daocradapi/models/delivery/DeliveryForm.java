package com.example.daocradapi.models.delivery;


public class DeliveryForm {
    private String recipientName;
    private String address;
    private String contactNumber;

    public DeliveryForm(String recipientName, String address, String contactNumber) {
        this.recipientName = recipientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }
    public DeliveryForm()
    {

    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

