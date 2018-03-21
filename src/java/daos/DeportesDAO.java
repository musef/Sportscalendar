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
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author musef2904@gmail.com
 */

public class DeportesDAO implements DeportesInterface {

    private EntityManager em;
    private EntityTransaction tx;
    
    private Logger log;
    
    
    public DeportesDAO() {
        log=Logger.getLogger("stdout");
    }
    

    /**
     * Este metodo crea un nuevo Deporte en la DDBB, con el objeto
     * Deportes suministrado
     * @param sport
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */    
    @Override
    public boolean createSport(Deportes sport) throws Exception {
        
        /* Verificacion de condiciones */
        if (sport==null) return false;

        if (sport.getSportName().isEmpty()) return false;
        if (sport.getSportName().length()>50) return false;
        
        if ((sport.getSportDescrip()!=null) && (sport.getSportDescrip().length()>255)) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            em.persist(sport);
            tx.commit();
            
        } catch (Exception ex) {
            // rollback
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Deportes cr-01 ->user"+sport.getIduser()+" - Mensaje: "+ex);
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        // logger
        log.info("Creado deporte id"+sport.getId()+" ->user"+sport.getIduser());
        return true;
        
    }

    /**
     * Este metodo devuelve el objeto Deportes correspondiente al id
     * @param id
     * @return Object || null
     * @throws java.lang.Exception
     */
    @Override
    public Deportes readSport(Long id) throws Exception {
        
        if (id<1) return null;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        Deportes sport;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Deportes.findById");
            q.setParameter("id", id);
            
            sport=(Deportes)q.getSingleResult();
            
            tx.commit();
            
        } catch (NonUniqueResultException nr) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Deportes rd-02A leyendo->id "+id+" - Mensaje: "+nr);            
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            log.error("ERROR: Deportes rd-02B leyendo->id "+id+" - Mensaje: "+ex);                       
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return sport;
        
    }

    
    /**
     * Este metodo actualiza el objeto Deportes en DDBB, con
     * el objeto suministrado
     * @param sport
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean updateSport(Deportes sport) throws Exception{
        
                /* Verificacion de condiciones */
        if (sport==null) return false;
        
        if (sport.getSportName().isEmpty()) return false;
        if (sport.getSportName().length()>50) return false;
        
        if ((sport.getSportDescrip()!=null) && (sport.getSportDescrip().length()>50)) return false;
        
        //creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Deportes sportattached=em.merge(sport);
            em.persist(sportattached);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            log.error("ERROR: Deportes up-03 updating->id "+sport.getId()+" - Mensaje: "+ex);                        
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        // logger
        log.info("Modificado deporte id"+sport.getId()+" ->user"+sport.getIduser());
        return true;
    }

    
    /**
     * Este metodo borra el objeto Deportes de la DDBB, correspondiente
     * al id suministrado
     * @param id
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean deleteSport(Long id) throws Exception{

        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        Deportes sport;
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            sport=em.find(Deportes.class, id);
            em.remove(sport);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Deportes dl-04 borrando->id"+id+" - Mensaje: "+ex);                      
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();
        }
        
        // logger
        log.info("Borrado deporte id "+id+" ->user"+sport.getIduser());
        return true;
    }
    
    
    /**
     * Este metodo lee todos los deportes correspondientes al usuario user
     * y los devuelve en forma de lista
     * @param  user
     * @return null | List
     * @throws java.lang.Exception
     */
    public List<Deportes> readAllUserSports(Usuarios user) throws Exception{
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        List<Deportes> sport;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Deportes.findByIduser");
            q.setParameter("iduser", user);
            
            sport=(List<Deportes>)q.getResultList();
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            //logger
            log.error("ERROR: Deportes rd-05 leyendo all->user "+user.getId()+" - Mensaje: "+ex);             
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return sport;
        
    }
    
}