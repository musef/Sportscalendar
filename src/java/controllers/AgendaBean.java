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
import components.LibraryClass;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Agenda;
import models.Deportes;

/**
 * Flujo de datos en modificación.
 * Se precarga en constructor la lista de deportes del usuario (es un static)
 * Como parametro inyectado viene thisEventToModify (static), con el id del evento a cargar 
 * En el postConstruct se lee el evento, el cual carga los datos del evento y los idx del deporte y la actividad
 * En el getSportidx leemos el valor inyectado y obtenemos el bean del deporte correspondiente, y cargamos la
 *  lista con los valores de la actividad que corresponde al deporte
 * Y finalmente, en getActivityidx leemos el valor inyectado que cambia el select de la lista anteriormente cargada
 */

/**
 * REGLA DE NEGOCIO:
 * No se graban dos eventos en el mismo día. Solo se manejará un evento diario.
 */

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped
public class AgendaBean extends LibraryClass implements Serializable {

    // manager
    AgendaComponent agendaComponent;

    // objetos seleccionables
    private long sportidx;
    private long activityidx;
    private List<Deportes> sports;
    private List<Actividades> activities;
    private Actividades selectedActivity;
    private Deportes selectedSport;     

    // inyectamos del objeto session del usuario 
    // la actividad seleccionada
    @ManagedProperty(value="#{loginBean.eventIdActivity}")
    private String thisEventToModify;

    // campos de formulario
    private String thisday;    
    private String nameact;
    private String distance;
    private String slope;
    private String duration;
    private String description;
    private String site;
    
    // mensaje informativo
    private String message;
    
    // formatters
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdt;
    
    
    
    public AgendaBean() {
           
        sdt=new SimpleDateFormat("dd-MM-yyyy");
        sdf=new SimpleDateFormat("HH:mm:ss");
        if (this.message==null) this.message="";
        
        // rellenamos la lista de deportes con la lista del user
        sports=LoginBean.userSportlist.getSportsList();
        
        // si no tenemos dia definido por parametro
        if (thisday==null) {                
           Date ldt=new Date();      
           thisday=sdt.format(ldt);
        }
                
    }
    
    @PostConstruct
    private void init() {
        // si este atributo tiene valor, es que tenemos que obtener este evento
        // para mostrarlo en modo modificacion
        if (thisEventToModify!=null && !thisEventToModify.isEmpty()) {
            // instanciamos el manager
            agendaComponent=new AgendaComponent();

            Agenda thisEvent=agendaComponent.readEvent(Long.parseLong(thisEventToModify), LoginBean.user);
            
            if (thisEvent!=null) {  
                // mostramos los datos
                this.nameact=thisEvent.getIdactivity().getName();
                this.description=thisEvent.getComments();
                this.site=thisEvent.getIdactivity().getSite();
                this.slope=String.valueOf(thisEvent.getSlope());
                this.distance=String.valueOf(thisEvent.getDistance());
                this.duration=sdf.format(thisEvent.getTiming());
                this.sportidx=thisEvent.getIdsport().getId();
                this.activityidx=thisEvent.getIdactivity().getId();
                this.thisday=sdt.format(thisEvent.getDate());
                
            }
        }
    }
    
    
    /**
     * Este metodo genera la grabacion de una actividad deportiva en la agenda, segun
     * los datos obtenidos del formulario.
     * Si el atributo thisEventToModify es empty, es una nueva grabación; en caso de 
     * modificación, contiene el id del evento de agenda a modificar
     * @return 
     */
    public String recordThisActivity() {

            thisday=verifyFormsInput(thisday);    
            duration=verifyFormsInput(duration);            
            description=verifyFormsInput(description);
        
        // obtenemos la fecha y creamos objeto para calendar
        int year=Integer.valueOf(this.thisday.substring(6));
        int month=Integer.valueOf(this.thisday.substring(3, 5));
        month--; // calendar empieza en mes 0 = Enero
        int day=Integer.valueOf(this.thisday.substring(0, 2));

        // convertimos el objeto a un Date        
        TimeZone tz=TimeZone.getTimeZone("Europe/Madrid");
        Calendar cal=Calendar.getInstance(tz);
        cal.set(year, month, day, 8, 0,0);
        Date dateday=new Date();
        dateday.setTime(cal.getTimeInMillis());
               
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
        Agenda ag=new Agenda(LoginBean.user.getKeyuser(), dateday, dst, slp, thistime, this.description, LoginBean.user, dp, act);       
        
        // instanciamos el manager
        agendaComponent=new AgendaComponent();
        
        if (thisEventToModify==null || thisEventToModify.isEmpty() || thisEventToModify.equals("0")) {
            // estamos en el caso de grabación de un nuevo evento
            
            // REGLA DE NEGOCIO : no grabaremos dos eventos el mismo día
            // por motivos de simplificacion de la app
            if (agendaComponent.readEventByDate(LoginBean.user, thisday)==null) {
                // en este caso, no hay eventos grabados este dia               
                if (agendaComponent.createEventCalendar(ag, LoginBean.user)) {
                    // en funcion del resultado devolvemos el mensaje
                    setMessage("Evento de agenda grabado correctamente");
                    // y recuperamos el objeto
                    ag=agendaComponent.readEventByDate(LoginBean.user, thisday);
                    // y retomamos el id del componente para modificacion o borrado
                    LoginBean.eventIdActivity=String.valueOf(ag.getId());
                    thisEventToModify=String.valueOf(ag.getId());
                } else {
                    setMessage("NO ha sido posible grabar un nuevo evento en la agenda");
                }                 
            } else setMessage("No es posible grabar varios eventos en el mismo día");
            
            
        } else {
            // estamos en el caso de modificación de un evento existente
            ag.setId(Long.parseLong(thisEventToModify));
            // en funcion del resultado devolvemos el mensaje
            if (agendaComponent.modifyEventCalendar(ag, LoginBean.user)) {
                setMessage("Evento de agenda modificado correctamente");
            } else {
                setMessage("NO ha sido posible modificar el evento en la agenda");
            }                         
        }
          
        return "";
    }

    
    public void deleteThisEvent() {
        
        // si este atributo tiene valor, entonces podemos borrarlo      
        if (thisEventToModify!=null && !thisEventToModify.isEmpty()) {
            // instanciamos el manager
            agendaComponent=new AgendaComponent();

            int deleting=agendaComponent.deleteEvent(Long.parseLong(thisEventToModify), LoginBean.user);
            
            if (deleting==1) {
                // ponemos a cero el id del objeto por si queremos crear otro
                LoginBean.eventIdActivity="0";
                setMessage("Evento de agenda borrado correctamente");
                this.nameact="";
                this.description="";
                this.site="";
                this.slope="0";
                this.distance="0";
                this.duration="00:00:00"; 
            } else if (deleting==0) {
                setMessage("No pudo borrarse por error en los datos suministrados");
            } else if (deleting==-99) {
                setMessage("Evento no borrado: error interno");
            } else {
                setMessage("No es posible borrar este evento");                
            }
        }                        
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
    
    /**
     * 
     */
    public void showActivityData() {

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
        if (sportidx>0) {
            // recuperamos el deporte seleccionado
            agendaComponent=new AgendaComponent();
            
            selectedSport=agendaComponent.readSport(sportidx, LoginBean.user);            

            if (selectedSport!=null) {
                // y recuperamos la lista de actividades de ese deporte
                activities=agendaComponent.allActivities(selectedSport, LoginBean.user);
            }                     
        }          
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
        return selectedSport;
    }

    /**
     * @param selectedSport the selectedSport to set
     */
    public void setSelectedSport(Deportes selectedSport) {
        this.selectedSport = selectedSport;
    }

    /**
     * @return the thisEventToModify
     */
    public String getThisEventToModify() {
        return thisEventToModify;
    }

    /**
     * @param thisEventToModify the thisEventToModify to set
     */
    public void setThisEventToModify(String thisEventToModify) {
        this.thisEventToModify = thisEventToModify;
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
