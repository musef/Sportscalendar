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

import components.AgendaComponent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Agenda;
import models.Deportes;


/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped
public class AgendaBean {

    // manager
    AgendaComponent agendaComponent;

    private long sportidx;
    private long activityidx;
    private List<Deportes> sports;
    private List<Actividades> activities;
    private Actividades selectedActivity;
    private Deportes selectedSport;     

    private String thisday;
    private String nameact;
    private String distance;
    private String slope;
    private String duration;
    private String description;
    private String site;
    private String message;
    
    private SimpleDateFormat sdf;
    
    
    public AgendaBean() {
        
        
        sdf=new SimpleDateFormat("HH:mm:ss");
        if (this.message==null) this.message="";
        
    }
    
    /**
     * Este metodo genera la grabacion de una actividad deportiva en la agenda, segun
     * los datos obtenidos del formulario
     * @return 
     */
    public String recordThisActivity() {

        // obtenemos la fecha y creamos on objeto localDateTime
        int year=Integer.valueOf(this.thisday.substring(6));
        int month=Integer.valueOf(this.thisday.substring(3, 5));
        int day=Integer.valueOf(this.thisday.substring(0, 2));
        //LocalDateTime ldt=LocalDateTime.of(year, month, day, 0, 0);

        // convertimos el objeto a un Date
        LocalDateTime thisdate=LocalDateTime.of(year, month, day, 0, 0,0);
        Calendar ddate=Calendar.getInstance();
        ddate.setTimeInMillis(thisdate.toEpochSecond(ZoneOffset.UTC));
       
        // obtenemos el resto de los datos del formulario
        Float dst=Float.parseFloat(this.distance);
        Float slp=Float.parseFloat(this.slope);
        // creamos un objeto time para el tiempo de la actividad
        SimpleDateFormat st=new SimpleDateFormat("HH:mm:ss");
        Date thistime;
        try {
            thistime=st.parse(duration);
        } catch (ParseException ex) {
            thistime=new Date();
        }

        // generamos objeto vacio pero con id para relacion con agenda
        Deportes dp=new Deportes(sportidx);
        // generamos objeto vacio pero con id para relacion con agenda
        Actividades act=new Actividades(activityidx);
        
        // construimos un objeto Agenda con los datos procesados del formulario
        Agenda ag=new Agenda(LoginBean.user.getKeyuser(), ddate, dst, slp, thistime, this.description, LoginBean.user, dp, act);
        
        // instanciamos el manager
        agendaComponent=new AgendaComponent();
        
        // en funcion del resultado devolvemos el control a uno u otro
        if (agendaComponent.createEventCalendar(ag, LoginBean.user)) return "inputdata.xhmtl";     
        else return "main.xhtml";
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
        sportidx=Long.parseLong(e.getNewValue().toString());
        // recuperamos el objeto
        if (sportidx>0) {
            // recuperamos el deporte seleccionado
            agendaComponent=new AgendaComponent();
            
            selectedSport=agendaComponent.readSport(sportidx, LoginBean.user);            

            if (selectedSport!=null) {
                // y recuperamos la lista de actividades de ese deporte
                activities=agendaComponent.allActivities(selectedSport, LoginBean.user);
            }         
            // borramos la actividad que estuviera seleccionada
            this.selectedActivity=null;
            activityidx=0;
            
        } else {
            // borramos los datos  
            selectedSport=null;
            this.selectedActivity=null;
            activityidx=0;
            activities=null;          
        }
        this.nameact="";
        this.description="";
        this.site="";
        this.slope="0";
        this.distance="0";
        this.duration="00:00:00";            
            
    }    
    
    
    
    /**
     * Este metodo recupera el objeto Activity, en funcion
     * de la seleccion hecha en el select del formulario
     * Puede generar un objeto Activity vacio
     * @param f
     */
    public void changeActivity(ValueChangeEvent f) {
        
        // borramos mensaje
        message="";
        
        // actualizamos aqui
         activityidx=Long.parseLong(f.getNewValue().toString());
        // recuperamos el objeto
        if (activityidx>0) {
            
            agendaComponent=new AgendaComponent();
            
            selectedActivity=agendaComponent.readActivity(activityidx, LoginBean.user);
            
            if (selectedActivity!=null) {
                // mostramos los datos
                this.nameact=selectedActivity.getName();
                this.description=selectedActivity.getDescription();
                this.site=selectedActivity.getSite();
                this.slope=String.valueOf(selectedActivity.getSlope().toString());
                this.distance=String.valueOf(selectedActivity.getDistance());
                this.duration=sdf.format(selectedActivity.getTiming());                
            }
        }
    }
    
    
    /* ****************** GETTERS AND SETTERS ************************** */
    
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
     * @return the thisday
     */
    public String getThisday() {
        return thisday;
    }

    /**
     * @param thisday the thisday to set
     */
    public void setThisday(String thisday) {
        this.thisday = thisday;
    }

    /**
     * @return the nameact
     */
    public String getNameact() {
        //if (selectedActivity!=null) this.nameact=selectedActivity.getName();
        return nameact;
    }

    /**
     * @param nameact the nameact to set
     */
    public void setNameact(String nameact) {
        this.nameact = nameact;
    }

    /**
     * @return the distance
     */
    public String getDistance() {
       // if (selectedActivity!=null) this.distance=String.valueOf(selectedActivity.getDistance());
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return the slope
     */
    public String getSlope() {
        //if (selectedActivity!=null) this.slope=String.valueOf(selectedActivity.getSlope());
        return slope;
    }

    /**
     * @param slope the slope to set
     */
    public void setSlope(String slope) {
        this.slope = slope;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
       // if (selectedActivity!=null) this.distance=String.valueOf(selectedActivity.getDistance());
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        //if (selectedActivity!=null) this.description=selectedActivity.getDescription();
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the site
     */
    public String getSite() {
        //if (selectedActivity!=null) this.site=selectedActivity.getSite();
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the sports
     */
    public List<Deportes> getSports() {
        //if (sports==null) sports=LoginBean.user.getDeportesList();
        return sports;
    }

    /**
     * @param sports the sports to set
     */
    public void setSports(List<Deportes> sports) {
        this.sports = sports;
    }

    /**
     * @return the activities
     */
    public List<Actividades> getActivities() {
        /*
        if (sportidx>0) selectedSport=ddao.readSport(sportidx);
            else selectedSport=null;
        activities=adao.readAllSportActivities(selectedSport);  
        */
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(List<Actividades> activities) {
        this.activities = activities;
    }

    /**
     * @return the selectedActivity
     */
    public Actividades getSelectedActivity() {
        //if (selectedActivity==null && activityidx>0) selectedActivity=adao.readActivity(activityidx);        
        return selectedActivity;
    }

    /**
     * @param selectedActivity the selectedActivity to set
     */
    public void setSelectedActivity(Actividades selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    /**
     * @return the selectedSport
     */
    public Deportes getSelectedSport() {
        //if (selectedSport==null && sportidx>0) selectedSport=ddao.readSport(sportidx);
        return selectedSport;
    }

    /**
     * @param selectedSport the selectedSport to set
     */
    public void setSelectedSport(Deportes selectedSport) {
        this.selectedSport = selectedSport;
    }
    
    
   
}
