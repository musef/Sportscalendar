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

import org.apache.log4j.Logger;

/**
 *
 * @author floren
 */
public class LibraryClass {
    
    // longitudes parametros en identificacion
    public static final int MIN_LENGTH_USERNAME=6;
    public static final int MAX_LENGTH_USERNAME=15;
    public static final int MIN_LENGTH_USERPASS=8;
    public static final int MAX_LENGTH_USERPASS=20;
    
    // caracteres prohibidos en login
    private final CharSequence forbiddenCharsInLogin="<>!$%&/()=?;[]\"'\\";
    // caracteres prohibidos en formularios
    private final CharSequence forbiddenCharsInForms="<>&@#%\\";
    
    // log
    private Logger log;
    
    /**
     * Este método sanitiza el parametro data recibido, y lo devuelve sanitizado
     * @param data
     * @return 
     */
    public String verifyLoginInput(String data) {
          
        // copia para comparacion posterior
        String copydata=data;
        
        // caracteres prohibidos
        data=data.replace(forbiddenCharsInLogin, " ");
        // secuencias prohibidas
        data=data.replaceAll("script", "");
        data=data.replaceAll("document", "");
        data=data.replaceAll("value", "");
        data=data.trim();
        
        if (!copydata.equals(data)) {
            log.warn("ADVERTENCIA: El dato "+copydata+" ha sido filtrado y por motivos de seguridad transformado en "+data);
        }

        return data;        
        
    }

    /**
     * Este método sanitiza el parametro data recibido, y lo devuelve sanitizado
     * @param data
     * @return 
     */
    public String verifyFormsInput(String data) {
          
        // copia para comparacion posterior
        String copydata=data;
        
        // caracteres prohibidos
        data=data.replace(forbiddenCharsInForms, "");
        // secuencias prohibidas
        data=data.replaceAll("script", "");
        data=data.replaceAll("document", "");
        data=data.replaceAll("value", "");
        data=data.replaceAll("ById", "");
        data=data.trim();
        
        if (!copydata.equals(data)) {
            log.warn("ADVERTENCIA: El dato "+copydata+" ha sido filtrado y por motivos de seguridad transformado en "+data);
        }
        
        return data;        
        
    }

    
}
