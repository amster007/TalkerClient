/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amster
 */
public class Contact implements Serializable{

    public String firstName;
    public String lastName;
    public String displayedName;
    public String email;
    public Integer talkerNumber;
    public Long phoneNumber;
    public Date birthDate;

    @Override
    public String toString() {
        return displayedName;
    }
}
