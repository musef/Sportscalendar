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
import models.Deportes;
import models.Usuarios;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * musef2904@gmail.com
 */
public class DeportesDAOTest {
    
    // usuario para pruebas
    UsuariosDAO udao;
    Usuarios testuser;
    Long idusertest=(long)2;    // usuario existente usado para pruebas
    
    // deportes para test
    Deportes sport;
    Deportes sport1;
    DeportesDAO instance;
    Long idsporttest=(long)10;  // deporte de pruebas creado
    Long noexistingidsporttest=(long)9999999;   // id inexistente (buscar en DDBB)
       

    
    
    
    public DeportesDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        
        // usuario para pruebas
        udao=new UsuariosDAO();
        testuser=udao.readUser(idusertest);

        sport = null;
        instance = new DeportesDAO();
        
        sport1 = new Deportes("test", "test", "test", testuser);
        sport1.setId(idsporttest);
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createSport method, of class DeportesDAO.
     */
    @Test
    public void testCreateSport() throws Exception {

        System.out.println("Iniciando test createSport");
        // test crear un null en DDBB deportes
        assertFalse("FALLO: se ha creado un deporte con null", instance.createSport(null));
        // test crear un deporte vacio en DDBB deportes
        sport=new Deportes("", "", "", null);
        assertFalse("FALLO: se ha creado un deporte empty", instance.createSport(sport));
        // test crear un deporte con usuario null en DDBB deportes
        sport=new Deportes("ZZZZZZ", "DDDDDD", "XXXXXX", null);
        assertFalse("FALLO: se ha creado un deporte sin usuario", instance.createSport(sport)); 
        // test crear un deporte con usuario y contenido empty en DDBB deportes
        sport=new Deportes("", "", "", testuser);
        assertFalse("FALLO: se ha creado un deporte vacio con usuario", instance.createSport(sport));         
        // test crear un deporte todo correcto en DDBB deportes - ESTE GRABA !!
        sport=new Deportes("test", "test", "test", testuser);
       // assertTrue("FALLO: NO se ha creado un deporte con datos correctos", instance.createSport(sport));         
        System.out.println("Fin de test createSport");        
    }

    /**
     * Test of readSport method, of class DeportesDAO.
     */
    @Test
    public void testReadSport() throws Exception {
                
        System.out.println("Iniciando test readSport");
        // test leer un id cero en DDBB deportes
        assertNull("FALLO: se ha leido un deporte con id 0", instance.readSport((long)0));
        // test leer un id inexistente en DDBB deportes
        assertNull("FALLO: se ha leido un deporte con id diferente al objeto", instance.readSport(noexistingidsporttest));
        // test leer un id existente en DDBB deportes
        assertNotNull("FALLO: NO se ha leido un deporte existente", instance.readSport(idsporttest));
        assertEquals("FALLO: NO se ha leido un deporte con id correcto", instance.readSport(idsporttest),sport1);   
        System.out.println("Fin de test readSport");   
        
    }

    /**
     * Test of updateSport method, of class DeportesDAO.
     */
    @Test
    public void testUpdateSport() throws Exception {
        System.out.println("updateSport");
        Deportes sport = null;
        DeportesDAO instance = new DeportesDAO();
        boolean expResult = false;
        boolean result = instance.updateSport(sport);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteSport method, of class DeportesDAO.
     */
    @Test
    public void testDeleteSport() throws Exception {
        System.out.println("deleteSport");
        Long id = null;
        DeportesDAO instance = new DeportesDAO();
        boolean expResult = false;
        boolean result = instance.deleteSport(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readAllUserSports method, of class DeportesDAO.
     */
    @Test
    public void testReadAllUserSports() throws Exception {
        System.out.println("readAllUserSports");
        Usuarios user = null;
        DeportesDAO instance = new DeportesDAO();
        List<Deportes> expResult = null;
        List<Deportes> result = instance.readAllUserSports(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
