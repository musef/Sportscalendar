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


/**
 *
 * @author musef2904@gmail.com
 * 
 * Deportes:
 * Hay uno/varios deportes para un mismo usuario.
 * 
 * Un deporte de nombre sportName, y descripcion sportDescript
 * Puede haber una lista de recorridosList que engloba diversos recorridos
 */


@Entity
@Table(name = "deportes")
@NamedQueries({
    @NamedQuery(name = "Deportes.findAll", query = "SELECT d FROM Deportes d")
    , @NamedQuery(name = "Deportes.findById", query = "SELECT d FROM Deportes d WHERE d.id = :id")
    , @NamedQuery(name = "Deportes.findByKeyuser", query = "SELECT d FROM Deportes d WHERE d.keyuser = :keyuser")
    , @NamedQuery(name = "Deportes.findBySportName", query = "SELECT d FROM Deportes d WHERE d.sportName = :sportName")
    , @NamedQuery(name = "Deportes.findBySportDescrip", query = "SELECT d FROM Deportes d WHERE d.sportDescrip = :sportDescrip")})
public class Deportes implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "keyuser",length = 50)
    private String keyuser;

    @Column(name = "sportName",length = 50)
    private String sportName;
    
    @Column(name = "sportDescrip",length = 255)
    private String sportDescrip;
    
    @JoinColumn(name = "iduser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios iduser;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsport")
    private List<Actividades> recorridosList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsport")
    private List<Agenda> agendaList;
    
    
    public Deportes() {
    }
    
    public Deportes(Long id) {
        this.id=id;
    }


    public Deportes(String keyuser, String sportName, String sportDescript, Usuarios user ) {
        
        this.keyuser = keyuser;
        this.sportName = sportName;
        this.iduser = user;
        this.sportDescrip=sportDescript;
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

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportDescrip() {
        return sportDescrip;
    }

    public void setSportDescrip(String sportDescrip) {
        this.sportDescrip = sportDescrip;
    }

    public Usuarios getIduser() {
        return iduser;
    }

    public void setIduser(Usuarios iduser) {
        this.iduser = iduser;
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
        if (!(object instanceof Deportes)) {
            return false;
        }
        Deportes other = (Deportes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Deportes[ id=" + id + " ]";
    }

    public List<Actividades> getRecorridosList() {
        return recorridosList;
    }

    public void setRecorridosList(List<Actividades> recorridosList) {
        this.recorridosList = recorridosList;
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
