package datos;

import database.Conexion;
import datos.CrudInterface.ProductoInterface;
import entidades.Productos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO implements ProductoInterface<Productos> {
    // Variables
    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;
    
    // Constructor
    public ProductoDAO() {
        CON = Conexion.getInstancia();
    }

    /* ============================== L I S T A R ============================== */
    @Override
    public List<Productos> listar(String texto) {
        List<Productos> registros = new ArrayList<>();
        try {
            ps = CON.conectar().prepareStatement(
                "SELECT * FROM productos WHERE nombre_producto LIKE ?"
            );
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Productos(
                        rs.getInt("idproducto"),
                        rs.getInt("categoria_id"),
                        rs.getString("nombre_producto"),
                        rs.getString("descripcion_producto"),
                        rs.getString("imagen_producto"),
                        rs.getString("marca_producto"),
                        rs.getInt("cantidad_producto"),
                        rs.getString("fecha_vencimiento"),
                        rs.getDouble("precio_compra"),
                        rs.getBoolean("condicion")
                ));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pueden mostrar los datos: " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros;
    }

    /* ============================== I N S E R T A R ============================== */
    @Override
    public boolean insertar(Productos obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement(
                "INSERT INTO productos (categoria_id, nombre_producto, descripcion_producto, imagen_producto, " +
                "marca_producto, cantidad_producto, fecha_vencimiento, precio_compra, condicion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)"
            );
            ps.setInt(1, obj.getCategoria_id());
            ps.setString(2, obj.getNombre_producto());
            ps.setString(3, obj.getDescripcion_producto());
            ps.setString(4, obj.getImagen_producto());
            ps.setString(5, obj.getMarca_producto());
            ps.setInt(6, obj.getCantidad_producto());
            ps.setString(7, obj.getFecha_vencimiento());
            ps.setDouble(8, obj.getPrecio_compra());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el producto: " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    /* ============================== A C T U A L I Z A R ============================== */
    @Override
    public boolean actualizar(Productos obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement(
                "UPDATE productos SET categoria_id = ?, nombre_producto = ?, descripcion_producto = ?, imagen_producto = ?, " +
                "marca_producto = ?, cantidad_producto = ?, fecha_vencimiento = ?, precio_compra = ?, condicion = ? " +
                "WHERE idproducto = ?"
            );
            ps.setInt(1, obj.getCategoria_id());
            ps.setString(2, obj.getNombre_producto());
            ps.setString(3, obj.getDescripcion_producto());
            ps.setString(4, obj.getImagen_producto());
            ps.setString(5, obj.getMarca_producto());
            ps.setInt(6, obj.getCantidad_producto());
            ps.setString(7, obj.getFecha_vencimiento());
            ps.setDouble(8, obj.getPrecio_compra());
            ps.setBoolean(9, obj.isCondicion());
            ps.setInt(10, obj.getIdproducto());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pueden actualizar los datos: " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    /* ============================== D E S A C T I V A R ============================== */
    @Override
    public boolean desactivar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement(
                "UPDATE productos SET condicion = 0 WHERE idproducto = ?"
            );
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede desactivar el producto: " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    /* ============================== A C T I V A R ============================== */
    @Override
    public boolean activar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement(
                "UPDATE productos SET condicion = 1 WHERE idproducto = ?"
            );
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede activar el producto: " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    /* ============================== T O T A L ============================== */
    @Override
    public int total() {
        int totalRegistros = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT COUNT(idproducto) FROM productos");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalRegistros = rs.getInt(1);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede obtener el total de productos: " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return totalRegistros;
    }

    /* ============================== E X I S T E ============================== */
    @Override
    public boolean existe(String texto) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement(
                "SELECT nombre_producto FROM productos WHERE nombre_producto = ?"
            );
            ps.setString(1, texto);
            rs = ps.executeQuery();
            if (rs.next()) {
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede validar los datos: " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return resp;
    }

    /* ============================== S E L E C C I O N A R ============================== */
    public List<Productos> seleccionar() {
        List<Productos> registros = new ArrayList<>();
        try {
            ps = CON.conectar().prepareStatement(
                "SELECT idproducto, categoria_id, nombre_producto, descripcion_producto, imagen_producto, " +
                "marca_producto, cantidad_producto, fecha_vencimiento, precio_compra, condicion " +
                "FROM productos ORDER BY nombre_producto ASC"
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Productos(
                    rs.getInt("idproducto"),
                    rs.getInt("categoria_id"),
                    rs.getString("nombre_producto"),
                    rs.getString("descripcion_producto"),
                    rs.getString("imagen_producto"),
                    rs.getString("marca_producto"),
                    rs.getInt("cantidad_producto"),
                    rs.getString("fecha_vencimiento"),
                    rs.getDouble("precio_compra"),
                    rs.getBoolean("condicion")
                ));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pueden cargar los productos: " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros;
    }
}