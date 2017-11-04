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

/**
 *
 * @author musef2904@gmail.com
 */

public class DeportesDAO implements DeportesInterface {

    EntityManager em;
    EntityTransaction tx;

    /**
     * Este metodo crea un nuevo Deporte en la DDBB, con el objeto
     * Deportes suministrado
     * @param sport
     * @return boolean, con el resultado de la operacion
     */    
    @Override
    public boolean createSport(Deportes sport) {
        
        /* Verificacion de condiciones */
        if (sport==null) return false;
        
        if (sport.getSportName().isEmpty()) return false;
        if (sport.getSportName().length()>50) return false;
        
        if ((sport.getSportDescrip()!=null) && (sport.getSportDescrip().length()>50)) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            em.persist(sport);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Deportes cr-01");
            em.close();
            return false;
        }
        
        em.close();
        return true;
        
    }

    /**
     * Este metodo devuelve el objeto Deportes correspondiente al id
     * @param id
     * @return Object || null
     */
    @Override
    public Deportes readSport(Long id) {
        
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
            System.err.println("ERROR: Deportes rd-02A"); 
            em.close();
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Deportes rd-02B");            
            em.close();
            return null;
        }
        
        em.close();
        return sport;
        
    }

    
    /**
     * Este metodo actualiza el objeto Deportes en DDBB, con
     * el objeto suministrado
     * @param sport
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean updateSport(Deportes sport) {
        
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
            System.err.println("ERROR: Deportes up-03");            
            em.close();
            return false;
        }
        
        em.close();
        return true;
    }

    /**
     * Este metodo borra el objeto Deportes de la DDBB, correspondiente
     * al id suministrado
     * @param id
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean deleteSport(Long id) {

        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Deportes sport=em.find(Deportes.class, id);
            em.remove(sport);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Deportes dl-04");            
            em.close();
            return false;
        }
        
        em.close();
        return true;
    }
    
    
    /**
     * Este metodo lee todos los deportes correspondientes al usuario user
     * y los devuelve en forma de lista
     * @param  user
     * @return null | List
     */
    public List<Deportes> readAllUserSports(Usuarios user) {
        
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
            System.err.println("ERROR: Deportes rd-05");
            em.close();
            return null;
        }
        
        em.close();
        return sport;
        
    }
    
}