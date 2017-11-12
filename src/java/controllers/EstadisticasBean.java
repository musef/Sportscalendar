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
import daos.AgendaDAO;
import daos.DeportesDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import models.Actividades;
import models.Agenda;
import models.Deportes;

/**
 *
 * @author floren
 */
@ManagedBean
@RequestScoped
public class EstadisticasBean {

    private static String message="";
    private static long idxsport;
    private long idxactivity;
    private String fechini;
    private String fechfin;
    
    private AgendaDAO agdao;
    private DeportesDAO ddao;
    private ActividadesDAO acdao;
    
    private List<Agenda> listaAgenda;
    
    
    
    public EstadisticasBean() {
        
        if (agdao==null) agdao=new AgendaDAO();
        
        if (fechini==null) fechini="01-01-2010";
        if (fechfin==null) fechfin="31-12-2050";
        
    }

    
    public String getRecordsList() {
        
        if (ddao==null) ddao=new DeportesDAO();
        if (acdao==null) acdao=new ActividadesDAO();
        
        Deportes deporte=null;
        if (idxsport>0) deporte=ddao.readSport(idxsport);
        
        Actividades actividad=null;
        if (idxactivity>0) actividad=acdao.readActivity(idxactivity);
        
        System.out.println("Data:"+idxsport+"-"+idxactivity+"-"+fechini+"-"+fechfin);
        
        setListaAgenda(agdao.listCalendar(LoginBean.user, deporte, actividad, fechini, fechfin));
        
        if (listaAgenda!=null) System.out.println("Tamaño:"+listaAgenda.size());
        
        
        return "estadisticas";
        
    }
    
    
    public void changeSportIdx(ValueChangeEvent e) {
        
        idxsport=Long.parseLong(e.getNewValue().toString());
        
        
    }
    
    /**
     * @return the message
     */
    public static String getMessage() {
        return message;
    }

    /**
     * @param aMessage the message to set
     */
    public static void setMessage(String aMessage) {
        message = aMessage;
    }

    /**
     * @return the idxsport
     */
    public static long getIdxsport() {
        return idxsport;
    }

    /**
     * @param aIdxsport the idxsport to set
     */
    public static void setIdxsport(long aIdxsport) {
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
    
}
