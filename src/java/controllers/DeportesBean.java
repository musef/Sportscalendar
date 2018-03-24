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

import components.DeportesComponent;
import javax.faces.bean.ManagedBean;
import java.util.List;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import models.Deportes;


/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped()
public class DeportesBean {

    //manager
    private DeportesComponent deportesComponent;
    
    private Deportes sport;
    private List<Deportes> sports;
    
    private long sportidx;
    private String sportName;
    private String sportDescription;
    
    // mensaje de operaciones
    private String message;

    
    
    
    public DeportesBean() {
        // recuperamos la lista actualizada de deportes
        this.sports =LoginBean.userSportlist.getSportsList();
        if (sport!=null) {
            this.sportidx=sport.getId();
            this.sportName=sport.getSportName();
            this.sportDescription=sport.getSportDescrip();             
        }
    }

    
    /**
     * Este metodo elimina un objeto Deportes, en funcion
     * del indice del select sportidx.
     * Genera un mensaje con el resultado de la operacion
     */    
    public void deleteSport() {
        
        if (sportidx!=0) {

            deportesComponent=new DeportesComponent();

            // instanciamos el borrado del deporte
            boolean result=deportesComponent.deleteSport(sportidx,LoginBean.user);
            if (result) {
                // actualizamos la lista de sportsList
                LoginBean.userSportlist.setSportsList(deportesComponent.allSports(LoginBean.user));
                message="Deporte "+sportName+" borrado correctamente";
                this.sportName="";
                this.sportDescription="";
                this.sportidx=0;                
            } else message="No ha sido posible borrar este deporte";

            // recuperamos la lista actualizada de deportes
            this.sports =LoginBean.userSportlist.getSportsList();
            
        }
    }

    
    /**
     * Este metodo graba o actualiza un objeto Deportes, en funcion
     * del indice del select sportidx.
     * Genera un mensaje con el resultado de la operacion
     * @return String con redireccion
     */
    public String recordSport() {
        
        // vamos a grabar el objeto Deportes
        
        // instanciamos el manager
        deportesComponent=new DeportesComponent();
        
        // puede ser una nueva modificacion (sportidx>0) o nuevo valor        
        if (sportidx==0) {
            
            // nueva grabacion
            Deportes newsport=new Deportes(LoginBean.user.getKeyuser(), sportName, sportDescription, LoginBean.user);
            if (deportesComponent.createSport(newsport)) {
                // actualizamos la lista de sportsList
                LoginBean.userSportlist.setSportsList(deportesComponent.allSports(LoginBean.user));
                message="Nuevo deporte grabado correctamente";
            } else message="No ha sido posible grabar un nuevo deporte";

        } else {
            
            // modificacion
            sport.setSportName(sportName);
            sport.setSportDescrip(sportDescription);
            if (deportesComponent.updateSport(sport)) {
                // actualizamos la lista de sportsList
                LoginBean.userSportlist.setSportsList(deportesComponent.allSports(LoginBean.user));
                this.sportName="";
                this.sportDescription="";
                this.sportidx=0;
                this.message="Deporte modificado correctamente";            
            } else message="No ha sido posible modificar el deporte";

        }
        
        // recuperamos la lista actualizada de deportes
        this.sports =LoginBean.userSportlist.getSportsList();
        
        return null;
    }

    
    /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de la seleccion hecha en el select del formulario
     * Puede generar un objeto Deportes vacio
     * @param e 
     */
    public void changeSport(ValueChangeEvent e) {
        
        // actualizamos aqui
         sportidx=Long.parseLong(e.getNewValue().toString());
        // recuperamos el objeto
        if (sportidx>0) {
 
            // obtenemos el objeto Deportes
            deportesComponent=new DeportesComponent();
            sport=deportesComponent.readSport(sportidx, LoginBean.user);
            
            if (sport!=null) {
                // mostramos los datos
                this.sportName=sport.getSportName();
                this.sportDescription=sport.getSportDescrip();                
            }

        } else {
            // borramos los datos
            this.sportName="";
            this.sportDescription="";    
            sport=null;
        }
        // borramos los posibles mensajes mostrados
        message="";

    }
    
    
    /* ******* GETTERS AND SETTERS ************** */
    
    /**
     * @return the sport
     */
    public Deportes getSport() {
        return sport;
    }

    /**
     * @param sport the sport to set
     */
    public void setSport(Deportes sport) {
        this.sport = sport;
    }

    /**
     * @return the sports
     */
    public List<Deportes> getSports() {
        // rellenamos la lista de deportes
        return this.sports;
    }

    /**
     * @param sports the sports to set
     */
    public void setSports(List<Deportes> sports) {
        this.sports = sports;
    }

    /**
     * @return the sportName
     */
    public String getSportName() {
        return sportName;
    }

    /**
     * @param sportName the sportName to set
     */
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    /**
     * @return the sportDescription
     */
    public String getSportDescription() {
        return sportDescription;
    }

    /**
     * @param sportDescription the sportDescription to set
     */
    public void setSportDescription(String sportDescription) {
        this.sportDescription = sportDescription;
    }

    /**
     * @return the sportidx
     */
    public long getSportidx() {
        return sportidx;
    }

    /**
     * @param sportidx the sportidx to set
     */
    public void setSportidx(long sportidx) {
        this.sportidx = sportidx;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


    
    
}
