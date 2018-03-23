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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import models.Actividades;
import models.Agenda;
import models.Deportes;
import models.Usuarios;
import org.apache.log4j.Logger;

/**
 *
 * @author musef2904@gmail.com
 */
public class AgendaDAO implements AgendaInterface{

    private EntityManager em;
    private EntityTransaction tx;
    
    private Logger log;
    
    
    public AgendaDAO() {
        
        log=Logger.getLogger("stdout");
        
    }
    
    
    /**
     * Este metodo graba en DDBB un objeto Agenda, con el parametro
     * suministrado
     * Verifica previamente que cumple las condiciones
     * @param calendar
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean createCalendar(Agenda calendar) throws Exception {
      
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
            // logger
            log.error("ERROR: Agenda cr-01 creando->evento "+calendar.getIdactivity()+" del userid "+calendar.getIduser()+" - Mensaje: "+ex);            
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Evento agenda creado ->user: "+calendar.getIduser().getNameuser());
        return true;        
        
    }

    
    /**
     * Este metodo devuelve un objeto Agenda, dado el id suministrado
     * Verifica previamente que cumple las condiciones
     * @param id
     * @return objeto Agenda || null
     * @throws java.lang.Exception
     */
    @Override
    public Agenda readCalendar(Long id) throws Exception {
    
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
            // logger
            log.error("ERROR: Agenda rd-02A leyendo->evento id"+id+" - Mensaje: "+nr);  
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-02B leyendo->evento id"+id+" - Mensaje: "+ex);                        
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return calendar;
        
    }

    
    /**
     * Este metodo modifica en DDBB un objeto Agenda, con el objeto
     * Agenda suministrado
     * Verifica previamente que cumple las condiciones
     * @param calendar
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean updateCalendar(Agenda calendar) throws Exception {
        
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
            // logger
            log.error("ERROR: Agenda up-03 modificando->evento "+calendar.getIdactivity()+" del userid "+calendar.getIduser()+" - Mensaje: "+ex);            
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Evento agenda modificado ->user: "+calendar.getIduser().getNameuser());
        return true;        
        
    }

    
    /**
     * Este metodo borra de DDBB un objeto Agenda, con el parametro
     * id suministrado
     * Verifica previamente que cumple las condiciones
     * @param id
     * @return boolean, con el resultado de la operacion
     * @throws java.lang.Exception
     */
    @Override
    public boolean deleteCalendar(Long id) throws Exception {
       
        /* Verificacion de condiciones */
        if (id<1) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        Agenda cal;
        // iniciamos la transaccion
        try {
            tx.begin();
            // attaching el objeto
            cal=em.find(Agenda.class, id);
            em.remove(cal);          
            tx.commit();
            
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda dl-04 borrando->evento  con id "+id+" - Mensaje: "+ex);                    
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // logger
        log.info("Evento agenda borrado ->user: "+cal.getIduser().getNameuser()+"con id "+id);
        return true;
        
    }
    
    
/**
 * Este método devuelve un listado con las actividades efectuadas por el usuario user,
 * segun el sport, activity y fechas dadas como parametros
 * @param user
 * @param sport
 * @param activity
 * @param fechini
 * @param fechfin
 * @return null | List<Agenda> con las actividades
     * @throws java.lang.Exception
 */
    
    public List<Agenda> listCalendar(Usuarios user, Deportes sport, Actividades activity, String fechini, String fechfin) throws Exception {
    
        /* Verificacion de condiciones */
        if (user==null) return null;
        if (sport==null) return null;
        if (activity==null) return null;
        
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
            Query q=em.createQuery("SELECT ag FROM Agenda ag WHERE ag.iduser= :iduser AND ag.idsport = :idsport AND ag.idactivity=:idactivity AND ag.date>=:fec1 AND ag.date<= :fec2 ORDER BY ag.date DESC");

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
            // logger
            log.error("ERROR: Agenda rd-05A leyendo->parseo fechas"+fec1+"//"+fec2+" - Mensaje: "+ps);                
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-05B leyendo->lista eventos user id"+user.getId()+" - Mensaje: "+ex);                        
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return calendar;
        
    }
    
        
/**
 * Este método devuelve un listado con las actividades efectuadas por el usuario user,
 * segun el sport y fechas dadas como parametros
 * @param user
 * @param sport
 * @param fechini
 * @param fechfin
 * @return null | List<Agenda> con las actividades
     * @throws java.lang.Exception
 */
    
    public List<Agenda> listCalendar(Usuarios user, Deportes sport, String fechini, String fechfin) throws Exception {
    
        /* Verificacion de condiciones */
        if (user==null) return null;
        if (sport==null) return null;
        
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
            Query q=em.createQuery("SELECT ag FROM Agenda ag WHERE ag.iduser= :iduser AND ag.idsport = :idsport AND ag.date>=:fec1 AND ag.date<= :fec2 ORDER BY ag.date DESC");

            q.setParameter("iduser", user);
            q.setParameter("idsport", sport);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
            fec1=sd.parse(fechini.substring(6)+fechini.substring(2,6)+fechini.substring(0,2));
            fec2=sd.parse(fechfin.substring(6)+fechfin.substring(2,6)+fechfin.substring(0,2));
            q.setParameter("fec1", fec1);
            q.setParameter("fec2", fec2);
            
            calendar=(List<Agenda>)q.getResultList();
            
            tx.commit();
            
        } catch (ParseException ps) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-06A leyendo->parseo fechas"+fec1+"//"+fec2+" - Mensaje: "+ps);                
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-06B leyendo->lista eventos user id"+user.getId()+" - Mensaje: "+ex);                       
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return calendar;
        
    }
    
        
    /**
     * Este método devuelve un listado con las actividades efectuadas por el usuario user,
     * segun las fechas dadas como parametros
     * @param user
     * @param fechini
     * @param fechfin
     * @return null | List<Agenda> con las actividades
     * @throws java.lang.Exception
     */
    
    public List<Agenda> listCalendar(Usuarios user, String fechini, String fechfin) throws Exception {
    
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
            Query q=em.createQuery("SELECT ag FROM Agenda ag WHERE ag.iduser= :iduser AND ag.date>=:fec1 AND ag.date<= :fec2 ORDER BY ag.date DESC");

            q.setParameter("iduser", user);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
            fec1=sd.parse(fechini.substring(6)+fechini.substring(2,6)+fechini.substring(0,2));
            fec2=sd.parse(fechfin.substring(6)+fechfin.substring(2,6)+fechfin.substring(0,2));
            q.setParameter("fec1", fec1);
            q.setParameter("fec2", fec2);
            
            calendar=(List<Agenda>)q.getResultList();
            
            tx.commit();
            
        } catch (ParseException ps) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-07A leyendo->parseo fechas"+fec1+"//"+fec2+" - Mensaje: "+ps);               
            return null;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-07B leyendo->lista eventos user id"+user.getId()+" - Mensaje: "+ex);                        
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return calendar;
        
    }
    
    /**
     * Esta funcion recupera el numero de entrenamiento totales, dentro del lapso de
     * tiempo (en dias) suministrado, correspondiente el Usuario user 
     * @param lapse - dias de retroceso en el tiempo, para obtener el historico
     * de sesiones desde esa fecha
     * @param user - usuario de la estadistica
     * @return 0 | int con el num. de sesiones acumuladas de entrenamiento
     * @throws java.lang.Exception
     */
    public long getSessionsByLapseTime (int lapse, Usuarios user) throws Exception {
        
        /* Verificacion de condiciones */
        if (lapse==0) return 0;
        if (user==null) return 0;
        
        // tomamos la fecha de hoy
        Date thisdate=new Date();
        // la pasamos a milisegundos
        long todaytimemillis=thisdate.getTime();

        // calculamos el lapso de tiempo desde el que queremos informacion
        long diff1=(lapse*24);
        long diff2=diff1*3600000;

        // retrocedemos el reloj
        todaytimemillis=todaytimemillis-diff2;
        
        // y creamos la nueva fecha de busqueda
        thisdate.setTime(todaytimemillis);

        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        long result;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Agenda.findByUserAndGap");
            q.setParameter("iduser", user);
            q.setParameter("lapsetime", thisdate);
            
            result=(long)q.getSingleResult();
            
            tx.commit();
            
        } catch (NonUniqueResultException nr) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-08A leyendo->cuantos eventos user id"+user.getId()+" - Mensaje: "+nr);              
            return (long)0;
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-08B leyendo->cuantos eventos user id"+user.getId()+" - Mensaje: "+ex);                        
            return (long)0;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        return result;

    }
    
    
    /**
     * Esta funcion recupera las horas de entrenamiento totales, dentro del lapso de
     * tiempo (en dias) suministrado, correspondiente el Usuario user
     * @param lapse - dias de retroceso en el tiempo, para obtener el historico
     * de horas desde esa fecha
     * @param user - usuario de la estadistica
     * @return null | String con un formato hh:mm:ss con las horas acumuladas de 
     * entrenamiento
     * @throws java.lang.Exception
     */
    public String getHoursByLapseTime (int lapse, Usuarios user) throws Exception {
        
        /* Verificacion de condiciones */
        if (lapse==0) return null;
        if (user==null) return null;
        
        // tomamos la fecha de hoy
        Date thisdate=new Date();
        // la pasamos a milisegundos
        long todaytimemillis=thisdate.getTime();

        // calculamos el lapso de tiempo desde el que queremos informacion
        long diff1=(lapse*24);
        long diff2=diff1*3600000;

        // retrocedemos el reloj
        todaytimemillis=todaytimemillis-diff2;
        
        // y creamos la nueva fecha de busqueda
        thisdate.setTime(todaytimemillis);

        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        tx=em.getTransaction();
        
        // creamos el objeto
        List<Agenda> result;
        
        // iniciamos la transaccion
        try {
            tx.begin();
            Query q=em.createNamedQuery("Agenda.findByUserAndTime");
            q.setParameter("iduser", user);
            q.setParameter("lapsetime", thisdate);
            
            result=q.getResultList();
            
            tx.commit();          
        
        } catch (Exception ex) {
            if (tx!=null && tx.isActive()) tx.rollback();
            // logger
            log.error("ERROR: Agenda rd-09A leyendo->tiempo en eventos user id"+user.getId()+" - Mensaje: "+ex);           
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();            
        }
        
        // vamos a realizar un sumatorio de tiempo
        Iterator iter=result.iterator();
        
        long acumula=0;
        SimpleDateFormat sd=new SimpleDateFormat("HH:mm:ss");
        int hrs=0;
        int mnt=0;
        int scn=0;
        int newDateInSeconds=0;        
        String newdate;
        
        while (iter.hasNext()) {
            // lee cada anotacion, y acumula el timemillis
            Agenda ag=(Agenda) iter.next();

            // metodo por el que se parsea y luego se traduce y obtiene el
            // valor de horas, minutos y segundos y luego se acumula
            newdate=sd.format(ag.getTiming());
            hrs=Integer.parseInt(newdate.substring(0,2));
            mnt=Integer.parseInt(newdate.substring(3,5));
            scn=Integer.parseInt(newdate.substring(6));
            newDateInSeconds=(hrs*3600)+(mnt*60)+scn;
            
            // acumulamos
            acumula+=newDateInSeconds;
            
        }

        // y ahora convertimos lo acumulado en desglosado String
        
        // obtenemos los segundos 
        int segundos=(int)(acumula % 60);
        acumula=acumula-segundos;
        acumula=acumula/60;
        // obtenemos los minutos
        int minutos=(int)(acumula % 60);
        acumula=acumula-minutos;
        acumula=acumula/60;        
        // obtenemos las horas
        int horas=(int)acumula;    
        
        String hr=String.valueOf(horas);
        if (hr.length()<2) hr="0"+hr;
        String mn=String.valueOf(minutos);
        if (mn.length()<2) mn="0"+mn;
        String sg=String.valueOf(segundos);
        if (sg.length()<2) sg="0"+sg;
        
        return hr+":"+mn+":"+sg;

    }
    
    /**
     * Este metodo nos informa de si una actividad (thisactivity) ha sido usada
     * en la agenda o no. 
     * Si la actividad no ha sido usada, es posible borrarla; si ha sido
     * utilizada, no es posible borrarla porque se perderian datos esenciales.
     * @param activity
     * @param user
     * @return boolean; con el resultado usada (true) o no (false)
     * @throws java.lang.Exception
     */
    public boolean usedActivity(Actividades activity, Usuarios user) throws Exception {
        
        // verificacion de condiciones
        if (activity==null) return false;
        
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        
        tx=em.getTransaction();
        
        Long count=(long)0;
        try {
            String sql="SELECT COUNT(u) FROM Agenda u WHERE u.idactivity=:act AND u.iduser=:iduser";
            tx.begin();
            
            Query q=em.createQuery(sql);
            q.setParameter("act", activity);
            q.setParameter("iduser", user.getId());
            count=(Long)q.getSingleResult();
            
        } catch (Exception ex) {
            // logger
            log.error("ERROR: Agenda rd-10 leyendo->actividad usada del user id"+user.getId()+" - Mensaje: "+ex);
            // evitamos el borrado
            return true;
        } finally {
            if (tx!=null && tx.isActive()) em.close();
        }
        
        // sin resultados: borramos
        if (count<1) return false;        

        return true;
    }
    
    /**
     * Este método comprueba si el dia thisday hay alguna actividad
     * realizada en la agenda
     * 
     * @param thisday
     * @param user
     * @return boolean
     * @throws java.lang.Exception 
     */
    public boolean existActivity(String thisday, Usuarios user) throws Exception {
        
       // verificamos parametros de entrada
       if (thisday==null || thisday.length()!=10) return false;
             
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        
        tx=em.getTransaction();
        
        // tomamos la fecha en formato String para generar un Date
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        cal.set(Integer.parseInt(thisday.substring(6)), Integer.parseInt(thisday.substring(3, 5)), Integer.parseInt(thisday.substring(0, 2)));
        
        Date ddate=cal.getTime();
        long result=0;
        try {
            String sql="SELECT COUNT(u) FROM Agenda u WHERE u.date=:ddate AND u.iduser=:iduser";
            tx.begin();
            
            Query q=em.createQuery(sql);
            q.setParameter("ddate", ddate);
            q.setParameter("iduser", user);
            result=(Long)q.getSingleResult();
            
        } catch (Exception ex) {
            // logger
            log.error("ERROR: Agenda rd-11 leyendo->dia con actividad del user id"+user.getId()+" - Mensaje: "+ex);            
            return false;
        } finally {
            if (tx!=null && tx.isActive()) em.close();
        }
        
        // hay resultados
        if (result>0) return true;

        
        return false;
    }


    /**
     * Este método comprueba si el dia thisday hay alguna actividad
     * realizada en la agenda, y devuelve la información del día
     * 
     * @param thisday
     * @param iduser
     * @return null si no hay datos | hashmap con la info del dia
     * @throws java.lang.Exception
     */
    public HashMap<String,String> getActivityOfToday(String thisday, Usuarios user) throws Exception {
        
        HashMap<String,String> returndata=null;
        
       // verificamos parametros de entrada
       if (thisday==null || thisday.length()!=10) {
           return returndata;
       }
       
       if (user==null) return null;
             
        // creamos los objetos de transaccion
        em=Factory.getEmf().createEntityManager();
        
        tx=em.getTransaction();
        
        // tomamos la fecha en formato String para generar un Date;
        
        String chgday=thisday.substring(6)+"-"+thisday.substring(3, 5)+"-"+thisday.substring(0, 2);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date ddate1;
        Date ddate2;
        try {
            ddate1 = sdf.parse(chgday+" 00:00:00");
            ddate2 = sdf.parse(chgday+" 23:59:59");
        } catch (ParseException ex) {
            // logger
            log.error("ERROR: Agenda rd-12B leyendo->parseando fecha del user id"+user.getId()+" - Mensaje: "+ex);            
            return null;
        }
        
        List<Agenda> result=null;
        try {
            tx.begin();
            Query q=em.createNamedQuery("Agenda.findByDate");
            q.setParameter("iduser", user);
            q.setParameter("date1", ddate1);
            q.setParameter("date2", ddate2);
            result=q.getResultList();
            
        } catch (Exception ex) {
            // logger
            log.error("ERROR: Agenda rd-12A leyendo->dia con actividad del user id"+user.getId()+" - Mensaje: "+ex); 
            return null;
        } finally {
            if (tx!=null && tx.isActive()) em.close();
        }
                        
        // hay resultados
        if (result!=null && result.size()>0) {
            returndata=new HashMap<>();
            returndata.put("time", result.get(0).getTiming().toString());            
            returndata.put("spname",result.get(0).getIdsport().getSportName());
            returndata.put("acname",result.get(0).getIdactivity().getName());
            return returndata;
        }
        
        return null;
    }



    
}
