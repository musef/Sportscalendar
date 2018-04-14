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
package components;

import daos.ActividadesDAO;
import daos.AgendaDAO;
import daos.DeportesDAO;
import java.util.HashMap;
import java.util.List;
import models.Actividades;
import models.Agenda;
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
public class EstadisticasComponent {
    
    private AgendaDAO agdao;
    private DeportesDAO ddao;
    private ActividadesDAO acdao;
    
    private Logger log;
    
    public EstadisticasComponent() {
        
        log=Logger.getLogger("stdout");
        
        
    }
    
    
    /**
     * Este metodo obtiene una lista de eventos de la agenda, segun los parametros suministrados
     * @param user
     * @param sport
     * @param activity
     * @param fechini
     * @param fechfin
     * @return null | List Agenda
     */
    public List<Agenda> getAgendaList(Usuarios user, Deportes sport, Actividades activity, String fechini, String fechfin) {
        
        //chequeamos los parametros
        if (user==null) return null;        
        if (fechini==null || fechini.length()!=10) return null;
        if (fechfin==null || fechfin.length()!=10) return null;
        
        // instanciamos los daos
        agdao=new AgendaDAO();
        
        List<Agenda> agendaList=null;
        
        // buscamos la informacion seleccionando entre los parametros
        try {
            if (sport==null) {
                // solo queremos un listado entre fechas
                agendaList= agdao.listCalendar(user,fechini, fechfin);

            } else if (activity==null) {
                // queremos un listado de actividades de un deporte entre fechas
                agendaList=agdao.listCalendar(user, sport, fechini, fechfin);
            } else {
                // queremos un listado de actividades entre fechas
                agendaList=agdao.listCalendar(user, sport, activity, fechini, fechfin);            
            }          
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal obteniendo lista de actividades por parametros para el user "+user.getId()+" - mensaje: "+ex);
        }
        if (agendaList!=null) System.out.println("VER LISTA:"+agendaList.size());
        else System.out.println("VER LISTA NULL");
        return agendaList;
        
    }
    
    /**
     * Este metodo obtiene de la base de datos el acumulado de resultados entre fechas, segun
     * los parametros suministrados
     * @param user
     * @param sport
     * @param activity
     * @param fechini
     * @param fechfin
     * @return null | HashMap
     */
    public HashMap<String,String> getSumDataAgendaList(Usuarios user, Deportes sport, Actividades activity, String fechini, String fechfin) {
        
        //chequeamos los parametros
        if (user==null) return null;        
        if (fechini==null || fechini.length()!=10) return null;
        if (fechfin==null || fechfin.length()!=10) return null;
        
        // instanciamos los daos
        agdao=new AgendaDAO();
        
        HashMap<String,String> sumData=null;
        
        // buscamos la informacion seleccionando entre los parametros
        try {
            // pasamos la informacion al dao
            sumData=agdao.getSessionsUntilDates(user, sport, activity, fechini, fechfin);
         
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal obteniendo lista de actividades por parametros para el user "+user.getId()+" - mensaje: "+ex);
        }
        if (sumData!=null) System.out.println("VER LISTA data:"+sumData.get("time"));
        else System.out.println("VER LISTA DATA NULL");
        return sumData;
        
    }
    
    
    /**
     * Este metodo obtiene un deporte de la base de datos, con los parametros suministrados
     * @param idSport
     * @param user
     * @return null | objeto Deportes
     */
    public Deportes getSelectedSport(long idSport, Usuarios user) {
        
        // chequeamos parametros
        if (user==null) return null;
        if (idSport<1) return null;
        
        ddao=new DeportesDAO();
        
        Deportes deporte=null;
        try {
            // obtenemos el objeto deporte segun el id
            deporte=ddao.readSport(idSport);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal obteniendo un deporte en estadisticas, para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
        return deporte;
    }


    /**
     * Este metodo obtiene una actividad de la base de datos, con los parametros suministrados
     * @param idActivity
     * @param user
     * @return null | objeto Actividades
     */
    public Actividades getSelectedActivity(long idActivity, Usuarios user) {
        
        // chequeamos parametros
        if (user==null) return null;
        if (idActivity<1) return null;
        
        acdao=new ActividadesDAO();
        
        Actividades actividad=null;
        
        try {
            // obtenemos el objeto actividades segun el id
            actividad=acdao.readActivity(idActivity);
            
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal obteniendo una actividad en estadisticas, para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
        return actividad;
    }
    
    
}
