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
package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author musef2904@gmail.com
 */
@Entity
@Table(name = "agenda")
@NamedQueries({
    @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a")
    , @NamedQuery(name = "Agenda.findById", query = "SELECT a FROM Agenda a WHERE a.iduser = :iduser AND a.id = :id")
    , @NamedQuery(name = "Agenda.findByKeyuser", query = "SELECT a FROM Agenda a WHERE a.keyuser = :keyuser")
    , @NamedQuery(name = "Agenda.findBySport", query = "SELECT COUNT (a) FROM Agenda a WHERE a.iduser = :iduser AND a.idsport = :idsport")        
    , @NamedQuery(name = "Agenda.findByActivity", query = "SELECT COUNT (a) FROM Agenda a WHERE a.iduser = :iduser AND a.idactivity = :idactivity")         
    , @NamedQuery(name = "Agenda.findByDate", query = "SELECT a FROM Agenda a WHERE a.iduser = :iduser AND a.ddate >= :date1 AND a.ddate <= :date2")
    , @NamedQuery(name = "Agenda.findByTiming", query = "SELECT a FROM Agenda a WHERE a.iduser = :iduser AND  a.timing = :timing")
    , @NamedQuery(name = "Agenda.findByUserAndGap", query = "SELECT COUNT (a) FROM Agenda a WHERE a.iduser = :iduser AND a.ddate >= :lapsetime AND a.ddate <= :toptime")
    , @NamedQuery(name = "Agenda.findByUserAndTime", query = "SELECT a FROM Agenda a WHERE a.iduser = :iduser AND a.ddate > :lapsetime")})
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "keyuser")
    private String keyuser;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ddate;
    
    @Column(name = "distance")
    private Float distance;
    
    @Column(name = "slope")
    private Float slope;
    
    @Column(name = "timing")
    @Temporal(TemporalType.TIME)
    private Date timing;
    
    @Column(name = "comments")
    private String comments;
    
    @JoinColumn(name = "iduser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios iduser;
    
    @JoinColumn(name = "idsport", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Deportes idsport;
    
    @JoinColumn(name = "idactivity", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Actividades idactivity;

    
    
    public Agenda() {
    }

    public Agenda(Long id) {
        this.id = id;
    }

    public Agenda(String keyuser, Date date, Float distance, Float slope, Date timing, 
            String comments, Usuarios iduser, Deportes idsport, Actividades idactivity) {
        
        this.keyuser = keyuser;
        this.ddate = date;
        this.distance = distance;
        this.slope = slope;
        this.timing = timing;
        this.comments = comments;
        this.iduser = iduser;
        this.idsport = idsport;
        this.idactivity = idactivity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyuser() {
        return keyuser;
    }

    public void setKeyuser(String keyuser) {
        this.keyuser = keyuser;
    }

    public Date getDate() {
        return ddate;
    }

    public void setDate(Date date) {
        this.ddate = date;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getSlope() {
        return slope;
    }

    public void setSlope(Float slope) {
        this.slope = slope;
    }

    public Date getTiming() {
        return timing;
    }

    public void setTiming(Date timing) {
        this.timing = timing;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Usuarios getIduser() {
        return iduser;
    }

    public void setIduser(Usuarios iduser) {
        this.iduser = iduser;
    }

    public Deportes getIdsport() {
        return idsport;
    }

    public void setIdsport(Deportes idsport) {
        this.idsport = idsport;
    }

    public Actividades getIdactivity() {
        return idactivity;
    }

    public void setIdactivity(Actividades idactivity) {
        this.idactivity = idactivity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Agenda[ id=" + id + " ]";
    }
    
}
