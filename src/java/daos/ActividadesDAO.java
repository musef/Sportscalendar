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
package daos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import models.Actividades;
import models.Deportes;

/**
 *
 * @author musef2904@gmail.com
 */
public class ActividadesDAO implements ActividadesInterface{

    EntityManager em;
    EntityTransaction tx;
    
    /**
     * Este metodo graba en DDBB un objeto Actividades, suministrado
     * en el parametro
     * Verifica previamente que cumple las condiciones
     * @param activity
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean createActivity(Actividades activity) {
        
        /* Verificacion de condiciones */
        
        if (activity==null) return false;
        
        if (activity.getName().isEmpty()) return false;
        if (activity.getName().length()>100) return false;
        
        if (activity.getTiming()==null) return false;
        
        if (activity.getSlope()<0) return false;
        if (activity.getDistance()<0) return false;
        if ((activity.getSite()!=null) && (activity.getSite().length()>100)) return false;
        if ((activity.getDescription()!=null) && (activity.getDescription().length()>255)) return false;
        
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            em.persist(activity);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades cr-01");
            em.close();
            return false;
        }
        
        em.close();
        return true;
        
    }

    
    /**
     * Este metodo busca y devuelve un objeto Actividades en DDBB, segun el id
     * suministrado.
     * Verifica previamente que cumple las condiciones
     * @param id
     * @return objeto Actividades || null
     */
    @Override
    public Actividades readActivity(Long id) {
        
        if (id<1) return null;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        Actividades activity;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Actividades.findById");
            q.setParameter("id", id);
            
            activity=(Actividades)q.getSingleResult();
            
            tx.commit();
            
        } catch (NonUniqueResultException nr) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades rd-02A");  
            em.close();
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades rd-02B");            
            em.close();
            return null;
        }
        
        em.close();
        
        return activity;
                
    }

    /**
     * Este metodo actualiza en DDBB un objeto Actividades, segun el
     * objeto suministrado
     * Verifica previamente que cumple las condiciones
     * @param activity
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean updateActivity(Actividades activity) {
        
        /* Verificacion de condiciones */
        
        if (activity==null) return false;
        
        if (activity.getId()==0) return false;
        
        if (activity.getName().isEmpty()) return false;
        if (activity.getName().length()>100) return false;
        
        if (activity.getTiming()==null) return false;
        
        if (activity.getSlope()<0) return false;
        if (activity.getDistance()<0) return false;
        if ((activity.getSite()!=null) && (activity.getSite().length()>100)) return false;
        if ((activity.getDescription()!=null) && (activity.getDescription().length()>255)) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Actividades act=em.merge(activity);
            em.persist(act);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades up-03");
            em.close();
            return false;
        }
        
        System.out.println("TImming on update: "+activity.getTiming());
        
        em.close();
        return true;
        
    }

    
    /**
     * Este metodo borra de la DDBB un objeto Actividades, segun el 
     * id suministrado
     * Verifica previamente que cumple las condiciones
     * Si el objeto actividades ha sido utilizado en la agenda no es posible
     * borrarlo, retornando false
     * @param id
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean deleteActivity(Long id) {
        
        // verificacion de condiciones
        if (id<1) return false;
        
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Actividades act=em.find(Actividades.class, id);
            
            // verificamos si la actividad es usada
            // si es usada no podemos borrarla
            AgendaDAO agdao=new AgendaDAO();
            if (agdao.usedActivity(act)) return false;
            
            em.remove(act);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades dl-04");            
            em.close();
            return false;
        }
        
        em.close();
        return true;
    }
    
    
     /**
     * Este metodo lee todas las actividades correspondientes al Deporte
     * sport y los devuelve en forma de lista
     * @param  sport
     * @return null | List
     */
    public List<Actividades> readAllSportActivities(Deportes sport) {
        
        if (sport==null) return null;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        List<Actividades> activities;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Actividades.findByIdsport");
            q.setParameter("idsport", sport);
            
            activities=(List<Actividades>)q.getResultList();
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Actividades rd-05");
            em.close();
            return null;
        }
        
        em.close();
        return activities;
        
    }
    
}
