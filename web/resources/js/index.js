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


$(document).ready(function(){
    
    var message='';
    $("[id*=login]").click(function() {
        var name=$("[id*=username]").val();
        if (name.length<6 || name.length>15) message+="Longitud inadecuada de nombre de usuario\n";
        var pass=$("[id*=userpass]").val();
        if (pass.length<8 || pass.length>20) message+="Longitud inadecuada de contraseña\n";
        
        // mostramos el mensaje si tiene contenido
        if (message.length>0) alert ("Se han producido los siguientes errores:\n"+message);
    });

    
});