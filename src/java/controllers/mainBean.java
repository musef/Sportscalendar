/*
 * Copyright (C) fmsdevelopment.com author musef2904@gmail.com
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package controllers;

import static controllers.LoginBean.user;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@RequestScoped
public class mainBean {

    private long thisiduser;
    private String thisusername;
    
    
    
    public mainBean() {
        this.thisiduser = LoginBean.user.getId();
        this.thisusername = LoginBean.user.getNameuser();
        System.out.println("User:"+thisiduser+" - Object2:"+user);
    
    }
    


    /**
     * @return the thisiduser
     */
    public long getThisiduser() {
        return thisiduser;
    }

    /**
     * @param thisiduser the thisiduser to set
     */
    public void setThisiduser(long thisiduser) {
        this.thisiduser = thisiduser;
    }

    /**
     * @return the thisusername
     */
    public String getThisusername() {
        return thisusername;
    }

    /**
     * @param thisusername the thisusername to set
     */
    public void setThisusername(String thisusername) {
        this.thisusername = thisusername;
    }
 
    
    
}
