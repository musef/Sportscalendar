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
    
    $("[id*=eliminarac]").click(function(){
        
        var sel=$("[id*=activityselect]").val();
        
        if (sel==0) {
            alert("No es posible realizar esta acción \nporque no ha seleccionado ninguna actividad");
            return false;
        }
        return true;
    });
    
    
    $("[id*=grabarac]").click(function(){
        
        // inicializamos variables y background
        var reg=new RegExp("<|>|&|@|#|%|!");
        var message='';
        
        
        $("[id*=activitynm]").css('backgroundColor','white');
        $("[id*=activitytm]").css('backgroundColor','white');
        $("[id*=activityst]").css('backgroundColor','white');        
        $("[id*=activitydc]").css('backgroundColor','white'); 
        $("[id*=activityds]").css('backgroundColor','white'); 
        $("[id*=activitysl]").css('backgroundColor','white');         
        
        var name=$("[id*=activitynm]").val();        
        if (name.length<1 || name.length>100) {
            message+="Longitud inadecuada de nombre de actividad\n";
            $("[id*=activitynm]").css('backgroundColor','red');
        }

        if (reg.test(name)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=activitynm]").css('backgroundColor','red');             
        }

        var timming=$("[id*=activitytm]").val();
        if (timming.length!==8) {
            message+="Formato inadecuado de tiempo (hh:mm:ss)\n";
            $("[id*=activitytm]").css('backgroundColor','red');
        }
        
        var site=$("[id*=activityst]").val();        
        if (site.length>100) {
            message+="Longitud inadecuada de lugar\n";
            $("[id*=activityst]").css('backgroundColor','red');
        }

        if (reg.test(site)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=activityst]").css('backgroundColor','red');            
        }

        var desc=$("[id*=activitydc]").val();        
        if (desc.length>255) {
            message+="Longitud inadecuada de descripción\n";
            $("[id*=activitydc]").css('backgroundColor','red');
        }        

        if (reg.test(desc)) {
            message+="Caracteres < > & @ # % ! no permitidos\n";
            $("[id*=activitydc]").css('backgroundColor','red');           
        }

        var dist=$("[id*=activityds]").val();        
        if (isNaN(dist)) {
            message+="Distancia incorrecta\n";
            $("[id*=activityds]").css('backgroundColor','red');
        }             
        
        var slp=$("[id*=activitysl]").val();        
        if (isNaN(slp)) {
            message+="Desnivel incorrecto\n";
            $("[id*=activitysl]").css('backgroundColor','red');
        }               
        
        // mostramos el mensaje si tiene contenido
        if (message.length>0) {
            alert("Se han encontrado los siguientes errores:\n"+message);
            message='';
            return false;
        } else return true;
        
    });
    
    $("[id*=helpac]").click(function(){
        var mess="Para grabar una actividad nueva, primero seleccione un Deporte en el selector.";
        mess+="\nA continuación, deje el selector de actividad en 'Seleccione...'";
        mess+="\nIntroduzca los datos de la actividad a grabar. Esta actividad es un modelo de los entrenamientos que ud. realizará.";
        mess+="\nEs obligatorio poner el nombre y la duración de la actividad (en formato hh:mm:ss)";
        mess+="\n\nPara modificar una actividad, seleccione la actividad con el selector, y realice las modificaciones pertinentes.";
        mess+="\n\nPara eliminar una actividad, seleccione la actividad y pulse en el botón eliminar. No podrá borrar una actividad si tiene eventos realizados";
        mess+="\n\nLa opción eliminar no funciona en modo anonimo.";
        alert(mess);
    });
    
});