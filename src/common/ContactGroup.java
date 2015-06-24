/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;

/**
 *
 * @author amster
 */
public class ContactGroup implements Serializable
{
    public String name = null;

    public ContactGroup(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
