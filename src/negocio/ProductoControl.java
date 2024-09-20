package negocio;

import datos.ProductoDAO;
import entidades.Productos;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class ProductoControl {

    // Variables
    private final ProductoDAO DATOSPROD;
    private final Productos obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados = 0;

    public ProductoControl() {
        this.DATOSPROD = new ProductoDAO();
        this.obj = new Productos();
        this.registrosMostrados = 0;
    }

    // Método para seleccionar productos
    public DefaultComboBoxModel seleccionar() {
    DefaultComboBoxModel items = new DefaultComboBoxModel();
    List<Productos> lista = new ArrayList();
    lista = DATOSPROD.seleccionar();
    
    for (Productos item : lista) {
        items.addElement(item.getNombre_producto());
    }
    
    return items;
}

    // Métodos para el giro de negocio (listar productos)
    public DefaultTableModel listar(String texto) {
        List<Productos> lista = new ArrayList();
        lista.addAll(DATOSPROD.listar(texto));

        String[] titulos = {"IdProducto", "Categoría", "Nombre", "Descripción", "Imagen", "Marca", "Cantidad", "Fecha Vencimiento", "Precio Compra", "Condición"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String estado;
        String[] registro = new String[11];

        this.registrosMostrados = 0;

        for (Productos item : lista) {
            if (item.isCondicion()) {
                estado = "Activo";
            } else {
                estado = "Inactivo";
            }
            registro[0] = Integer.toString(item.getIdproducto());
            registro[1] = Integer.toString(item.getCategoria_id());
            registro[2] = item.getNombre_producto();
            registro[3] = item.getDescripcion_producto();
            registro[4] = item.getImagen_producto();
            registro[5] = item.getMarca_producto();
            registro[6] = Integer.toString(item.getCantidad_producto());
            registro[7] = item.getFecha_vencimiento();
            registro[8] = Double.toString(item.getPrecio_compra());
            registro[9] = estado;
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }

    // Método para registrar producto
    public String insertar(int categoria_id, String nombre_producto, String descripcion_producto, String imagen_producto, String codigo_producto, String marca_producto, int cantidad_producto, String fecha_vencimiento, double precio_compra, boolean condicion) {
        if (DATOSPROD.existe(codigo_producto)) {
            return "El código del producto ya existe en nuestra BD";
        } else {
            obj.setCategoria_id(categoria_id);
            obj.setNombre_producto(nombre_producto);
            obj.setDescripcion_producto(descripcion_producto);
            obj.setImagen_producto(imagen_producto);
            obj.setMarca_producto(marca_producto);
            obj.setCantidad_producto(cantidad_producto);
            obj.setFecha_vencimiento(fecha_vencimiento);
            obj.setPrecio_compra(precio_compra);
            obj.setCondicion(condicion);

            if (DATOSPROD.insertar(obj)) {
                return "OK";
            } else {
                return "Error al registrar el producto";
            }
        }
    }

    // Método para actualizar los datos del producto
    public String actualizar(int idproducto, int categoria_id, String nombre_producto, String descripcion_producto, String imagen_producto, String codigo_producto, String marca_producto, int cantidad_producto, String fecha_vencimiento, double precio_compra, boolean condicion) {
        if (DATOSPROD.existe(codigo_producto)) {
            obj.setIdproducto(idproducto);
            obj.setCategoria_id(categoria_id);
            obj.setNombre_producto(nombre_producto);
            obj.setDescripcion_producto(descripcion_producto);
            obj.setImagen_producto(imagen_producto);
            obj.setMarca_producto(marca_producto);
            obj.setCantidad_producto(cantidad_producto);
            obj.setFecha_vencimiento(fecha_vencimiento);
            obj.setPrecio_compra(precio_compra);
            obj.setCondicion(condicion);

            if (DATOSPROD.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en la actualización";
            }
        } else {
            return "El código del producto ya existe";
        }
    }

    // Método para retornar el total de registros
    public int total() {
        return DATOSPROD.total();
    }

    public int totalMostrados() {
        return this.registrosMostrados;
    }

    // Método para desactivar producto
    public String desactivar(int idproducto) {
        if (DATOSPROD.desactivar(idproducto)) {
            return "OK";
        } else {
            return "No se puede desactivar el producto";
        }
    }
    
    // Método para activar producto
    public String activar(int idproducto) {
        if (DATOSPROD.activar(idproducto)) {
            return "OK";
        } else {
            return "No se puede activar el producto";
        }
    }
}
