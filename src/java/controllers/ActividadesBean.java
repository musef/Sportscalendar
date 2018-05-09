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


import components.ActividadesComponent;
import components.DeportesComponent;
import components.LibraryClass;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Deportes;
import org.apache.log4j.Logger;

/**
 * REGLAS DE NEGOCIO:
 * 
 * - Todos los usuarios pueden grabar sus actividades
 * - El nombre de la actividad puede estar repetida, incluso en el mismo usuario
 * - Todos los usuarios pueden modificar sus actividades
 * - Todos los usuarios pueden eliminar sus actividades, excepto el usuario anonimo
 * - No es posible eliminar una actividad que tenga eventos grabados en la agenda
 */


/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped
public class ActividadesBean extends LibraryClass implements Serializable {

    //managers
    private ActividadesComponent actividadesComponent;
    private DeportesComponent deportesComponent;
    
    private Actividades activity;
    private Deportes selectedSport;   
    private long sportidx;
    private List<Deportes> sports;
    private List<Actividades> activities;
    
    private long activityidx;
    private String activityName;
    private float activitySlope;
    private float activityDistance;
    private String activityTimming;
    private String activitySite;
    private String activityDescription;
    
    // flag sport changed
    boolean sportChanged=false;
    
    // mensaje de operaciones
    private String message;
    
    private Logger log;
    
    // habilitar o deshabilitar boton de borrado
    private boolean disableDeleteButton;
        // habilitar / deshabilitar boton de grabar
    private boolean disableRecordButton;
   
    
    /**
     * Creates a new instance of ActividadesBean
     */
    public ActividadesBean() {
        
        actividadesComponent=new ActividadesComponent();
        
        // recuperamos la lista de deportes
        sports=LoginBean.userSportlist.getSportsList();  
        // recuperamos la lista de actividades
        if (selectedSport!=null)activities=actividadesComponent.allActivities(selectedSport,LoginBean.user);
        
        // habilitar / deshabilitar boton de borrado
        disableDeleteButton=LoginBean.user.getKeyuser().equals("anonimo");
        // deshabilitar boton de grabar
        disableRecordButton=true;
        
        log=Logger.getLogger("stdout");
    }
 
    
    /**
     * Este metodo elimina un objeto Actividades, en funcion
     * del indice del select activityidx.
     * Genera un mensaje con el resultado de la operacion
     */    
    public void deleteActivity() {
        
        // en modo anonino no esta permitido el borrado de deportes
        if (!LoginBean.user.getKeyuser().equals("anonimo")) {
            if (getActivityidx()!=0) {

                actividadesComponent=new ActividadesComponent();
                deportesComponent=new DeportesComponent();

                // instanciamos el borrado de la actividad
                int result=actividadesComponent.deleteActivity(getActivityidx(),LoginBean.user);
                if (result==1) {
                    setMessage("La actividad ha sido borrada correctamente");
                    // actualizamos la lista despues de operacion DDBB
                    setActivities(actividadesComponent.allActivities(selectedSport, LoginBean.user));
                    activityidx=0;
                    sportidx=selectedSport.getId();
                    // borramos los datos en formulario
                    this.activityName="";
                    this.activityDescription="";
                    this.activitySite="";
                    this.activitySlope=0;
                    this.activityDistance=0;
                    this.activityTimming="";                 
                } else if (result==0) {
                    setMessage("Actividad no borrada: había algún dato incorrecto en la petición");
                } else if (result==-1) {
                    setMessage("Actividad no borrada: tiene eventos grabados en la agenda");
                } else  {
                    setMessage("No ha sido posible borrar esta actividad: error interno");
                }

            }            
        } else message="En modo anónimo no es posible borrar actividades";
        
    }

    
    /**
     * Este metodo graba o actualiza un objeto Deportes, en funcion
     * del indice del select sportidx.
     * Genera un mensaje con el resultado de la operacion
     * @return String con redireccion
     */
    public String recordActivity() {
        
        // sanitizamos entradas
        activityName=verifyFormsInput(activityName);
        activityDescription=verifyFormsInput(activityDescription);
        activitySite=verifyFormsInput(activitySite);
        activityTimming=verifyFormsInput(activityTimming);       
        
        //manager
        actividadesComponent=new ActividadesComponent();
        
        // vamos a grabar el objeto Deportes
        // puede ser una nueva modificacion (sportidx>0) o nuevo valor
        if (activityidx==0) {

            // nueva grabacion
            SimpleDateFormat sd=new SimpleDateFormat("HH:mm:ss");
            Date thistime;
            try {
                thistime=sd.parse(getActivityTimming());
            } catch (ParseException ex) {
                thistime=new Date();
            }
            Actividades newactivity=new Actividades(LoginBean.user.getKeyuser(), activityName, activityDistance, activitySlope, thistime, activitySite, activityDescription, LoginBean.user, getSelectedSport());

            boolean result=actividadesComponent.createActivity(newactivity,LoginBean.user);
            if (result) {
                setMessage("Nueva actividad grabada correctamente");
                // actualizamos la lista despues de operacion DDBB
                setActivities(actividadesComponent.allActivities(selectedSport,LoginBean.user));
                activityidx=0;
                sportidx=selectedSport.getId();
            } else setMessage("No ha sido posible grabar una nueva actividad");
            // borramos los datos en formulario
            this.activityName="";
            this.activityDescription="";
            this.activitySite="";
            this.activitySlope=0;
            this.activityDistance=0;
            this.activityTimming="";            
            

        } else {
   
            if (activity==null && activityidx>0) activity=actividadesComponent.readActivity(activityidx,LoginBean.user);
            // modificacion
            activity.setName(activityName);
            activity.setDescription(activityDescription);
            activity.setSite(activitySite);
            activity.setSlope(activitySlope);
            activity.setDistance(activityDistance);
            activity.setTiming(Time.valueOf(activityTimming));

            boolean result=actividadesComponent.updateActivity(getActivity(),LoginBean.user);
            sportidx=selectedSport.getId();
            if (result) {
                setMessage("Actividad modificada correctamente");
                // actualizamos la lista despues de operacion DDBB
                setActivities(actividadesComponent.allActivities(selectedSport,LoginBean.user));  
                            // borramos los datos en formulario
                this.activityName="";
                this.activityDescription="";
                this.activitySite="";
                this.activitySlope=0;
                this.activityDistance=0;
                this.activityTimming="";
            } else setMessage("No ha sido posible modificar la actividad");

        }
        return "";
    }

    
     /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de la seleccion hecha en el select del formulario
     * Eso altera la seleccion de objetos Actividades, que hay que rehacer
     * Puede generar un objeto Deportes vacio
     * @param e 
     */
    public void changeSport(ValueChangeEvent e) {
        
        //manager
        deportesComponent=new DeportesComponent();
        
        // actualizamos el indice
        sportidx=Long.parseLong(e.getNewValue().toString());
        
        // habilitamos/deshabilitamos grabar
        this.disableRecordButton= (sportidx == 0);
            
        
        // recuperamos el objeto
        readSportAndGetActivities();
        
        sportChanged=true;
        this.activityName="";
        this.activityDescription="";
        this.activitySite="";
        this.activitySlope=0;
        this.activityDistance=0;
        this.activityTimming="00:00:00";
            
    }
    
    
    /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de la seleccion hecha en el select del formulario
     * Puede generar un objeto Deportes vacio
     * @param e 
     */
    public void changeActivity(ValueChangeEvent e) {
        
        // borramos mensaje
        message="";
        
        // actualizamos aqui
         activityidx=Long.parseLong(e.getNewValue().toString());
        // recuperamos el objeto
        if (activityidx>0) {
            
            actividadesComponent=new ActividadesComponent();
            // leemos la actividad
            activity=actividadesComponent.readActivity(activityidx, LoginBean.user);

            // mostramos los datos
            this.activityName=activity.getName();
            this.activityDescription=activity.getDescription();
            this.activitySite=activity.getSite();
            this.activitySlope=activity.getSlope();
            this.activityDistance=activity.getDistance();
            this.activityTimming=new SimpleDateFormat("HH:mm:ss").format(activity.getTiming());
        } 

    }

    /**
     * Obtiene la lista de los actividades a traves del deporte seleccionado
     */
    public void readSportAndGetActivities() {
        
        // recuperamos el objeto
        if (sportidx>0) {            
            // recuperamos el deporte seleccionado
            selectedSport=deportesComponent.readSport(sportidx,LoginBean.user);
        } else {
            // borramos los datos  
            selectedSport=null;
            // borramos los datos en formulario
            this.activityName="";
            this.activityDescription="";
            this.activitySite="";
            this.activitySlope=0;
            this.activityDistance=0;
            this.activityTimming="";            
        }
        
        // y recuperamos la lista de actividades de ese deporte
        activities=actividadesComponent.allActivities(selectedSport, LoginBean.user);
        // borramos la actividad que estuviera seleccionada
        activity=null;
        activityidx=0;
        
    }
    

    /* ********************* GETTERS AND SETTERS ************************** */
    
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

    public long getSportidx () {
        return sportidx;
    }
    
    public void setSportidx (long sportidx2) {
        sportidx=sportidx2;
    }
    
        /**
     * @return the disableDeleteButton
     */
    public boolean isDisableDeleteButton() {
        return disableDeleteButton;
    }

    /**
     * @param disableDeleteButton the disableDeleteButton to set
     */
    public void setDisableDeleteButton(boolean disableDeleteButton) {
        this.disableDeleteButton = disableDeleteButton;
    }
    
        /**
     * @return the disableRecordButton
     */
    public boolean isDisableRecordButton() {
        return disableRecordButton;
    }

    /**
     * @param disableRecordButton the disableRecordButton to set
     */
    public void setDisableRecordButton(boolean disableRecordButton) {
        this.disableRecordButton = disableRecordButton;
    }
    
}
