/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.taller7prod.service;

import com.example.taller7prod.model.Producto;
import com.example.taller7prod.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sise
 */
@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository repository;
    
    public List<Producto> ListarTodas(){
         return repository.findAll();
    }
    
     public void guardar(Producto producto){
        repository.save(producto);
        
        
    }
     
      /***
     *FUNCION PARA  BUSCAR REGISTRO EMPLEADO POR ID
     *@param id
     *@return
     */
     public Optional<Producto> buscarPorId(Long id){
        return repository.findById(id);
    }
       public void eliminar(Long id) {
        repository.deleteById(id);
    }

     
}


