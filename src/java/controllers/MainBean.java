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
package controllers;

import components.MainComponent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@ViewScoped
public class MainBean {

    //manager
    private MainComponent mainComponent;
    
    // es el dia elegido del calendario, en formato string
    // si se ha elegido mes, entonces se ubica en el dia 15
    private static String presentDay;
    
    private String dateCalendar;    // es el indice del select mm-aaaa
    
    // calendarios semanales
    private String[]calendar1;
    private String[]calendar2;
    private String[]calendar3;
    private String[]calendar4;
    private String[]calendar5;
    private String[]calendar6;
    
    // info del dia
    private String agSport;
    private String agActivity;
    private String agTime;
    
    
    private final List<String> months=new ArrayList<>(Arrays.asList("Enero","Febrero","Marzo",
            "Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"));
    private String[] years;
    
    private static List<String[]> monthchain;
    
    
    public MainBean() {
    
        mainComponent=new MainComponent();                 
        
        // al entrar comprobamos si tenemos dia posicionado en calendario
        if (presentDay==null || presentDay.isEmpty()) {
            // creamos el formateador de fechas y transformamos a formato string
            SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
            // se fija el present day en hoy
            presentDay=sd.format(new Date());
            // obtenemos la actividad del dia, si existe
            showTodayActivity();
            // construimos un nuevo calendario
            if (monthchain==null || monthchain.isEmpty()) buildCalendar(presentDay);
        }
           
    }
    
    
    /**
     * Construye un calendario con el dia asignado
     * POr un lado se construira un calendario de 6 sem * 7 dias correspondiente
     * al calendario que se vera en pantalla conforme a un dia/mes determinado
     * Por otro lado, se fabrican 6 matrices semanales con la informacion del calendario
     * mensual; estos calendarios tienen por objeto una mejor representacion en pantalla.
     * @param day 
     */
    public void buildCalendar(String day) {
        
        // construimos un list con los 12 meses anteriores al dia asignado
        monthchain=mainComponent.last18Months(day);
        
        // obtenemos el calendario actual mensual
        // consiste en una matriz de 6 semanas, que sera
        // luego representada visualmente en pantalla
        int[]calendar=mainComponent.monthCalendar(day);
        
        calendar=mainComponent.refillingCalendar(calendar,day,LoginBean.user);

        // instanciamos los seis calendarios semanales
        // para una mejor gestion del calendario
        calendar1=new String[7];
        calendar2=new String[7];
        calendar3=new String[7];
        calendar4=new String[7];
        calendar5=new String[7];
        calendar6=new String[7];        
        if (calendar!=null) {
            // introducimos los datos en cada calendario semanal
            for (int j=0;j<7;j++) {
                calendar1[j]=String.valueOf(calendar[j]);
                calendar2[j]=String.valueOf(calendar[j+7]);
                calendar3[j]=String.valueOf(calendar[j+14]);
                calendar4[j]=String.valueOf(calendar[j+21]);
                calendar5[j]=String.valueOf(calendar[j+28]);
                calendar6[j]=String.valueOf(calendar[j+35]);
            }
        }        
        
        // por ultimo, hacemos un option selected del mes dentro del calendario
        // dia de asignacion
        this.dateCalendar=day.substring(3);
    }
    

    /**
     * Cambia el mes al seleccionado mediante el formulario
     * @param e 
     */
    public void changeMonth(ValueChangeEvent e) {
        
        // recibimos el mes seleccionado en formato mm-yyyy
        String newmonth=e.getNewValue().toString();
        // asignamos el presentDay al 15 de cada mes seleccionado
        presentDay="15-"+newmonth;
        
        // y ahora fabricamos el nuevo calendario
        buildCalendar(presentDay);
        
        String s="";
    }
    
    
    /**
     * Retrocede un mes en el calendario 
     */
    public void lastMonth () {

        // asignamos el presentDay al 15 de cada mes seleccionado
        presentDay=mainComponent.getLastMonth(presentDay);
        
        // y ahora fabricamos el nuevo calendario
        buildCalendar(presentDay);
    }
    
    
    /**
     * Cambia el dia de trabajo
     * @param day 
     */
    public void changeDay(String day) {
        
        int n=Integer.parseInt(day);
        // si el dia es mayor que 100 es porque tiene actividad
        // y eso hay que modificarlo
        if (n>100) {
            n-=100;
            day=day.substring(1);
        }
        
        mainComponent.changeDay(n);
        
        if (day.length()==1) presentDay="0"+day+presentDay.substring(2); 
        else {
            presentDay=day+presentDay.substring(2);
        }
        
    }

    
    /**
     * Avanza un mes en el calendario
     */
    public void nextMonth () {

        // asignamos el presentDay al 15 de cada mes seleccionado
        presentDay=mainComponent.getNextMonth(presentDay);
        
        // y ahora fabricamos el nuevo calendario
        buildCalendar(presentDay);
    }
    
    
    /**
     * Muestra la actividad del dia en el formulario
     */
    public void showTodayActivity() {
        
        // leemos la actividad del dia
        HashMap<String,String>todayActivity=mainComponent.getDayActivity(presentDay,LoginBean.user);
        
        // si existen datos se trasladan a los valores del formulario
        if (todayActivity!=null) {
            agTime=todayActivity.get("time");
            agSport=todayActivity.get("spname");
            agActivity=todayActivity.get("acname");
        }
        
    }
    
    
    /* ****************** GETTERS AND SETTERS *********************** */
    


    /**
     * @return the calendar
     */
    public String[]getCalendar1() {
        return calendar1;
    }

    /**
     * @param calendar1 the calendar to set
     */
    public void setCalendar1(String[] calendar1) {
        this.calendar1 = calendar1;
    }

    /**
     * @return the calendar2
     */
    public String[] getCalendar2() {
        return calendar2;
    }

    /**
     * @param calendar2 the calendar2 to set
     */
    public void setCalendar2(String[] calendar2) {
        this.calendar2 = calendar2;
    }

    /**
     * @return the calendar3
     */
    public String[] getCalendar3() {
        return calendar3;
    }

    /**
     * @param calendar3 the calendar3 to set
     */
    public void setCalendar3(String[] calendar3) {
        this.calendar3 = calendar3;
    }

    /**
     * @return the calendar4
     */
    public String[] getCalendar4() {
        return calendar4;
    }

    /**
     * @param calendar4 the calendar4 to set
     */
    public void setCalendar4(String[] calendar4) {
        this.calendar4 = calendar4;
    }

    /**
     * @return the calendar5
     */
    public String[] getCalendar5() {
        return calendar5;
    }

    /**
     * @param calendar5 the calendar5 to set
     */
    public void setCalendar5(String[] calendar5) {
        this.calendar5 = calendar5;
    }

    /**
     * @return the calendar6
     */
    public String[] getCalendar6() {
        return calendar6;
    }

    /**
     * @param calendar6 the calendar6 to set
     */
    public void setCalendar6(String[] calendar6) {
        this.calendar6 = calendar6;
    }

    

    /**
     * @return the dateCalendar
     */
    public String getDateCalendar() {
        return this.dateCalendar;
    }

    /**
     * @param dateCalendar the dateCalendar to set
     */
    public void setDateCalendar(String dateCalendar) {
        this.dateCalendar = dateCalendar;
    }

    /**
     * @return the monthchain
     */
    public List<String[]> getMonthchain() {
        return monthchain;
    }

    /**
     * @param monthchain2 the monthchain to set
     */
    public void setMonthchain(List<String[]> monthchain2) {
        monthchain = monthchain2;
    }

    

    public String getPresentDay() {
        return presentDay;
    }

    /**
     * @return the agSport
     */
    public String getAgSport() {
        return agSport;
    }

    /**
     * @param agSport the agSport to set
     */
    public void setAgSport(String agSport) {
        this.agSport = agSport;
    }

    /**
     * @return the agActivity
     */
    public String getAgActivity() {
        return agActivity;
    }

    /**
     * @param agActivity the agActivity to set
     */
    public void setAgActivity(String agActivity) {
        this.agActivity = agActivity;
    }

    /**
     * @return the agTime
     */
    public String getAgTime() {
        return agTime;
    }

    /**
     * @param agTime the agTime to set
     */
    public void setAgTime(String agTime) {
        this.agTime = agTime;
    }
 
    
    
}
