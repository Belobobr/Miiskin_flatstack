package com.miiskin.miiskin.Data;

import java.io.Serializable;

/**
 * Created by Newshka on 30.06.2015.
 */
public class UserInfo implements Serializable {
    public String birth_date;
    public String email;
    public String gender;
    public Long userId;

    public static String MALE = "MALE";
    public static String FEMALE = "FEMALE";
}
