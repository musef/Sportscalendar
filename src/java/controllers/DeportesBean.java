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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import daos.DeportesDAO;
import java.util.List;
import models.Deportes;

/**
 *
 * @author floren
 */
@ManagedBean
@RequestScoped
public class DeportesBean {

    private DeportesDAO dao;
    
    private Deportes sport;
    private List<Deportes> sports;
    
    public DeportesBean() {
        
        dao=new DeportesDAO();
        // rellenamos la lista de deportes
        sports=dao.readAllUserSports(LoginBean.user);
      
    }

    /**
     * @return the sport
     */
    public Deportes getSport() {
        return sport;
    }

    /**
     * @param sport the sport to set
     */
    public void setSport(Deportes sport) {
        this.sport = sport;
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
    
    
}
