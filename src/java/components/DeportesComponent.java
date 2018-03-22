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

import controllers.LoginBean;
import daos.DeportesDAO;
import java.util.List;
import java.util.logging.Level;
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
public class DeportesComponent {
    
    private DeportesDAO ddao;
    
    private Logger log;
    
    
    public DeportesComponent() {
        
        ddao=new DeportesDAO();
        
        log=Logger.getLogger("stdout");
        
    }
    
    
    /**
     * Este metodo elimina un objeto Deportes, en funcion
     * del indice del select sportidx.
     * @param idSport
     * @param user
     * @return boolean
     */
    public boolean deleteSport(long idSport, Usuarios user) {
        
        // chequeamos parametros
        if (idSport<1) return false;
        if (user==null) return false;
        
        try {
            // borramos en base de datos
            boolean result=ddao.deleteSport(idSport);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal borrando un deporte para el user "+user.getId()+" - mensaje: "+ex);
            return false;
        }
    }
    
    
    /**
     * Este metodo obtiene la lista de los deportes del usuario suministrado por parametro
     * @param user
     * @return null | List Deportes
     */
    public List<Deportes> allSports(Usuarios user) {
        
        // chequeamos parametros
        if (user==null) return null;
        
        List<Deportes> sportsList=null;
        
        try {
            sportsList=ddao.readAllUserSports(user);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal leyendo la lista de deportes para el user "+user.getId()+" - mensaje: "+ex);
        }
                
        return sportsList;
        
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
            log.error("ERROR: Algo ha ido mal leyendo un deporte para el user "+user.getId()+" - mensaje: "+ex);
        }   
        
        return sport;
    }
    
    
    /**
     * Este metodo crea un deporte con los datos del objeto Deporte suministrado
     * @param deporte
     * @return boolean
     */
    public boolean createSport (Deportes deporte) {

        // chequeamos parametros
        if (deporte==null) return false;      
        
        try {
            boolean result=ddao.createSport(deporte);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal grabando un deporte para el user "+deporte.getIduser()+" - mensaje: "+ex);
            return false;
        }
        
    }
    
    /**
     * Este metodo modifica un deporte con los datos del objeto Deporte suministrado
     * @param deporte
     * @return boolean
     */
    public boolean updateSport (Deportes deporte) {

        // chequeamos parametros
        if (deporte==null) return false;      
        
        try {
            boolean result=ddao.updateSport(deporte);
            return result;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal modificando un deporte para el user "+deporte.getIduser()+" - mensaje: "+ex);
            return false;
        }                
        
    }
    
}
