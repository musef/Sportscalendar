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
import components.EstadisticasComponent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Agenda;
import models.Deportes;
import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
@ManagedBean
@ViewScoped
public class EstadisticasBean implements Serializable {

    //manager
    EstadisticasComponent estadisticasComponent;
    
    DeportesComponent deportesComponent;
    ActividadesComponent actividadesComponent;
    
    private List<Deportes> sportsList;
    private List<Actividades> activitiesList;
    private String message="";
    private long idxsport;
    private long idxactivity;
    private String fechini;
    private String fechfin;
    
    private List<Agenda> listaAgenda;
    private HashMap<String,String> resultAcum;
    
    private Logger log;
    
    
    public EstadisticasBean() {
        
        log=Logger.getLogger("stdout");   
        
        if (fechini==null) fechini="01-01-2010";
        if (fechfin==null) fechfin="31-12-2050";
        
        deportesComponent=new DeportesComponent();
        actividadesComponent=new ActividadesComponent();
        
        sportsList=LoginBean.userSportlist.getSportsList();
        
    }

    /**
     * Este metodo obtiene la lista de eventos del usuario, grabados en la DDBB
     * Los datos obtenidos dependen de la información del formulario
     * @return 
     */
    public String getRecordsList() {
 
        
        // instanciamos el manager
        estadisticasComponent=new EstadisticasComponent();
        
        // obtenemos el deporte, si procede
        Deportes deporte=estadisticasComponent.getSelectedSport(idxsport, LoginBean.user);

        // obtenemos la actividad, si procede
        Actividades actividad=estadisticasComponent.getSelectedActivity(idxactivity, LoginBean.user);
        
        System.out.println("THIS DATA:"+LoginBean.user+"**"+deporte.getSportName()+"**"+actividad.getName()+"**"+fechini+"**"+fechfin);
        // obtenemos la lista de los datos de la DDBB
        this.listaAgenda=estadisticasComponent.getAgendaList(LoginBean.user, deporte, actividad, fechini, fechfin);
        // obtenemos la estadistica global de esos datos
        resultAcum=estadisticasComponent.getSumDataAgendaList(LoginBean.user, deporte, actividad, fechini, fechfin);
        
        return "";
        
    }
    
    
    /**
     * Metodo para cambiar de deporte
     * @param e 
     */
    public void changeSportIdx(ValueChangeEvent e) {
        
        idxsport=Long.parseLong(e.getNewValue().toString());
        // recuperamos el deporte seleccionado
        Deportes selectedSport=deportesComponent.readSport(idxsport,LoginBean.user);
        // recuperamos la lista de actividades
        if (selectedSport!=null) activitiesList=actividadesComponent.allActivities(selectedSport,LoginBean.user);
        
    }
    
    /**
     * Metodo para cambiar de actividad
     * @param f
     */
    public void changeActivityIdx(ValueChangeEvent f) {
        
        idxactivity=Long.parseLong(f.getNewValue().toString());
        // instanciamos el manager
        estadisticasComponent=new EstadisticasComponent();        
        // obtenemos la actividad, si procede
        Actividades actividad=estadisticasComponent.getSelectedActivity(idxactivity, LoginBean.user);        
        
    }

    
    /* ***************** GETTERS AND SETTERS ************************ */
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param aMessage the message to set
     */
    public void setMessage(String aMessage) {
        message = aMessage;
    }

    /**
     * @return the idxsport
     */
    public long getIdxsport() {
        return idxsport;
    }

    /**
     * @param aIdxsport the idxsport to set
     */
    public void setIdxsport(long aIdxsport) {
        idxsport = aIdxsport;
    }

    /**
     * @return the idxactivity
     */
    public long getIdxactivity() {
        return idxactivity;
    }

    /**
     * @param aIdxactivity the idxactivity to set
     */
    public void setIdxactivity(long aIdxactivity) {
        this.idxactivity = aIdxactivity;
    }

    /**
     * @return the fechini
     */
    public String getFechini() {
        return fechini;
    }

    /**
     * @param fechini the fechini to set
     */
    public void setFechini(String fechini) {
        this.fechini = fechini;
    }

    /**
     * @return the fechfin
     */
    public String getFechfin() {
        return fechfin;
    }

    /**
     * @param fechfin the fechfin to set
     */
    public void setFechfin(String fechfin) {
        this.fechfin = fechfin;
    }

    /**
     * @return the listaAgenda
     */
    public List<Agenda> getListaAgenda() {
        return listaAgenda;
    }

    /**
     * @param listaAgenda the listaAgenda to set
     */
    public void setListaAgenda(List<Agenda> listaAgenda) {
        this.listaAgenda = listaAgenda;
    }

    /**
     * @return the resultAcum
     */
    public HashMap<String,String> getResultAcum() {
        return resultAcum;
    }

    /**
     * @param resultAcum the resultAcum to set
     */
    public void setResultAcum(HashMap<String,String> resultAcum) {
        this.resultAcum = resultAcum;
    }

    /**
     * @return the sportsList
     */
    public List<Deportes> getSportsList() {
        return sportsList;
    }

    /**
     * @param sportsList the sportsList to set
     */
    public void setSportsList(List<Deportes> sportsList) {
        this.sportsList = sportsList;
    }

    /**
     * @return the activitiesList
     */
    public List<Actividades> getActivitiesList() {
        return activitiesList;
    }

    /**
     * @param activitiesList the activitiesList to set
     */
    public void setActivitiesList(List<Actividades> activitiesList) {
        this.activitiesList = activitiesList;
    }
 
    
    
}
