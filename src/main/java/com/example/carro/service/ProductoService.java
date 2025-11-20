package com.example.carro.service;
 
import com.example.carro.dto.ProductoRequestDTO;
import com.example.carro.dto.ProductoResponseDTO;
import com.example.carro.model.Producto;
import com.example.carro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class ProductoService {
 
    @Autowired
    private ProductoRepository productoRepository;
 
    public List<ProductoResponseDTO> getAll() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
 
    public ProductoResponseDTO getProductoById(int id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return null;
        }
        return convertToResponseDTO(producto);
    }
 
    public ProductoResponseDTO save(ProductoRequestDTO productoDTO) {
        Producto producto = convertToEntity(productoDTO);
        Producto nuevoProducto = productoRepository.save(producto);
        return convertToResponseDTO(nuevoProducto);
    }
 
    // --- Métodos de conversión (mapeo) ---
 
    private ProductoResponseDTO convertToResponseDTO(Producto producto) {
        return new ProductoResponseDTO(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock());
    }
 
    private Producto convertToEntity(ProductoRequestDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        return producto;
    }
}