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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author musef2904@gmail.com
 
 Actividades:
 Hay una/varias actividades para un mismo deporte.
 Hay una/varias actividades para un mismo usuario 
 
 Una actividad de nombre name, tiene una distancia de distance,
 un desnivel de slope y una duracion de timing; es realizado en
 el lugar site con una descripcion description
 * 
 */
@Entity
@Table(name = "actividades")
@NamedQueries({
    @NamedQuery(name = "Actividades.findAll", query = "SELECT r FROM Actividades r")
    , @NamedQuery(name = "Actividades.findById", query = "SELECT r FROM Actividades r WHERE r.id = :id")
    , @NamedQuery(name = "Actividades.findByKeyuser", query = "SELECT r FROM Actividades r WHERE r.keyuser = :keyuser")
    , @NamedQuery(name = "Actividades.findByName", query = "SELECT r FROM Actividades r WHERE r.name = :name")
    , @NamedQuery(name = "Actividades.findByDistance", query = "SELECT r FROM Actividades r WHERE r.distance = :distance")
    , @NamedQuery(name = "Actividades.findBySlope", query = "SELECT r FROM Actividades r WHERE r.slope = :slope")
    , @NamedQuery(name = "Actividades.findByTiming", query = "SELECT r FROM Actividades r WHERE r.timing = :timing")
    , @NamedQuery(name = "Actividades.findBySite", query = "SELECT r FROM Actividades r WHERE r.site = :site")
    , @NamedQuery(name = "Actividades.findByDescription", query = "SELECT r FROM Actividades r WHERE r.description = :description")})
public class Actividades implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "keyuser", length = 50)
    private String keyuser;

    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "distance")
    private Float distance;
    
    @Column(name = "slope")
    private Float slope;
    
    @Column(name = "timing")
    @Temporal(TemporalType.TIME)
    private Date timing;
    
    @Column(name = "site", length = 100)
    private String site;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @JoinColumn(name = "iduser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios iduser;

    @JoinColumn(name = "idsport", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Deportes idsport;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idactivity")
    private List<Agenda> agendaList;
    
    
    
    public Actividades() {
    }

    public Actividades(Long id) {
        this.id = id;
    }

    public Actividades(String keyuser, String name, Float distance, Float slope, 
            Date timing, String site, String description, Usuarios iduser, Deportes idsport) {
        
        this.keyuser = keyuser;
        this.name = name;
        this.distance = distance;
        this.slope = slope;
        this.timing = timing;
        this.site = site;
        this.description = description;
        this.iduser = iduser;
        this.idsport = idsport;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividades)) {
            return false;
        }
        Actividades other = (Actividades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Actividades[ id=" + id + " ]";
    }

    /**
     * @return the agendaList
     */
    public List<Agenda> getAgendaList() {
        return agendaList;
    }

    /**
     * @param agendaList the agendaList to set
     */
    public void setAgendaList(List<Agenda> agendaList) {
        this.agendaList = agendaList;
    }
    
}
