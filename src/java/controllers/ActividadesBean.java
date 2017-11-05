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

import daos.ActividadesDAO;
import daos.DeportesDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Deportes;

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped
public class ActividadesBean {

    private ActividadesDAO adao;
    private DeportesDAO ddao;
    private Actividades activity;
    private Deportes selectedSport;    
    private List<Deportes> sports;
    private List<Actividades> activities;
    
    private long activityidx;
    private String activityName;
    private float activitySlope;
    private float activityDistance;
    private String activityTimming;
    private String activitySite;
    private String activityDescription;
    
    // mensaje de operaciones
    private String message;
    
    
    /**
     * Creates a new instance of ActividadesBean
     */
    public ActividadesBean() {
        
        adao=new ActividadesDAO();
        ddao=new DeportesDAO();
        
    }
 
    
    /**
     * Este metodo elimina un objeto Actividades, en funcion
     * del indice del select activityidx.
     * Genera un mensaje con el resultado de la operacion
     * @return String con redireccion
     */    
    public String deleteActivity() {
        
        if (getActivityidx()!=0) {
            boolean result=adao.deleteActivity(getActivityidx());
            if (result) setMessage("Nueva actividad borrada correctamente");
            else setMessage("No ha sido posible borrar una nueva actividad");
        }
        return "actividades";
    }

    
    /**
     * Este metodo graba o actualiza un objeto Deportes, en funcion
     * del indice del select sportidx.
     * Genera un mensaje con el resultado de la operacion
     * @return String con redireccion
     */
    public String recordActivity() {
        
        // vamos a grabar el objeto Deportes
        // puede ser una nueva modificacion (sportidx>0) o nuevo valor
        if (getActivityidx()==0) {
            // nueva grabacion
            SimpleDateFormat sd=new SimpleDateFormat("HH:mm:ss");
            Date thistime;
            try {
                thistime=sd.parse(getActivityTimming());
            } catch (ParseException ex) {
                thistime=new Date();
            }
            Actividades newactivity=new Actividades(LoginBean.user.getKeyuser(), getActivityName(), getActivityDistance(), getActivitySlope(), thistime, getActivitySite(), getActivityDescription(), LoginBean.user, getSelectedSport());
            
            boolean result=adao.createActivity(newactivity);
            if (result) setMessage("Nueva actividad grabada correctamente");
            else setMessage("No ha sido posible grabar una nueva actividad");
        } else {
            // modificacion
            activity.setName(activityName);
            activity.setDescription(activityDescription);
            activity.setSite(activitySite);
            activity.setSlope(activitySlope);
            activity.setDistance(activityDistance);
            activity.setTiming(new Date(activityTimming));
            
            boolean result=adao.updateActivity(getActivity());
            if (result) setMessage("Actividad modificada correctamente");
            else setMessage("No ha sido posible modificar la actividad");            
        }
        return "actividades";
    }

    
     /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de la seleccion hecha en el select del formulario
     * Eso altera la seleccion de objetos Actividades, que hay que rehacer
     * Puede generar un objeto Deportes vacio
     * @param e 
     */
    public void changeSport(ValueChangeEvent e) {
        
        // actualizamos aqui
         long sportidx=Long.parseLong(e.getNewValue().toString());
        // recuperamos el objeto
        if (sportidx>0) {
            // recuperamos el deporte seleccionado
            selectedSport=ddao.readSport(sportidx);
            // y recuperamos la lista de actividades de ese deporte
            activities=selectedSport.getRecorridosList();
            // borramos la actividad que estuviera seleccionada
            activity=null;
            activityidx=0;
        } else {
            // borramos los datos  
            selectedSport=null;
            activity=null;
            activityidx=0;
            this.activities=null;
            this.activityName="";
            this.activityDescription="";
            this.activitySite="";
            this.activitySlope=0;
            this.activityDistance=0;
            this.activityTimming="00:00:00";
        }

    }
    
    
        /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de la seleccion hecha en el select del formulario
     * Puede generar un objeto Deportes vacio
     * @param e 
     */
    public void changeActivity(ValueChangeEvent e) {
        
        // actualizamos aqui
         activityidx=Long.parseLong(e.getNewValue().toString());
        // recuperamos el objeto
        if (activityidx>0) {
            activity=adao.readActivity(activityidx);
            // mostramos los datos
            this.activityName=activity.getName();
            this.activityDescription=activity.getDescription();
            this.activitySite=activity.getSite();
            this.activitySlope=activity.getSlope();
            this.activityDistance=activity.getDistance();
            this.activityTimming=new SimpleDateFormat("HH:mm:ss").format(activity.getTiming());
        } else {
            // borramos los datos
            activity=null;
            this.activityName="";
            this.activityDescription="";
            this.activitySite="";
            this.activitySlope=0;
            this.activityDistance=0;
            this.activityTimming="00:00:00";
        }

    }
    
    /**
     * @return the activity
     */
    public Actividades getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Actividades activity) {
        this.activity = activity;
    }

    /**
     * @return the sports
     */
    public List<Deportes> getSports() {
        sports=LoginBean.user.getDeportesList();
        return sports;
    }

    /**
     * @param sports the sports to set
     */
    public void setSports(List<Deportes> sports) {
        this.sports = sports;
    }

    /**
     * @return the selectedSport
     */
    public Deportes getSelectedSport() {
        return selectedSport;
    }

    /**
     * @param selectedSport the selectedSport to set
     */
    public void setSelectedSport(Deportes selectedSport) {
        this.selectedSport = selectedSport;
    }

    /**
     * @return the activities
     */
    public List<Actividades> getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(List<Actividades> activities) {
        this.activities = activities;
    }

    /**
     * @return the activityidx
     */
    public long getActivityidx() {
        if (activity!=null) {
            activityidx=activity.getId();
            activityName=activity.getName();
            activityDistance=activity.getDistance();
            activityDescription=activity.getDescription();
            activitySlope=activity.getSlope();
            activityTimming=new SimpleDateFormat("HH:mm:ss").format(activity.getTiming());
            activitySite=activity.getSite();
        } else {
            activityidx=0;
            this.activityName="";
            this.activityDescription="";
            this.activitySite="";
            this.activitySlope=0;
            this.activityDistance=0;
            this.activityTimming="00:00:00";
        }
        return activityidx;
    }

    /**
     * @param activityidx the activityidx to set
     */
    public void setActivityidx(long activityidx) {
        this.activityidx = activityidx;
    }

    /**
     * @return the activityName
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * @param activityName the activityName to set
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * @return the activitySlope
     */
    public float getActivitySlope() {
        return activitySlope;
    }

    /**
     * @param activitySlope the activitySlope to set
     */
    public void setActivitySlope(float activitySlope) {
        this.activitySlope = activitySlope;
    }

    /**
     * @return the activityDistance
     */
    public float getActivityDistance() {
        return activityDistance;
    }

    /**
     * @param activityDistance the activityDistance to set
     */
    public void setActivityDistance(float activityDistance) {
        this.activityDistance = activityDistance;
    }

    /**
     * @return the activityTimming
     */
    public String getActivityTimming() {
        return activityTimming;
    }

    /**
     * @param activityTimming the activityTimming to set
     */
    public void setActivityTimming(String activityTimming) {
        this.activityTimming = activityTimming;
    }

    /**
     * @return the activitySite
     */
    public String getActivitySite() {
        return activitySite;
    }

    /**
     * @param activitySite the activitySite to set
     */
    public void setActivitySite(String activitySite) {
        this.activitySite = activitySite;
    }

    /**
     * @return the activityDescription
     */
    public String getActivityDescription() {
        return activityDescription;
    }

    /**
     * @param activityDescription the activityDescription to set
     */
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


    
}
