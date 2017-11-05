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
    
    $("[id*=eliminarsp]").click(function(){
        var seleccion=$("[id*=sportselect]").val();
        
        // si no hay ningun deporte seleccionado se le advierte
        // de que no es posible
        if (seleccion==null || seleccion==0) {
            alert("No es posible realizar esta acción \nporque no ha seleccionado ningún deporte");
            return false;
        }
    });

    
    $("[id*=grabarsp]").click(function(){
        
        // inicializamos variables y background
        var message='';
        $("[id*=namesp]").css('backgroundColor','white');
        $("[id*=descrsp]").css('backgroundColor','white');
        
        var namesp=$("[id*=namesp]").val();
        if (namesp.length<1 || namesp.length>50) {
            message+="Longitud inadecuada de nombre de deporte\n";
            $("[id*=namesp]").css('backgroundColor','red');
        }
        var descsp=$("[id*=descrsp]").val();
        if (descsp.length>255) {
            message+="Longitud inadecuada de descripción de deporte\n";
            $("[id*=descrsp]").css('backgroundColor','red');
        }

        // mostramos el mensaje si tiene contenido
        if (message.length>0) {
            alert("Se han encontrado los siguientes errores:\n"+message);
            message='';
            return false;
        } else return true;
    });
    
});

