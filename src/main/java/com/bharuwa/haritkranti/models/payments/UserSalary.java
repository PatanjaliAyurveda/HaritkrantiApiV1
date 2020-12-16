package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
@Document
public class UserSalary extends BaseObject {

    public enum PromotionType {
        Increment, Decrement
    }

    @DBRef
    private User user;
    private BigDecimal salary = BigDecimal.ZERO;
    private PromotionType promotionType = PromotionType.Increment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }
}
