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

import daos.UsuariosDAO;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author musef2904@gmail.com
 */
public class LoginComponent {
    

    private String loginmessage="";
    
    private Logger log;
    
    
    public LoginComponent() {

        log = Logger.getLogger("stdout");
    }
   
    
    

    
    
    /**
     * Este metodo comprueba si el usuario esta registrado; si esta registrado devuelve
     * el objeto usuario, en caso contrario null
     * @param username
     * @param userpass
     * @return null | objeto Usuarios
     */
    public Usuarios checkUserLogin(String username, String userpass) {
        
        Usuarios user=null;
        
        UsuariosDAO udao=new UsuariosDAO();
        
        // convertimos la contraseña suministrada a hash para comparar
        String passwhashed=securizePassword(userpass);        
        try {
            // comprobamos la identificacion, retornando un objeto user
            user=udao.identifyUser(username, passwhashed);
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal identificando un usuario con username "+username+" - mensaje: "+ex);
        }

                // problemas ddbb en logueado
        if (user==null || user.getId()==-1) this.loginmessage="Error procesando su solicitud";
        // error en user-pass
        else if (user.getId()==0) this.loginmessage="Usuario-contraseña inexistente";
  
            
        // login correcto
        if (user !=null && user.getId()>0) return user;
        
        return null;
    }
    
    
    /**
     * Este metodo convierte una contraseña dada en una contraseña segura de 128bits
     * codificada mediante PBKDF y una salt de 64bytes.
     * @param password
     * @return 
     */
    private String securizePassword(String password) {
        
        // obtenemos la salt
        byte[] salt=null;
        try {
            salt=getSalt();
        } catch (NoSuchAlgorithmException ex) {
            //return null;
        }
        
        // convertimos la contraseña en un array de chars
        char[] pass=password.toCharArray();
        
        // obtenemos el array de bytes (signed)
        byte[]hashedpass=hashPassword(pass, salt);
        
        // convertimos a un string de hexadecimales el array
        String ret=convertToHex(hashedpass);
        
        // devolvemos la contraseña
        return ret;
    }
    
    
    /**
     * Este metodo (sugerido OWASP) hace un hash de la password suministrada, para almacenamiento
     * seguro.
     * @param password
     * @param salt
     * @return 
     */
    private byte[] hashPassword( final char[] password, final byte[] salt ) {
        
        int keyLength=512;
        int iterations = 10;
        try {
            //SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;

        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
    
    
    /**
     * Este metodo genera una salt segura de 64 bytes
     * Por cuestiones de implementación demo, la salt se hace fija
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private byte[] getSalt() throws NoSuchAlgorithmException  {
        //SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //byte[] salt = new byte[64];
        //sr.nextBytes(salt);
        byte[] salt = {1,2,3,127,-15,8,3,0,-21,-23,-98,6,116,111,0,-65,-45,-4,-98,12,77,23,23,-15,
        8,56,-9,-119,63,61,44,-111,-7,-56,-47,17,3,9,99,119,56,-49,-16,-8,11,13,11,-89,
        -100,-125,14,79,45,54,12,-78,-31,31,66,-99,-14,0,-1,4};
        
        return salt;
    }
    
    
    /**
     * Este metodo retorna un string de hexadecimales, del array de bytes
     * seguro del correspondiente hash realizado sobre la contraseña.
     * El String mide 128 chars.
     * @param pass
     * @return 
     */
    private String convertToHex(byte[] pass) {
        
        String st="";
        String valueconvert="00";
        
        for (int n=0;n<pass.length;n++) {
            // convertimos a string y hacemos unsigned el valor byte
            valueconvert=Integer.toHexString(pass[n]+128);
            // si el valor es menor de dos caracteres, añadimos 0
            if (valueconvert.length()<2) st+="0"+valueconvert;
            else st+=valueconvert;
        }
        // retornamos el String que tiene que medir 128
        return st;
    }
    
    
 
    
}
