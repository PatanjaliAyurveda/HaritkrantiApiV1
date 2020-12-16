package com.bharuwa.haritkranti.utils;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.OTP;
import com.bharuwa.haritkranti.models.Role;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.bharuwa.haritkranti.utils.Constants.*;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author anuragdhunna
 */
public class HelperMethods {

    private static final BigDecimal hundred = BigDecimal.valueOf(100);
    public static String generateMD5value(String text) {

        text = text + generateRandomString();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        messageDigest.reset();
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "{MD5}" + hashtext;
        }
        return "{MD5}" + hashtext;
    }

    public static String generateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static String removeEndComma(String text) {
        if (!isEmpty(text) && text.charAt(text.length() - 1) == ',') {
            text = StringUtils.substring(text, 0, -1);
        }
        return !isEmpty(text) ? text.trim() : text;
    }

    //helper method to split filter ids and return list of those filter objects
    //return null if error occurs otherwise list of filters(can be empty)
    public static List<String> getListStringIds(String text) {
        List<String> filters = new ArrayList<>();
        if (isEmpty(text)) return filters;
        text = text.trim();
        try {
            if (text.contains(",")) {
                String[] filtList = text.split(",");
                for (String s : filtList) {
                    if (isEmpty(s)) continue;
                    filters.add(s);
                }
            } else {
                filters.add(text);
            }
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
        return filters;
    }

    public static List<Long> getFilterIDs(String text) {
        List<Long> filters = new ArrayList<>();
        if (isEmpty(text)) return filters;
        text = text.trim();
        try {
            if (text.contains(",")) {
                String[] filtList = text.split(",");
                for (String s : filtList) {
                    if (isEmpty(s)) continue;
                    Long filterID = Long.parseLong(s);
                    filters.add(filterID);
                }
            } else {
                Long filterID = Long.parseLong(text);
                filters.add(filterID);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return filters;
    }

    public static OTP generateOtp() {
        Random random = new Random();
        String otp = String.format("%04d", random.nextInt(10000));
        OTP mOTP = new OTP();

        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingTenMins = new Date(t + (5 * ONE_MINUTE_IN_MILLIS));

        mOTP.setOtp(otp);
        mOTP.setExpiryTime(afterAddingTenMins);
        return mOTP;
    }

    /**
     * Generate Unique Product Id for Market Place App
     * @return Unique Random String of 6 Digit AlphaNumeric
     */
    public static String generateProductId() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     * Generate Unique code for Agnets
     * @return Unique Random String of 6 Digit AlphaNumeric
     */
    public static String generateUniqueCodeForUser() {
        int alphaCount = 1;
        int numericCount = 5;
        String Alpha_String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Numeric_String = "0123456789";
        StringBuilder builder = new StringBuilder();
        while (alphaCount-- != 0) {
            int character = (int) (Math.random() * Alpha_String.length());
            builder.append(Alpha_String.charAt(character));
        }
        while (numericCount-- != 0) {
            int character = (int) (Math.random() * Numeric_String.length());
            builder.append(Numeric_String.charAt(character));
        }
        return builder.toString();
    }

    /**
     * Gets the cell value as string.
     *
     * @param cell the cell
     * @return the cell value as string
     */
    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.toString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        } else {
            return null;
        }
    }

    /**
     * add days to date in java
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     * subtract days to date in java
     * @param date
     * @param days
     * @return
     */
    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

//    public static EmployeeAssignmentHistory.EmplyeeRelationship getEmployeeRelationship(User touser,
//                                                                                        User fromUser,
//                                                                                        Role nationalManager,
//                                                                                        Role stateManager,
//                                                                                        Role districtManager,
//                                                                                        Role blockManager) {
//
//        EmployeeAssignmentHistory.EmplyeeRelationship emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ADMIN;
//
//        if(fromUser.getRoles().size() == 1){
//            String fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
//            switch (fromUserRoleName) {
//                case ROLE_ADMIN:
//                    if(touser.getRoles().size() == 1){
//                        String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                        switch (toUserRoleName) {
//                            case ROLE_ADMIN:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ADMIN;
//                                break;
//                            case ROLE_NATIONAL_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_NM;
//                                break;
//                            case ROLE_STATE_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_SM;
//                                break;
//                            case ROLE_DISTRICT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_DM;
//                                break;
//                            case ROLE_AGENT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_BM;
//                                break;
//                            case ROLE_AGENT:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_AGENT;
//                                break;
//                            case ROLE_USER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_FARMER;
//                                break;
//                        }
//                    } else {
//                        if(touser.getRoles().contains(nationalManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_NM;
//                        } else if(touser.getRoles().contains(stateManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_SM;
//                        } else if(touser.getRoles().contains(districtManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_DM;
//                        } else if(touser.getRoles().contains(blockManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_BM;
//                        }
//                    }
//                    break;
//                case ROLE_NATIONAL_MANAGER:
//                    if(touser.getRoles().size() == 1){
//                        String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                        switch (toUserRoleName) {
//                            case ROLE_STATE_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
//                                break;
//                            case ROLE_DISTRICT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
//                                break;
//                            case ROLE_AGENT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
//                                break;
//                            case ROLE_AGENT:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_AGENT;
//                                break;
//                            case ROLE_USER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_FARMER;
//                                break;
//                        }
//                    } else {
//                        if(touser.getRoles().contains(stateManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
//                        } else if(touser.getRoles().contains(districtManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
//                        } else if(touser.getRoles().contains(blockManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
//                        }
//                    }
//                    break;
//                case ROLE_STATE_MANAGER:
//                    if(touser.getRoles().size() == 1){
//                        String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                        switch (toUserRoleName) {
//                            case ROLE_DISTRICT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
//                                break;
//                            case ROLE_AGENT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
//                                break;
//                            case ROLE_AGENT:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_AGENT;
//                                break;
//                            case ROLE_USER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_FARMER;
//                                break;
//                        }
//                    } else {
//                        if(touser.getRoles().contains(districtManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
//                        } else if(touser.getRoles().contains(blockManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
//                        }
//                    }
//                    break;
//                case ROLE_DISTRICT_MANAGER:
//                    if(touser.getRoles().size() == 1){
//                        String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                        switch (toUserRoleName) {
//                            case ROLE_AGENT_MANAGER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
//                                break;
//                            case ROLE_AGENT:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_AGENT;
//                                break;
//                            case ROLE_USER:
//                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_FARMER;
//                                break;
//                        }
//                    } else {
//                        if(touser.getRoles().contains(blockManager)){
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
//                        }
//                    }
//                    break;
//                case ROLE_AGENT_MANAGER:
//                    String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                    switch (toUserRoleName) {
//                        case ROLE_AGENT:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_AGENT;
//                            break;
//                        case ROLE_USER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_FARMER;
//                            break;
//                    }
//                    break;
//                case ROLE_AGENT:
//                    emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_FARMER;
//                    break;
//                default:
//                    throw new CustomException("Role Type is not correct.");
//            }
//
//        } else {
//            if(fromUser.getRoles().contains(nationalManager)){
//                if(touser.getRoles().size() == 1){
//                    String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                    switch (toUserRoleName) {
//                        case ROLE_STATE_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
//                            break;
//                        case ROLE_DISTRICT_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
//                            break;
//                        case ROLE_AGENT_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
//                            break;
//                        case ROLE_AGENT:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_AGENT;
//                            break;
//                        case ROLE_USER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_FARMER;
//                            break;
//                    }
//                } else {
//                    if(touser.getRoles().contains(stateManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
//                    } else if(touser.getRoles().contains(districtManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
//                    } else if(touser.getRoles().contains(blockManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
//                    }
//                }
//            } else if(fromUser.getRoles().contains(stateManager)){
//                if(touser.getRoles().size() == 1){
//                    String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                    switch (toUserRoleName) {
//                        case ROLE_DISTRICT_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
//                            break;
//                        case ROLE_AGENT_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
//                            break;
//                        case ROLE_AGENT:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_AGENT;
//                            break;
//                        case ROLE_USER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_FARMER;
//                            break;
//                    }
//                } else {
//                    if(touser.getRoles().contains(districtManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
//                    } else if(touser.getRoles().contains(blockManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
//                    }
//                }
//            } else if(fromUser.getRoles().contains(districtManager)){
//                if(touser.getRoles().size() == 1){
//                    String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                    switch (toUserRoleName) {
//                        case ROLE_AGENT_MANAGER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
//                            break;
//                        case ROLE_AGENT:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_AGENT;
//                            break;
//                        case ROLE_USER:
//                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_FARMER;
//                            break;
//                    }
//                } else {
//                    if(touser.getRoles().contains(blockManager)){
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
//                    }
//                }
//            } else if(fromUser.getRoles().contains(blockManager)){
//                String  toUserRoleName = touser.getRoles().get(0).getRoleName();
//                switch (toUserRoleName) {
//                    case ROLE_AGENT:
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_AGENT;
//                        break;
//                    case ROLE_USER:
//                        emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_FARMER;
//                        break;
//                }
//            }
//        }
//        return emoloyeeRelationship;
//    }

    public static EmployeeAssignmentHistory.EmplyeeRelationship getEmployeeRelationshipByToUserType(String userRoleName,
                                                                                                    User fromUser,
                                                                                                    Role nationalManager,
                                                                                                    Role stateManager,
                                                                                                    Role districtManager,
                                                                                                    Role blockManager) {

        EmployeeAssignmentHistory.EmplyeeRelationship emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ADMIN;

            switch (userRoleName) {
                case ROLE_ADMIN:
                    emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ADMIN;
                    break;
                case ROLE_ADMIN_VIEW:
                    emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ADMINVIEW;
                    break;
                case ROLE_ACCOUNTANT:
                    emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_ACCOUNTANT;
                    break;
                case ROLE_NATIONAL_MANAGER:
                    emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_NM;
                    break;
                case ROLE_STATE_MANAGER:
                    if(fromUser.getRoles().size() == 1){
                        String  fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
                        switch (fromUserRoleName) {
                            case ROLE_ADMIN:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_SM;
                                break;
                            case ROLE_NATIONAL_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
                                break;
                        }
                    } else {
                        if(fromUser.getRoles().contains(nationalManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_SM;
                        }
                    }
                    break;
                case ROLE_DISTRICT_MANAGER:
                    if(fromUser.getRoles().size() == 1){
                        String  fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
                        switch (fromUserRoleName) {
                            case ROLE_ADMIN:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_DM;
                                break;
                            case ROLE_NATIONAL_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
                                break;
                            case ROLE_STATE_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
                                break;
                        }
                    } else {
                        if(fromUser.getRoles().contains(nationalManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_DM;
                        } else if (fromUser.getRoles().contains(stateManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_DM;
                        }
                    }
                    break;
                case ROLE_AGENT_MANAGER:
                    if(fromUser.getRoles().size() == 1){
                        String  fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
                        switch (fromUserRoleName) {
                            case ROLE_ADMIN:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_BM;
                                break;
                            case ROLE_NATIONAL_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
                                break;
                            case ROLE_STATE_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
                                break;
                            case ROLE_DISTRICT_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
                                break;
                        }
                    } else {
                        if(fromUser.getRoles().contains(nationalManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_BM;
                        } else if (fromUser.getRoles().contains(stateManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_BM;
                        } else if (fromUser.getRoles().contains(districtManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_BM;
                        }
                    }
                    break;
                case ROLE_AGENT:
                    if(fromUser.getRoles().size() == 1){
                        String  fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
                        switch (fromUserRoleName) {
                            case ROLE_ADMIN:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_AGENT;
                                break;
                            case ROLE_NATIONAL_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_AGENT;
                                break;
                            case ROLE_STATE_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_AGENT;
                                break;
                            case ROLE_DISTRICT_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_AGENT;
                                break;
                            case ROLE_AGENT_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_AGENT;
                                break;
                        }
                    } else {
                        if(fromUser.getRoles().contains(nationalManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_AGENT;
                        } else if (fromUser.getRoles().contains(stateManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_AGENT;
                        } else if (fromUser.getRoles().contains(districtManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_AGENT;
                        } else if (fromUser.getRoles().contains(blockManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_AGENT;
                        }
                    }
                    break;
                case ROLE_USER:
                    if(fromUser.getRoles().size() == 1){
                        String  fromUserRoleName = fromUser.getRoles().get(0).getRoleName();
                        switch (fromUserRoleName) {
                            case ROLE_ADMIN:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.ADMIN_FARMER;
                                break;
                            case ROLE_NATIONAL_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_FARMER;
                                break;
                            case ROLE_STATE_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_FARMER;
                                break;
                            case ROLE_DISTRICT_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_FARMER;
                                break;
                            case ROLE_AGENT_MANAGER:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_FARMER;
                                break;
                            case ROLE_AGENT:
                                emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.AGENT_FARMER;
                                break;
                        }
                    } else {
                        if(fromUser.getRoles().contains(nationalManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.NM_FARMER;
                        } else if (fromUser.getRoles().contains(stateManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.SM_FARMER;
                        } else if (fromUser.getRoles().contains(districtManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.DM_FARMER;
                        } else if (fromUser.getRoles().contains(blockManager)){
                            emoloyeeRelationship = EmployeeAssignmentHistory.EmplyeeRelationship.BM_FARMER;
                        }
                    }
                    break;
            }
        return emoloyeeRelationship;
    }

}