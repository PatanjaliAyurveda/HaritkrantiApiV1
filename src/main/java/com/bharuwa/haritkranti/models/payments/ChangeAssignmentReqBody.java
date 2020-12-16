package com.bharuwa.haritkranti.models.payments;

/**
 * @author harman
 */
public class ChangeAssignmentReqBody {

    private String fromUserId;

    private String toUserId;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
