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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author musef2904@gmail.com
 */
public class UsuariosDAO implements UsuariosInterface {

    
    private final int MIN_LENGTH_USERNAME=6;
    private final int MAX_LENGTH_USERNAME=100;
    private final int MIN_LENGTH_USERPASS=8;
    private final int MAX_LENGTH_USERPASS=255;

    private Logger log;
    
    public UsuariosDAO() {
        
        log=Logger.getLogger("stdout");
    }
    
    /**
     * Funcion que crea un usuario en DDBB.
     * Verifica que el usuario cumpla las condiciones para ser creado.
     * @param user
     * @return boolean con el resultado de la operacion
     * @throws java.lang.Exception
     */    
    @Override
    public boolean createUser(Usuarios user) throws Exception {

        /* Verificacion de condiciones */
        
        if (user==null) return false;
        
        // por seguridad el username no debe ser inferior a 6 chars.
        String username=user.getLogin();
        if (username==null || username.length()<MIN_LENGTH_USERNAME || username.length()>MAX_LENGTH_USERNAME) return false;
        
        // por seguridad la contraseña no debe ser inferior a 8 chars.
        String userpass=user.getPassword();
        if (userpass==null || userpass.length()<MIN_LENGTH_USERPASS || userpass.length()>MAX_LENGTH_USERPASS) return false;
        
        // el nombre de usuario debe contener al menos 2 chars
        String name=user.getNameuser();
        if (name==null || name.length()<2 || name.length()>100) return false;
        
        // por seguridad keyuser no debe ser inferior a 15 chars
        String key=user.getKeyuser();
        if (key==null || key.length()<15 || key.length()>255) return false;

        // el email de usuario debe contener al menos 8 chars
        String email=user.getEmail();
        if (email==null || email.length()<8 || email.length()>100) return false;
                
        
        /* Proceso de datos */   
      
        EntityManager em=Factory.getEmf().createEntityManager();
        
        EntityTransaction tx=null;
        
        try {
            tx=em.getTransaction();
            tx.begin();

            em.persist(user);

            tx.commit();                    
        } catch (Exception ex) {
            // problemas
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Usuarios cr-01 creando->user "+user.getNameuser()+" - Mensaje: "+ex);             
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Usuario creado ->user: "+user.getNameuser()); 
        return true;
    }

    
    /**
     * Este metodo devuelve el objeto Usuarios de la DDBB, correspondiente
     * al id suministrado
     * @param id
     * @return Usuarios || null
     * @throws java.lang.Exception
     */
    @Override
    public Usuarios readUser(Long id) throws Exception {
       
        /* Verificacion de condiciones */
        if (id<1) return null;
        
        // creamos los objetos de transaccion
        EntityManager em=Factory.getEmf().createEntityManager();       
        EntityTransaction tx=em.getTransaction();
        
        // objeto a devolver
        Usuarios us=null;
        
        // iniciamos la transaccion
        try {
            
            tx.begin();
            // attaching el objeto
            us=em.find(Usuarios.class, id);        
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Usuarios rd-02 leyendo->id "+id+" - Mensaje: "+ex);                        
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
         
        return us;
    
    }

    
    /**
     * Funcion que actualiza un usuario en DDBB.
     * Verifica que el usuario cumpla las condiciones para ser actualizado
     * @param user
     * @return boolean con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean updateUser(Usuarios user) throws Exception {
        
        /* Verificacion de condiciones */
        
        if (user==null) return false;
        
        // por seguridad, el id debe existir
        Long id=user.getId();
        if (id==null || id<1) return false;        
        
        // por seguridad el username no debe ser inferior a 6 chars.
        String username=user.getLogin();
        if (username==null || username.length()<MIN_LENGTH_USERNAME || username.length()>MAX_LENGTH_USERNAME) return false;
        
        // por seguridad la contraseña no debe ser inferior a 8 chars.
        String userpass=user.getPassword();
        if (userpass==null || userpass.length()<MIN_LENGTH_USERPASS || userpass.length()>MAX_LENGTH_USERPASS) return false;
        
        // el nombre de usuario debe contener al menos 2 chars
        String name=user.getNameuser();
        if (name==null || name.length()<2 || name.length()>100) return false;
        
        // por seguridad keyuser no debe ser inferior a 15 chars
        String key=user.getKeyuser();
        if (key==null || key.length()<15 || key.length()>255) return false;

        // el email de usuario debe contener al menos 8 chars
        String email=user.getEmail();
        if (email==null || email.length()<8 || email.length()>100) return false;
        
        /* Proceso de datos */   
      
        EntityManager em=Factory.getEmf().createEntityManager();
        
        EntityTransaction tx=null;
        
        try {
            
            tx=em.getTransaction();
            tx.begin();
            
            Usuarios us=em.merge(user);
            em.persist(us);
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("Error Usuarios up-03 modificando->user "+user.getNameuser()+" - Mensaje: "+ex);             
            return false;          
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Usuario modificado con id "+user.getId()+" ->user: "+user.getNameuser()); 
        return true;
    }

    
    /**
     * Este metodo borra el Usuario de la BBDD, correspondiente al 
     * id suministrado
     * @param id
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean deleteUser(Long id) throws Exception {
        
        /* Verificacion de condiciones */
        if (id<1) return false;
        
        // creamos los objetos de transaccion
        EntityManager em=Factory.getEmf().createEntityManager();
        
        EntityTransaction tx=em.getTransaction();
        
        Usuarios us;
        // iniciamos la transaccion
        try {
            
            tx.begin();
            // attaching el objeto
            us=em.find(Usuarios.class, id);
            em.remove(us);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Usuarios dl-04 borrando->id "+id+" - Mensaje: "+ex);                      
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Usuario borrado con id "+id+" ->user: "+us.getNameuser()); 
        return true;
    }
    
    
    /**
     * Este metodo obtiene un objeto Usuarios en la DDBB, si coincide con
     * el username and userpass suministrado, y lo devuelve.
     * Verifica que los parametros son adecuados
     * @param username
     * @param userpass
     * @return objeto Usuarios - retorna el usuario si correcto; 
     *                  getId == -1 si incorrecto o 
     *                  getId == 0 si no existe o 
     *                  null si fails
     * @throws java.lang.Exception
     */
    public Usuarios identifyUser (String username, String userpass) throws Exception {
        
        // objecto a devolver
        Usuarios us=null;
        
        // verificacion de parametros
        if (username==null || username.length()<MIN_LENGTH_USERNAME || username.length()>MAX_LENGTH_USERNAME ||
            userpass==null || userpass.length()<MIN_LENGTH_USERPASS || userpass.length()>MAX_LENGTH_USERPASS) {
        
            us=new Usuarios((long)0);
            return us; 
            
        }
        
        /* proceso de datos */
        
        EntityManager em=Factory.getEmf().createEntityManager();
        
        EntityTransaction tx=em.getTransaction();
        
        Query q=em.createNamedQuery("Usuarios.findByLoginPassword");
                         
        
        try {
            tx.begin();
            
            q.setParameter("usn", username);
            q.setParameter("pss", userpass);
            us=(Usuarios) q.getSingleResult();
                       
            tx.commit();
            
        } catch (NoResultException nr) {
            us=new Usuarios((long)-1);
            // logger
            log.info("Error Usuarios rd-05A identificando (inexistente)->username "+username+" - Mensaje: "+nr);             
            return us;   
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("Error Usuarios rd-05B identificando->username "+username+" - Mensaje: "+ex);             
            return null;   
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Usuario identificado username "+username);       
        return us;
    }
    
}
