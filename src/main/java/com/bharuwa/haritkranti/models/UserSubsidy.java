package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
@Document
public class UserSubsidy extends BaseObject {

    @Indexed
    private String userId;
    @DBRef
    private Subsidy subsidy;
    private String item;
    private int quantity;
    private BigDecimal amount = BigDecimal.ZERO;

    // for location Search(filter)
    private Address address;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Subsidy getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(Subsidy subsidy) {
        this.subsidy = subsidy;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
