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
    
    @Override
    public boolean createSport(Deportes sport) {
        
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
            em.close();
            return false;
        }
        
        em.close();
        return true;
        
    }

    @Override
    public Deportes readSport(Long id) {
        
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
            em.close();
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            em.close();
            return null;
        }
        
        em.close();
        return sport;
        
    }

    @Override
    public boolean updateSport(Deportes sport) {
        
// creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            
            //Deportes sportmerge=em.find(Deportes.class, sport.getId());
            em.merge(sport);
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            em.close();
            return false;
        }
        
        em.close();
        return true;
    }

    @Override
    public boolean deleteSport(Long id) {

        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            
            Deportes sport=em.find(Deportes.class, id);
            em.remove(sport);
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            em.close();
            return false;
        }
        
        em.close();
        return true;
    }
    
    
    /**
     * Este metodo lee todos los deportes correspondientes al usuario con iduser,
     * y los devuelve en forma de lista
     * @param iduser
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
            System.out.println("ERROR: "+ex.toString());
            em.close();
            return null;
        }
        
        em.close();
        return sport;
        
    }
    
    
    
}
