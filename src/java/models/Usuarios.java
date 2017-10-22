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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author musef2904@gmail.com
 *
 * Usuarios:
 * Hay varios usuarios, que practican una lista de Deportes, y Actividades,
 * y tienen una lista de eventos realizados en la Agenda.
 * 
 * Un usuario de nombre nameuser, tiene un username login y una contraseña password.
 * TIene una direccion de correo email y una imagen image.
 */


@Entity
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findById", query = "SELECT u FROM Usuarios u WHERE u.id = :id")
    , @NamedQuery(name = "Usuarios.findByKeyuser", query = "SELECT u FROM Usuarios u WHERE u.keyuser = :keyuser")
    , @NamedQuery(name = "Usuarios.findByLogin", query = "SELECT u FROM Usuarios u WHERE u.login = :login")
    , @NamedQuery(name = "Usuarios.findByPassword", query = "SELECT u FROM Usuarios u WHERE u.password = :password")
    , @NamedQuery(name = "Usuarios.findByNameuser", query = "SELECT u FROM Usuarios u WHERE u.nameuser = :nameuser")
    , @NamedQuery(name = "Usuarios.findByEmail", query = "SELECT u FROM Usuarios u WHERE u.email = :email")})
public class Usuarios implements Serializable {



    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "keyuser",length = 50)
    private String keyuser;

    @Column(name = "login",length = 50)
    private String login;

    @Column(name = "password",length = 255)
    private String password;

    @Column(name = "nameuser",length = 100)
    private String nameuser;

    @Column(name = "email",length = 100)
    private String email;
    
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    private List<Deportes> deportesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    private List<Actividades> recorridosList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    private List<Agenda> agendaList;
    
    
    public Usuarios() {
    }

    public Usuarios(Long id) {
        this.id=id;
    }

    public Usuarios(String keyuser, String login, String password, String nameuser, String email,byte[] image) {

        this.keyuser = keyuser;
        this.login = login;
        this.password = password;
        this.nameuser = nameuser;
        this.email = email;
        this.image = image;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Deportes> getDeportesList() {
        return deportesList;
    }

    public void setDeportesList(List<Deportes> deportesList) {
        this.deportesList = deportesList;
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


    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Usuarios[ id=" + id + " ]";
    }


    
}
