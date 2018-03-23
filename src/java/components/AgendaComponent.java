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
public class AgendaComponent {
    
    private AgendaDAO agdao;
    private ActividadesDAO adao;
    private DeportesDAO ddao;
    
    private Logger log;
    
    
    public AgendaComponent() {
        
        log=Logger.getLogger("stdout");
        
    }
    
    /**
     * Este método graba en la base de datos un objeto agenda con los datos suministrados
     * por parametro
     * @param agenda
     * @param user
     * @return boolean
     */
    public boolean createEventCalendar (Agenda agenda, Usuarios user) {
        
        // chequeo de parametros
        if (agenda==null) return false;
        if (user==null) return false;
        
        agdao=new AgendaDAO();
        
        try {
            //grabamos en la base de datos el objeto agenda
            boolean result=agdao.createCalendar(agenda);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal grabando un evento en agenda, para el user "+user.getId()+" - mensaje: "+ex);
            return false;
        }
        
    }
    
    
    /**
     * Este método devuelve una actividad correspondiente a los parametros suministrados
     * @param idActivity
     * @param user
     * @return null | Objeto Actividades
     */
    public Actividades readActivity (long idActivity,Usuarios user) {
        
        // chequeo de parametros
        if (idActivity<1) return null;
        if (user==null) return null;        
     
        Actividades activity=null;
        try {
            activity=adao.readActivity(idActivity);
            return activity;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo una actividad en agenda, para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
    }
    
    /**
     * Este metodo obtiene todas las actividades correspondientes a un objeto deporte concreto, segun los
     * parametros suministrados.
     * @param deporte
     * @param user
     * @return null | List Actividades
     */
    public List<Actividades> allActivities (Deportes deporte, Usuarios user) {

        // chequeo de parametros
        if (deporte==null) return null;
        if (user==null) return null;
        
        List<Actividades> activities=null;
        
        try {
            activities=adao.readAllSportActivities(deporte);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal listando actividades en agenda, para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
        return activities;
        
    }
    
    
    
    /**
     * Este metodo recupera el objeto Deportes, en funcion
     * de los parametros suministrados
     * @param idSport
     * @param user
     * @return null | objeto Deportes
     */
    public Deportes readSport (long idSport, Usuarios user) {
        
        // chequeamos parametros
        if (idSport<1) return null;
        if (user==null) return null;
        
        Deportes sport=null;
        try {
            sport=ddao.readSport(idSport);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo un deporte en agenda, para el user "+user.getId()+" - mensaje: "+ex);
        }   
        
        return sport;
    }
}
