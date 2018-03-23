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

import daos.AgendaDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import models.Usuarios;
import org.apache.log4j.Logger;


/**
 *
 * @author musef2904@gmail.com
 */

/**
 * Este managedBean genera el control de fechas.
 * 
 * @author musef
 *
 * @version 1.2JSF 2015-03-15
 */


public class MainComponent {
    
    private String dateSystem;                  // fecha del sistema. Necesario para statistics
    private final SimpleDateFormat dateF=new SimpleDateFormat("dd-MM-yyyy");             // formato de fechas
    protected static int MAX_YEAR;              // limite de fechas anuales
    
    private AgendaDAO adao;
    
    private Logger log;
    
    public MainComponent() {
        
        log=Logger.getLogger("stdout");
    }
    
    
    /**
     * La funcion tomando la fecha actual crea la variable static MAX_YEAR
     */
    @PostConstruct
    protected void init() {
        
        if (dateSystem==null || dateSystem.isEmpty()) {
            Date date=new Date();
            dateSystem=dateF.format(date);

            // obtenemos el año actual para fecha
            try {
            	MAX_YEAR=Integer.parseInt(dateSystem.substring(6));
            } catch (NumberFormatException nf) {
            	MAX_YEAR=2099;
            }
            
        }
        
        adao=new AgendaDAO();
        
    } // end of method
    
    
    
    /**
     * Este método obtiene el día actual del calendario y lo almacena en la variable today
     */
    private void initToday() {

        init();
  
    } // end of method
    
    
    
    /**
     * Este método devuelve un array (6 sem * 7 días) con un calendario del mes estilo español,
     * empezando por el lunes. Pone los días vacíos a cero y guarda el número del día en cada posición.
     * 
     * @return int[] conteniendo el calendario
     */
    
    public int[] monthCalendar(String thisdate) {
    
        
        int thismonth[]=new int[42];
        
        Calendar cal=Calendar.getInstance();
        // primero seteamos el dia con la fecha fijada en today
        int d=0;
        int m=0;
        int a=0;
        try {
            d=Integer.parseInt(thisdate.substring(0,2));
            m=Integer.parseInt(thisdate.substring(3,5))-1;
            a=Integer.parseInt(thisdate.substring(6));
        } catch (NumberFormatException nf) {
            d=1;
            m=1;
            a=2014;
        }

        // obtenemos el numero de dia actual, teniendo en cuenta que es el 1 del mes corriente
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // seteamos a dia 1 del mes corriente
        cal.set(a,m,1);
        

        int numDay=cal.get(Calendar.DAY_OF_WEEK);
        // obtenemos el numero de dias que tiene el mes
        int numDays=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // rellenamos la matriz con ceros
        for (int j=0;j<42;j++) {
            thismonth[j]=0;
        }
        
        if (numDay==1) {
            // si el mes comienza en lunes deja la primera semana en blanco
            for (int n=1;n<=numDays;n++) {
                // el calendario empieza en 7
                thismonth[n+6]=n;
            }
        } else if (numDay==0){
            // si la semana empieza en domingo
            for (int n=1;n<=numDays;n++) {
                thismonth[n+5]=n;
            }
        } else {
            // resto de dias
            for (int n=1;n<=numDays;n++) {
                thismonth[n+numDay-3]=n;
            }
        }
        
        return thismonth;
        
    } // end of method
    
    
    /**
     * Rellena y devuelve una matriz con los días del calendario correspondiente,
     * y si existe actividad en ese dia, devuelve dia+100
     * @param calendar
     * @param thisday
     * @param user
     * @return 
     */
    public int[] refillingCalendar(int[]calendar, String thisday, Usuarios user ) {
        
        for (int n=0;n<41;n++) {
            // procesamos cada dia
            if (calendar[n]>0) {
                String dday=calendar[n]+thisday.substring(2);
                if (dday.length()<10) dday="0"+calendar[n]+thisday.substring(2);
                adao=new AgendaDAO();
                try {
                    if (adao.existActivity(dday,user)) {
                        // si existe actividad, sumamos 100 al valor
                        // del dia para mostrar otra imagen
                        calendar[n]=calendar[n]+100;
                    }
                } catch (Exception ex) {
                    log.error("ERROR: Algo ha ido mal rellenando el calendario para el user "+user.getId()+" - mensaje: "+ex);
                    return null;
                }
            }

        }
        
        return calendar;
        
    }
    
 
    /**
     * Este método retrocede un mes en el calendario y lo almacena en la variable estática today
     * @param thisdate
     * @return String
     */
    public String getLastMonth(String thisdate) {

        Calendar cal=Calendar.getInstance();
        // primero seteamos el dia al dia fijado en today
        int d=0;
        int m=0;
        int a=0;
        try {
            d=Integer.parseInt(thisdate.substring(0,2));
            // retroceder es el mes obtenido-2
            m=Integer.parseInt(thisdate.substring(3,5))-2;
            a=Integer.parseInt(thisdate.substring(6));
        } catch (NumberFormatException nf) {
            d=1;
            m=1;
            a=2014;
        }
        // seteamos al nuevo mes
        cal.set(a,m,d);

        // formateamos la fecha
        return dateF.format(cal.getTime());
        
    } // end of method
    
    
    
    /**
     * Este método avanza un mes en el calendario y lo almacena en la variable estática today
     * @param thisdate
     * @return String
     */
    public String getNextMonth(String thisdate) {

        Calendar cal=Calendar.getInstance();
        // primero seteamos el dia al dia fijado en today
        int d=0;
        int m=0;
        int a=0;
        try {
            d=Integer.parseInt(thisdate.substring(0,2));
            // avanzar es el mes obtenido(-1+1)
            m=Integer.parseInt(thisdate.substring(3,5));
            a=Integer.parseInt(thisdate.substring(6));
        } catch (NumberFormatException nf) {
            d=1;
            m=1;
            a=2014;
        }
        // seteamos la nueva fecha
        cal.set(a,m,d);

        // formateamos fecha
        return dateF.format(cal.getTime());
        
    } // end of method
    
    
    /**
     * Este método devuelve un list[] de una combinación de meses y años 
     * desde la fecha thisDate. Seis meses por delante y 12 meses por detras
     * de la fecha que le hayamos suministrado.
     * 
     * @param thisDate, String con la fecha(formato español) a partir de la cual conseguir el list de meses.
     * @return un objeto List String[] conteniendo una lista de 12 pares mes y año o empty si hay error.
     */
	
    public List<String[]> last18Months(String thisDate) {
        
        // la matriz va de adelante a atras en el tiempo (desde el mes actual 
        // hasta el inicio de la serie)
        List<String[]> monthList=new ArrayList<>();
        
        // recibimos fecha actual
        String ddate=thisDate;
        
        // primero obtenemos el dia, mes y año por separado
        int d=0;
        int m=0;
        int a=0;
        try {
            d=Integer.parseInt(ddate.substring(0,2));
            m=Integer.parseInt(ddate.substring(3,5));
            a=Integer.parseInt(ddate.substring(6));
        } catch (NumberFormatException nf) {
            d=1;
            m=1;
            a=2014;
        }
        
        // vamos a tomar 6 meses hacia adelante y 12 meses hacia atras
        m=m+6;
        if (m>12) {
            // el mes futuro es mayor que diciembre, hay que restar
            // meses y subir año
            m=m-12;
            a++;
        }

        // procesamos el mes, y creamos un string con el mes en
        // formato letra y el año
        for (int n=0;n<18;n++){
            String data[]=new String[2];
            data[0]="";
            String annus=String.valueOf(a);
            switch (m) {
                case 1: 
                    data[0]="Enero-"+annus;
                    break;
                case 2: 
                    data[0]="Febrero-"+annus;
                    break;
                case 3: 
                    data[0]="Marzo-"+annus;
                    break;
                case 4: 
                    data[0]="Abril-"+annus;
                    break;
                case 5: 
                    data[0]="Mayo-"+annus;
                    break;
                case 6: 
                    data[0]="Junio-"+annus;
                    break;
                case 7: 
                    data[0]="Julio-"+annus;
                    break;
                case 8: 
                    data[0]="Agosto-"+annus;
                    break;
                case 9: 
                    data[0]="Septiembre-"+annus;
                    break;
                case 10: 
                    data[0]="Octubre-"+annus;
                    break;
                case 11: 
                    data[0]="Noviembre-"+annus;
                    break;
                case 12: 
                    data[0]="Diciembre-"+annus;
                    break;
                default: data[0]="ERROR";
            }
            // lo mismo, pero con el mes en formato numero
            if (m<10) data[1]="0"+String.valueOf(m)+"-"+String.valueOf(a);
                else data[1]=String.valueOf(m)+"-"+String.valueOf(a);
            // añadimos al list la matriz con valores
            monthList.add(data);
            
            // decrementamos el mes, y si sale de limites
            // tomamos diciembre
            if (--m<1) {
                // cambio de año
                m=12;
                a--;
            }
        }        
        
        return monthList;
        
    } // end of method
    
    
    /**
     * Este metodo convierte una fecha en formato String dd-mm-yyyy en un objeto
     * Date
     * @param thisday
     * @return 
     */
    public Date convertStringToDate(String thisday) {
        
        // control de parametros
        if (thisday==null || thisday.isEmpty()) return null;
        if (thisday.length()==9) thisday="0"+thisday;
        if (thisday.length()!=10) return null;
        
        Calendar cal=Calendar.getInstance();
        // primero seteamos el dia al dia fijado en today
        int d=0;
        int m=0;
        int a=0;
        try {
            d=Integer.parseInt(thisday.substring(0,2));
            m=Integer.parseInt(thisday.substring(3,5))-1;
            a=Integer.parseInt(thisday.substring(6));
        } catch (NumberFormatException nf) {
            return null;
        }
        cal.set(a,m,d);
                
        // devolvemos la fecha
        return cal.getTime();
    }
    
    
    /**
     * Este metodo devuelve un hashmap con datos de la actividad del dia (solo la 
     * primera actividad si existen varias)
     * @param thisday
     * @param iduser
     * @return null si algo falla o el HashMap
     */
    public HashMap<String,String> getDayActivity(String thisday, Usuarios iduser) {
        
        adao=new AgendaDAO();
       
        HashMap<String,String>todayActivity=null;
        try {
            todayActivity = adao.getActivityOfToday(thisday,iduser);
            return todayActivity;
        } catch (Exception ex) {
            log.error("ERROR: Algo ha ido mal obteniendo la actividad del dia para el user "+iduser+" - mensaje: "+ex);
            return null;
        }
                
    }
    
    
    
    // SETTERS AND GETTERS

    public String getDateSystem() {
        return dateSystem;
    }


} // ****************** END OF CLASS

