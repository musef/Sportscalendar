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
    
    // boton eliminar
    $("[id*=eliminarac]").click(function(){
        
        // si no hay evento seleccionado no es posible eliminarlo
        var ev=$("[id*=thisEventToModify]").val();
        if (isNaN(ev) || ev<1) {
            alert("NO es posible eliminar un evento de agenda inexistente");
            return false;
        }
        
        // si no hay actividad elegida, no es posible eliminar
        var sel=$("[id*=activityselected]").val();        
        if (sel==='0') {
            alert("No es posible realizar esta acción \nporque no ha seleccionado ninguna actividad");
            return false;
        }
        
        return true;
    });
    
    // boton grabar
    $("[id*=grabarac]").click(function(){
        
        // hay que atribuir alguna actividad al evento
        var sel=$("[id*=activityselected]").val();       
        if (sel==='0') {
            alert("No es posible realizar esta acción \nporque no ha seleccionado ninguna actividad");
            return false;
        }
        
        
        // inicializamos variables y background
        var message='';
               
        $("[id*=dataday]").css('backgroundColor','white');
        $("[id*=dataname]").css('backgroundColor','white');
        $("[id*=datasite]").css('backgroundColor','white');        
        $("[id*=datadrt]").css('backgroundColor','white'); 
        $("[id*=datadst]").css('backgroundColor','white'); 
        $("[id*=dataslp]").css('backgroundColor','white'); 
        $("[id*=datadsc]").css('backgroundColor','white'); 
        
        // patron de signos excluidos
        var reg=new RegExp("<|>|&|@|#|%|!");
        
        var name=$("[id*=dataname]").val();        
        if (name.length<1 || name.length>100) {
            message+="Longitud inadecuada de nombre de evento\n";
            $("[id*=dataname]").css('backgroundColor','red');
        }

        if (reg.test(name)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=dataname]").css('backgroundColor','red');             
        }

        var daytime=$("[id*=dataday]").val();
        if (daytime.length!==10) {
            message+="Formato inadecuado de fecha (dd-mm-aaaa)\n";
            $("[id*=dataday]").css('backgroundColor','red');
        }

        if (reg.test(daytime)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=dataday]").css('backgroundColor','red');             
        }

        var duration=$("[id*=datadrt]").val();
        if (duration.length!==8) {
            message+="Formato inadecuado de tiempo (hh:mm:ss)\n";
            $("[id*=datadrt]").css('backgroundColor','red');
        }
        
        
        var site=$("[id*=datasite]").val();        
        if (site.length>100) {
            message+="Longitud inadecuada de lugar\n";
            $("[id*=datasite]").css('backgroundColor','red');
        }

        if (reg.test(site)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=datasite]").css('backgroundColor','red');            
        }

        var desc=$("[id*=datadsc]").val();        
        if (desc.length>255) {
            message+="Longitud inadecuada de descripción\n";
            $("[id*=datadsc]").css('backgroundColor','red');
        }        

        if (reg.test(desc)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=datadsc]").css('backgroundColor','red');           
        }

        var dist=$("[id*=datadst]").val();        
        if (isNaN(dist)) {
            message+="Distancia incorrecta\n";
            $("[id*=datadst]").css('backgroundColor','red');
        }             
        
        var slp=$("[id*=dataslp]").val();        
        if (isNaN(slp)) {
            message+="Desnivel incorrecto\n";
            $("[id*=dataslp]").css('backgroundColor','red');
        }               
        
        // mostramos el mensaje si tiene contenido
        if (message.length>0) {
            alert("Se han encontrado los siguientes errores:\n"+message);
            message='';
            return false;
        } else return true;
        
    });
    
    
});