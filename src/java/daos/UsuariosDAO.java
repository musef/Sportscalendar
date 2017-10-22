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
import models.Usuarios;

/**
 *
 * @author musef2904@gmail.com
 */
public class UsuariosDAO implements UsuariosInterface {

    /**
     * Funcion que crea un usuario en DDBB.
     * Verifica que el usuario cumpla las condiciones para ser creado.
     * @param user
     * @return boolean con el resultado de la operacion
     */
    @Override
    public boolean createUser(Usuarios user) {

        /* Verificacion de condiciones */
        
        if (user==null) return false;
        
        // por seguridad el username no debe ser inferior a 6 chars.
        String username=user.getLogin();
        if (username==null || username.length()<6 || username.length()>50) return false;
        
        // por seguridad la contraseña no debe ser inferior a 8 chars.
        String userpass=user.getPassword();
        if (userpass==null || userpass.length()<8 || userpass.length()>255) return false;
        
        // el nombre de usuario debe contener al menos 2 chars
        String name=user.getNameuser();
        if (name==null || name.length()<2 || name.length()>100) return false;
        
        // por seguridad keyuser no debe ser inferior a 15 chars
        String key=user.getKeyuser();
        if (key==null || key.length()<15 || key.length()>50) return false;

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
            System.err.println("Error Usuarios cr-01");
            em.close();
            return false;
        }
        // grabacion exitosa
        em.close();
        return true;
    }

    @Override
    public Usuarios readUser(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Funcion que actualiza un usuario en DDBB.
     * Verifica que el usuario cumpla las condiciones para ser actualizado
     * @param user
     * @return boolean con el resultado de la operacion
     */
    @Override
    public boolean updateUser(Usuarios user) {
        
        /* Verificacion de condiciones */
        
        if (user==null) return false;
        
        // por seguridad, el id debe existir
        Long id=user.getId();
        if (id==null || id<1) return false;        
        
        // por seguridad el username no debe ser inferior a 6 chars.
        String username=user.getLogin();
        if (username==null || username.length()<6 || username.length()>50) return false;
        
        // por seguridad la contraseña no debe ser inferior a 8 chars.
        String userpass=user.getPassword();
        if (userpass==null || userpass.length()<8 || userpass.length()>255) return false;
        
        // el nombre de usuario debe contener al menos 2 chars
        String name=user.getNameuser();
        if (name==null || name.length()<2 || name.length()>100) return false;
        
        // por seguridad keyuser no debe ser inferior a 15 chars
        String key=user.getKeyuser();
        if (key==null || key.length()<15 || key.length()>50) return false;

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
            
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            System.err.println("Error Usuarios up-01");
            em.close();
            return false;            
        }

        // grabacion exitosa
        em.close();
        return true;
    }

    
    @Override
    public boolean deleteUser(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
