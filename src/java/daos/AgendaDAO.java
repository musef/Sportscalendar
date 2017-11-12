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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import models.Actividades;
import models.Agenda;
import models.Deportes;
import models.Usuarios;

/**
 *
 * @author musef2904@gmail.com
 */
public class AgendaDAO implements AgendaInterface{

    EntityManager em;
    EntityTransaction tx;
    
    /**
     * Este metodo graba en DDBB un objeto Agenda, con el parametro
     * suministrado
     * Verifica previamente que cumple las condiciones
     * @param calendar
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean createCalendar(Agenda calendar) {
      
        /* Verificacion de condiciones */
        if (calendar==null) return false;      

        if (calendar.getDate()==null) return false;
        
        if (calendar.getSlope()!=null && calendar.getSlope()<0) return false;
        if (calendar.getDistance()!=null && calendar.getDistance()<0) return false;        
        if (calendar.getTiming()==null) return false;
        if ((calendar.getComments()!=null) && (calendar.getComments().length()>255)) return false;
     
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            em.persist(calendar);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda cr-01");
            em.close();
            return false;
        }
        
        em.close();
        return true;        
        
    }

    
    /**
     * Este metodo devuelve un objeto Agenda, dado el id suministrado
     * Verifica previamente que cumple las condiciones
     * @param id
     * @return objeto Agenda || null
     */
    @Override
    public Agenda readCalendar(Long id) {
    
        /* Verificacion de condiciones */
        if (id<1) return null;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        Agenda calendar;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Agenda.findById");
            q.setParameter("id", id);
            
            calendar=(Agenda)q.getSingleResult();
            
            tx.commit();
            
        } catch (NonUniqueResultException nr) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda rd-02A");  
            em.close();
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda rd-02B");            
            em.close();
            return null;
        }
        
        em.close();
        
        return calendar;
        
    }

    /**
     * Este metodo modifica en DDBB un objeto Agenda, con el objeto
     * Agenda suministrado
     * Verifica previamente que cumple las condiciones
     * @param calendar
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean updateCalendar(Agenda calendar) {
        
        /* Verificacion de condiciones */
        if (calendar==null) return false;      

        if (calendar.getDate()==null) return false;
        
        if (calendar.getSlope()!=null && calendar.getSlope()<0) return false;
        if (calendar.getDistance()!=null && calendar.getDistance()<0) return false;        
        if (calendar.getTiming()==null) return false;
        if ((calendar.getComments()!=null) && (calendar.getComments().length()>255)) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Agenda cal=em.merge(calendar);
            em.persist(cal);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda up-03");
            em.close();
            return false;
        }
        
        em.close();
        return true;        
        
    }

    
    /**
     * Este metodo borra de DDBB un objeto Agenda, con el parametro
     * id suministrado
     * Verifica previamente que cumple las condiciones
     * @param id
     * @return boolean, con el resultado de la operacion
     */
    @Override
    public boolean deleteCalendar(Long id) {
       
        /* Verificacion de condiciones */
        if (id<1) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            Agenda cal=em.find(Agenda.class, id);
            em.remove(cal);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda dl-04");            
            em.close();
            return false;
        }
        
        em.close();
        return true;
        
    }
    
    

    
    public List<Agenda> listCalendar(Usuarios user, Deportes sport, Actividades activity, String fechini, String fechfin) {
    
        /* Verificacion de condiciones */
        if (user==null) return null;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        List<Agenda> calendar;
        
        Date fec1=null;
        Date fec2=null;
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createQuery("SELECT ag FROM Agenda ag WHERE ag.iduser= :iduser AND ag.idsport = :idsport AND ag.idactivity=:idactivity AND ag.date>=:fec1 AND ag.date<= :fec2");
            //Query q=em.createQuery("SELECT ag FROM Agenda ag WHERE ag.iduser= :iduser AND ag.idsport = :idsport AND ag.idactivity=:idactivity ");
            q.setParameter("iduser", user);
            q.setParameter("idsport", sport);
            q.setParameter("idactivity", activity);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
            fec1=sd.parse(fechini.substring(6)+fechini.substring(2,6)+fechini.substring(0,2));
            fec2=sd.parse(fechfin.substring(6)+fechfin.substring(2,6)+fechfin.substring(0,2));
            q.setParameter("fec1", fec1);
            q.setParameter("fec2", fec2);
            
            calendar=(List<Agenda>)q.getResultList();
            
            tx.commit();
            
        } catch (ParseException ps) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda rd-05A"+fec1+"//"+fec2);  
            em.close();
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("ERROR: Agenda rd-05B");            
            em.close();
            return null;
        }
        
        em.close();
        
        return calendar;
        
    }
    
    
}
