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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import components.LibraryClass;
import daos.UsuariosDAO;

/**
 *
 * @author floren
 */
@ManagedBean
@RequestScoped
public class LoginBean {
   
    private String username;
    private String userpass;
    private String loginmessage;
    
    private long iduser;
    private UsuariosDAO udao;
    
    public LoginBean() {
    
    }

    
    public String login() {
        
        if (this.username==null || this.username.length()< LibraryClass.MIN_LENGTH_USERNAME
                || this.username.length()> LibraryClass.MAX_LENGTH_USERNAME ||
            this.userpass==null || this.userpass.length()< LibraryClass.MIN_LENGTH_USERPASS
                || this.userpass.length()> LibraryClass.MAX_LENGTH_USERPASS ) {
            
            udao=new UsuariosDAO();
            
            iduser=udao.identifyUser(username, userpass);
            
            if (this.iduser==0) this.loginmessage="Usuario-contraseña inexistente";   
            if (this.iduser==-1) this.loginmessage="Error procesando su solicitud";  
            if (this.iduser>0) {
                this.loginmessage="Autenticación correcta "+this.iduser;
                return "main";
            }  
  
        } else this.loginmessage="Usuario-contraseña no válidos. Pruebe de nuevo";  
        
        return "index";
    }
    
    public String clear() {
        this.username="";
        this.userpass="";
        this.loginmessage="";
        return "index";
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the userpass
     */
    public String getUserpass() {
        return userpass;
    }

    /**
     * @param userpass the userpass to set
     */
    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    /**
     * @return the loginmessage
     */
    public String getLoginmessage() {
        return loginmessage;
    }

    /**
     * @param loginmessage the loginmessage to set
     */
    public void setLoginmessage(String loginmessage) {
        this.loginmessage = loginmessage;
    }
    
    
}
