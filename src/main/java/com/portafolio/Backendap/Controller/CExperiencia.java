package com.portafolio.Backendap.Controller;

import com.portafolio.Backendap.Dto.DtoExperiencia;
import com.portafolio.Backendap.Entity.Experiencia;
import com.portafolio.Backendap.Security.Controller.Mensaje;
import com.portafolio.Backendap.Service.SExperiencia;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/explab")
@CrossOrigin(origins = {"https://portafolio-frontend-ap.web.app/"})
public class CExperiencia {

    @Autowired
    SExperiencia sExperiencia;

    @GetMapping("/lista")
    public ResponseEntity<List<Experiencia>> list() {
        List<Experiencia> list = sExperiencia.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DtoExperiencia dtoexp) {
        if(StringUtils.isBlank(dtoexp.getNombreE())) {
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
//Yo tengo mas de una experiencia en una misma empresa, por lo que si pongo este control, no puedo agregarlas
        //if(sExperiencia.existsByNombreE(dtoexp.getNombreE()))
        //ESTA LINEA ES PARA NO REPETIR LA MISMA EXPERIENCIA LABORAL
            //return new ResponseEntity(new Mensaje("La experiencia laboral ya existe"), HttpStatus.BAD_REQUEST);
        
        Experiencia experiencia = new Experiencia(dtoexp.getNombreE(), dtoexp.getDescripcionE());
        sExperiencia.save(experiencia);

        return new ResponseEntity(new Mensaje("Experiencia laboral agregada"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody DtoExperiencia dtoexp){
        //Validamos si existe el ID
        if(!sExperiencia.existsById(id))
            return new ResponseEntity(new Mensaje("El id no existe"), HttpStatus.BAD_REQUEST);
        
        //if(sExperiencia.existsByNombreE(dtoexp.getNombreE()) && sExperiencia.getByNombreE(dtoexp.getNombreE()).get().getId()!= id)
            //return new ResponseEntity(new Mensaje("Esa experiencia ya existe"), HttpStatus.BAD_REQUEST);
        
        if(StringUtils.isBlank(dtoexp.getNombreE()))
            return new ResponseEntity(new Mensaje("El nombre no puede estar en blanco"), HttpStatus.BAD_REQUEST);
        
        Experiencia experiencia = sExperiencia.getOne(id).get();
        experiencia.setNombreE(dtoexp.getNombreE());
        experiencia.setDescripcionE(dtoexp.getDescripcionE());
        
        sExperiencia.save(experiencia);
            return new ResponseEntity(new Mensaje("Experiencia laboral actualizada"), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        if(!sExperiencia.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.BAD_REQUEST);
        
        sExperiencia.delete(id);
        return new ResponseEntity(new Mensaje("Experiencia eliminada"), HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Experiencia> getById(@PathVariable("id") int id){
        if(!sExperiencia.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        
        Experiencia experiencia = sExperiencia.getOne(id).get();
        return new ResponseEntity(experiencia, HttpStatus.OK);
    }
}