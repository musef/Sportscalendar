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
import org.apache.log4j.Logger;

/**
 *
 * @author musef2904@gmail.com
 */
public class ActividadesDAO implements ActividadesInterface {

    private EntityManager em;
    private EntityTransaction tx;
    
    private Logger log;
    
    
    public ActividadesDAO() {
        
        log=Logger.getLogger("stdout");
        
    }
    
    /**
     * Este metodo graba en DDBB un objeto Actividades, suministrado
     * en el parametro
     * Verifica previamente que cumple las condiciones
     * @param activity
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean createActivity(Actividades activity) throws Exception {
        
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
            // logger
            log.error("ERROR: Actividades cr-01 creando->user "+activity.getIduser()+" - Mensaje: "+ex);            
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Creada actividad "+activity.getName()+" ->user"+activity.getIduser());        
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
    public Actividades readActivity(Long id) throws Exception {
        
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
            // logger
            log.error("ERROR: Actividades rd-02A leyendo->id "+id+" - Mensaje: "+nr);             
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Actividades rd-02B leyendo->id "+id+" - Mensaje: "+ex);
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
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
    public boolean updateActivity(Actividades activity) throws Exception {
        
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
            log.error("ERROR: Actividades up-03 updating->user "+activity.getIduser()+" - Mensaje: "+ex);         
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }

        // logger
        log.info("Modificada actividad "+activity.getName()+" ->user"+activity.getIduser());         
        return true;
        
    }

    
    /**
     * Este metodo borra de la DDBB un objeto Actividades, segun el 
     * id suministrado
     * Verifica previamente que cumple las condiciones
     * Si el objeto actividades ha sido utilizado en la agenda no es posible
     * borrarlo, retornando false
     * @param id
     * @return int, con el resultado de la operacion (1=ok ; 0=fallo dato; -1=tenia eventos; -99=error sistema)
     */
    @Override
    public int deleteActivity(Long id) throws Exception {
        
        // verificacion de condiciones
        if (id<1) return 0;
        
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        Actividades act;
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            act=em.find(Actividades.class, id);
            
            // comprobamos si tiene eventos este deporte
            if (!checkIfActivitiesExist(act)) {   
                // no tiene, procedemos a borrarlo
                em.remove(act);          
                tx.commit();
            } else {
                // no se borra porque tiene eventos en la agenda
                log.info("NO Borrada actividad id "+id+" porque tenía eventos en agenda ->user"+act.getIduser());                
                return -1;
            }            
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            log.error("ERROR: Actividades dl-04 borrando->id "+id+" - Mensaje: "+ex);                  
            return -99;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Borrada actividad id "+id+" ->user"+act.getIduser());         
        return 1;
    }
    
    
     /**
     * Este metodo lee todas las actividades correspondientes al Deporte
     * sport y los devuelve en forma de lista
     * @param  sport
     * @return null | List
     * @throws java.lang.Exception
     */
    public List<Actividades> readAllSportActivities(Deportes sport) throws Exception {
        
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
            log.error("ERROR: Actividades rd-05 leyendo todos->sport "+sport.getId()+" - Mensaje: "+ex);            
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return activities;
        
    }
    
    
    /**
     * Este metodo comprueba si la actividad tiene eventos en la agenda, y si los tiene
     * devuelve un true (existe evento)
     * @param sport
     * @return boolean
     */
    private boolean checkIfActivitiesExist(Actividades activity) {
        
        // creamos los objetos de transaccion
        EntityManager em2=Factory.getEmf().createEntityManager();
        EntityTransaction tx2=em2.getTransaction();
        
        // iniciamos la transaccion
        long result=0;
        try {
            tx2.begin();
            Query q=em2.createNamedQuery("Agenda.findByActivity");
            q.setParameter("iduser", activity.getIduser());
            q.setParameter("idactivity", activity);
            result=(long) q.getResultList().get(0);
            tx2.commit();
            
        } catch (Exception ex) {
            if (tx2!=null && tx2.isActive()) tx2.rollback();
            // logger
            log.error("ERROR: Actividades rd-06 leyendo eventos->idsport"+activity.getId()+" - Mensaje: "+ex);                      
            return true;
        } finally {
            if (tx2!=null && tx2.isActive()) em2.close();
        }
        
        if (result<1) return false;
        else return true;
        
    }
    
    
}
