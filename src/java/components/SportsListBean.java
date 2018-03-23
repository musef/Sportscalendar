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


import daos.DeportesDAO;
import java.util.List;
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
public class SportsListBean {
    
    private List<Deportes> sportsList;
    private DeportesDAO ddao;
    
    private Logger log;
    
    public SportsListBean(Usuarios user) {
        
        // si la lista esta nula (la primera vez que se instancia)
        if (sportsList==null) {
            // generamos el contenido del listado actualizado
            ddao=new DeportesDAO();
            try {
                sportsList=ddao.readAllUserSports(user);
            } catch (Exception ex) {
                log=Logger.getLogger("stdout");
                log.error("ERROR: Algo ha ido mal obteniendo la lista de deportes para el user "+user.getId()+" - mensaje: "+ex);
            }
        }        
    }

    /**
     * @return the sportsList
     */
    public List<Deportes> getSportsList() {
        return sportsList;
    }

    /**
     * @param aSportsList the sportsList to set
     */
    public void setSportsList(List<Deportes> aSportsList) {
        sportsList = aSportsList;
    }
    
    
    
}
