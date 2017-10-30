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

import components.LibraryClass;
import daos.UsuariosDAO;
import javax.faces.bean.SessionScoped;
import models.Usuarios;
import components.LoginComponent;


/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@SessionScoped
public class LoginBean extends LoginComponent{
   
    // variable de identificacion
    private String username;
    private String userpass;
    private String loginmessage;
    
    // datos del usuario
    public static Usuarios user;

    private String worksHoursM;
    private String worksSessM;
    private String worksHoursY;
    private String worksSessY;
    
    
    private UsuariosDAO udao;
    
    
    public LoginBean() {
        
    }

    
    public String login() {
        
        if (this.username==null || this.username.length()< LibraryClass.MIN_LENGTH_USERNAME
                || this.username.length()> LibraryClass.MAX_LENGTH_USERNAME ||
            this.userpass==null || this.userpass.length()< LibraryClass.MIN_LENGTH_USERPASS
                || this.userpass.length()> LibraryClass.MAX_LENGTH_USERPASS ) {
            
            udao=new UsuariosDAO();
            
            user=udao.identifyUser(username, userpass);
            
            if (user.getId()==0) this.loginmessage="Usuario-contraseña inexistente";   
            if (user.getId()==-1) this.loginmessage="Error procesando su solicitud";  
            if (user.getId()>0) {
                this.loginmessage="Autenticación correcta "+user.getId();
                this.worksHoursM="00:00:00";
                this.worksSessM="0";
                this.setWorksHoursY("00:00:00");
                this.setWorksSessY("0");
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
    
    
    public String exit() {
        // borramos los datos del usuario
        user=null;
        this.username="";
        this.userpass="";
        // lanzamos mensaje
        this.loginmessage="Ha salido de la aplicación de forma correcta";        
        
        
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

   

    /**
     * @return the user
     */
    public Usuarios getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(Usuarios user) {
        this.user = user;
    }

    /**
     * @return the worksHoursM
     */
    public String getWorksHoursM() {
        return worksHoursM;
    }

    /**
     * @param worksHoursM the worksHoursM to set
     */
    public void setWorksHoursM(String worksHoursM) {
        this.worksHoursM = worksHoursM;
    }

    /**
     * @return the worksSessM
     */
    public String getWorksSessM() {
        return worksSessM;
    }

    /**
     * @param worksSessM the worksSessM to set
     */
    public void setWorksSessM(String worksSessM) {
        this.worksSessM = worksSessM;
    }

    /**
     * @return the worksHoursY
     */
    public String getWorksHoursY() {
        return worksHoursY;
    }

    /**
     * @param worksHoursY the worksHoursY to set
     */
    public void setWorksHoursY(String worksHoursY) {
        this.worksHoursY = worksHoursY;
    }

    /**
     * @return the worksSessY
     */
    public String getWorksSessY() {
        return worksSessY;
    }

    /**
     * @param worksSessY the worksSessY to set
     */
    public void setWorksSessY(String worksSessY) {
        this.worksSessY = worksSessY;
    }
    
    
}
