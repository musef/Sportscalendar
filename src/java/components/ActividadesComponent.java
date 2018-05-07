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
import daos.DeportesDAO;
import java.util.List;
import models.Actividades;
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
public class ActividadesComponent {
    
    private ActividadesDAO adao;
    private DeportesDAO ddao;
    
    private Logger log;
    
    
    public ActividadesComponent () {
        
        adao=new ActividadesDAO();
        ddao=new DeportesDAO();
        
        log=Logger.getLogger("stdout");
        
    }
    
    /**
     * Este método crea una actividad según los parametros suministrados
     * @param activity
     * @param user
     * @return boolean
     */
    public boolean createActivity(Actividades activity, Usuarios user) {

        // chequeo de parametros
        if (activity==null) return false;
        if (user==null) return false;        

        try {
            // instanciamos la grabación de la nueva actividad
            boolean result=adao.createActivity(activity);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal creando una actividad para el user "+user.getId()+" - mensaje: "+ex);
            return false;
        }
        
    }
    
    
    /**
     * Este metodo modifica una actividad segun los parametros suministrados
     * @param activity
     * @param user
     * @return boolean
     */
    public boolean updateActivity(Actividades activity, Usuarios user) {
        
        // chequeo de parametros
        if (activity==null) return false;
        if (user==null) return false;
        
        try {
            // instanciamos la modificacion de la actividad
            boolean result=adao.updateActivity(activity);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal modificando una actividad para el user "+user.getId()+" - mensaje: "+ex);
            return false;
        }
        
    }
    
    /**
     * Este metodo borra una actividad del usuario, segun los parametros suministrados
     * @param idActivity
     * @param user
     * @return int, con el resultado de la operacion (1=ok ; 0=fallo dato; -1=tenia eventos; -99=error sistema)
     */
    public int deleteActivity(long idActivity, Usuarios user) {
        
        // chequeo de parametros
        if (idActivity<1) return 0;
        if (user==null) return 0;
        
        try {
            int result=adao.deleteActivity(idActivity);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal borrando una actividad para el user "+user.getId()+" - mensaje: "+ex);
            return -99;
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
            log.error("ERROR: Algo ha ido mal listando actividades para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
        return activities;
        
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
            log.error("ERROR: Algo ha ido mal leyendo una actividad para el user "+user.getId()+" - mensaje: "+ex);
            return null;
        }
        
    }
    
}
