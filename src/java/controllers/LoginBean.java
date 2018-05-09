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

import javax.faces.bean.SessionScoped;
import models.Usuarios;
import components.LoginComponent;
import components.SportsListBean;
import components.LibraryClass;
import daos.AgendaDAO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 * REGLAS DE NEGOCIO
 * EL objeto LoginBean será un objeto de sesión, ya que mantendrá los datos de sesión
 * del usuario, en especial el objeto user, la lista de deportes userSportlist, y
 * eventIdActivity como evento en selección, el cual es inyectado en el formulario de
 * modificación de eventos del calendario.
 * 
 */

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@SessionScoped
public class LoginBean extends LibraryClass implements Serializable {
    
    // manager
    private LoginComponent loginComponent;
   
    // variable de identificacion
    private String username;
    private String userpass;
    private String loginmessage;
    
    // datos del usuario
    public static Usuarios user;
    protected static SportsListBean userSportlist;
    // dato idActivitySeleccionada
    public static String eventIdActivity; 
    
    // datos de estadistica del usuario
    private String worksHoursM;
    private String worksSessM;
    private String worksHoursY;
    private String worksSessY;
    
    private Logger log;
    

    
    
    public LoginBean() {
        
        log=Logger.getLogger("stdout");
        eventIdActivity="0";
    }

    
    /**
     * Metodo de comprobación del login en la aplicación por parte de un nuevo usuario.
     * Genera un usuario user static con los datos del usuario, si el login es correcto; 
     * si es incorrecto, genera un usuario null segun caso.
     * @return 
     */
    public String login() {
        
        // comprobamos longitudes de variables recibidas
        if (!(this.username==null || this.username.length()< MIN_LENGTH_USERNAME
                || this.username.length()> MAX_LENGTH_USERNAME ||
            this.userpass==null || this.userpass.length()< MIN_LENGTH_USERPASS
                || this.userpass.length()> MAX_LENGTH_USERPASS )) {
                        
            // instanciamos el component
            loginComponent=new LoginComponent();
            // sanitizamos los inputs
            username=verifyLoginInput(username);
            userpass=verifyLoginInput(userpass);
            
            // realizamos la comprobacion del user-pass introducido
            user=loginComponent.checkUserLogin(username,userpass); 
           
            // el user-pass no existe
            if (user==null) {
                this.loginmessage="Usuario-contraseña no válidos. Pruebe de nuevo";
                return "";
            }
            
            // login correcto
            // cuando un usuario se ha logueado correctamente en la aplicacion
            // se obtiene el objeto user completo, las estadisticas del usuario
            if (user.getId()>0) {
                
                // obtenemos variables estadisticas
                this.worksHoursM="00:00:00";
                this.worksSessM="0";
                this.setWorksHoursY("00:00:00");
                this.setWorksSessY("0");
                
                // instanciamos la lista de deportes
                userSportlist=new SportsListBean(LoginBean.user);

                // mensajes y logger
                this.loginmessage="Autenticación correcta "+user.getId();
                log.info("Usuario "+user.getNameuser()+" - id:"+user.getId()+" CONECTADO CORRECTAMENTE");
                
                // navegacion hacia pantalla principal de la aplicación
                return "main";
            }  
  
        } else {           
            this.loginmessage="Usuario-contraseña incorrectos. Pruebe de nuevo";
        }  
        
        return "";
    }

    
    /**
     * Este metodo borra los datos introducidos en el formulario
     */
    public void clear() {
        this.username="";
        this.userpass="";
        this.loginmessage="";
    }
    
    
    /**
     * Metodo para salir de forma segura de la aplicación.
     * @return 
     */
    public String exit() {

        // mensajes y logger
        this.loginmessage="Ha salido de la aplicación de forma correcta";
        log.info("Usuario "+user.getNameuser()+" - id:"+user.getId()+" DESCONECTADO CORRECTAMENTE");
                
        // borramos los datos del usuario y de la sesion
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        user=null;
        this.username="";
        this.userpass="";
                
        return "index";
    }
    
    
    /* ******************** GETTERS AND SETTERS ****************** */
    
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
     * @return the worksHoursM
     */
    public String getWorksHoursM() {
        
        AgendaDAO agDao=new AgendaDAO();
        String res=null;
        try {
            res = agDao.getHoursByLapseTime(30, user);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo horas de trabajo mensual para el user "+LoginBean.user.getId()+" - mensaje: "+ex);
        }
        
        if (res!=null) worksHoursM=res;           
        
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

        AgendaDAO agDao=new AgendaDAO();
        long res=0;
        try {
            res = agDao.getSessionsByLapseTime(30, user);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo sesiones de trabajo mensual para el user "+LoginBean.user.getId()+" - mensaje: "+ex);
        }
        
        if (res>0) worksSessM=String.valueOf(res);

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
        
        AgendaDAO agDao=new AgendaDAO();
        String res=null;
        try {
            res = agDao.getHoursByLapseTime(365, user);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo horas de trabajo anual para el user "+LoginBean.user.getId()+" - mensaje: "+ex);
        }
        
        if (res!=null) worksHoursY=res;      
        
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
        
        AgendaDAO agDao=new AgendaDAO();
        long res=0;
        try {
            res = agDao.getSessionsByLapseTime(365, user);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo sesiones de trabajo anual para el user "+LoginBean.user.getId()+" - mensaje: "+ex);
        }
        
        if (res>0) worksSessY=String.valueOf(res);
        
        return worksSessY;
    }

    /**
     * @param worksSessY the worksSessY to set
     */
    public void setWorksSessY(String worksSessY) {
        this.worksSessY = worksSessY;
    }
    
    
    public Usuarios getUser(){
        return user;
    }

    /**
     * @return the eventIdActivity
     */
    public String getEventIdActivity() {
        return eventIdActivity;
    }

    /**
     * @param eventIdActivity the eventIdActivity to set
     */
    public void setEventIdActivity(String eventIdActivity) {
        this.eventIdActivity = eventIdActivity;
    }

 
    
}
