package negocio;

import dominio.ComponenteRecuperado;
import dominio.Donante;
import dominio.EquipoDonante;
import dominio.EquipoReceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda en memoria los registros de ReparaLoop y coordina las operaciones.
 * Cada lista representa una relación 0..* con una clase del dominio.
 *
 * Versión actual: RF01, RF02, RF03 y RF04.
 * Para agregar una nueva clase (por ejemplo, Tecnico o Reparacion en RF05) se agrega su lista,
 * su método registrar y su método buscar, sin modificar lo que ya existe.
 */
public class GestorReparaciones {

    private final List<Donante> donantes;
    private final List<EquipoDonante> equiposDonantes;
    private final List<ComponenteRecuperado> componentes;
    private final List<EquipoReceptor> equiposReceptores;
    private final GestorCompatibilidad gestorCompatibilidad;

    public GestorReparaciones() {
        donantes = new ArrayList<>();
        equiposDonantes = new ArrayList<>();
        componentes = new ArrayList<>();
        equiposReceptores = new ArrayList<>();
        gestorCompatibilidad = new GestorCompatibilidad();
    }

    // ===================== RF01: donantes =====================

    public Donante registrarDonante(String codigo, String nombreCompleto, String telefono,
                                    String correo, String direccion) {
        if (buscarDonante(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un donante con el código " + codigo + ".");
        }
        Donante donante = new Donante(codigo, nombreCompleto, telefono, correo, direccion);
        donantes.add(donante);
        return donante;
    }

    // ===================== RF02: equipos donados =====================

    public EquipoDonante registrarEquipoDonante(String codigoDonante, String codigo, String marca,
                                                String modelo, String diagnostico) {
        Donante donante = buscarDonante(codigoDonante);
        if (donante == null) {
            throw new IllegalArgumentException("No existe un donante con el código " + codigoDonante + ".");
        }
        if (buscarEquipoDonante(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo donante con el código " + codigo + ".");
        }
        EquipoDonante equipo = new EquipoDonante(codigo, marca, modelo, diagnostico, donante);
        donante.agregarEquipo(equipo);
        equiposDonantes.add(equipo);
        return equipo;
    }

    // ===================== RF03: componentes recuperados =====================

    public ComponenteRecuperado registrarComponente(String codigoEquipoDonante, String codigo, String tipo,
                                                    int capacidad, String interfaz, boolean estadoPrueba) {
        EquipoDonante equipo = buscarEquipoDonante(codigoEquipoDonante);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo donante con el código " + codigoEquipoDonante + ".");
        }
        if (buscarComponente(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un componente con el código " + codigo + ".");
        }
        ComponenteRecuperado componente = new ComponenteRecuperado(codigo, tipo, capacidad, interfaz, estadoPrueba, equipo);
        equipo.agregarComponente(componente);
        componentes.add(componente);
        return componente;
    }

    // ===================== RF04: equipos receptores y compatibilidad =====================

    public EquipoReceptor registrarEquipoReceptor(String codigo, String marca, String modelo,
                                                  String tipoRequerido, int capacidadMinima) {
        if (buscarEquipoReceptor(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo receptor con el código " + codigo + ".");
        }
        EquipoReceptor receptor = new EquipoReceptor(codigo, marca, modelo, tipoRequerido, capacidadMinima);
        equiposReceptores.add(receptor);
        return receptor;
    }

    public List<ComponenteRecuperado> buscarCompatibles(String codigoReceptor) {
        EquipoReceptor receptor = buscarEquipoReceptor(codigoReceptor);
        if (receptor == null) {
            throw new IllegalArgumentException("No existe un equipo receptor con el código " + codigoReceptor + ".");
        }
        return gestorCompatibilidad.filtrarCompatibles(componentes, receptor);
    }

    // ===================== BÚSQUEDAS =====================
    // Cada búsqueda devuelve el objeto encontrado o null si no existe.
    // Los códigos se comparan sin importar mayúsculas o minúsculas.

    public Donante buscarDonante(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Donante donante : donantes) {
            if (donante.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return donante;
            }
        }
        return null;
    }

    public EquipoDonante buscarEquipoDonante(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (EquipoDonante equipo : equiposDonantes) {
            if (equipo.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return equipo;
            }
        }
        return null;
    }

    public ComponenteRecuperado buscarComponente(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (ComponenteRecuperado componente : componentes) {
            if (componente.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return componente;
            }
        }
        return null;
    }

    public EquipoReceptor buscarEquipoReceptor(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (EquipoReceptor receptor : equiposReceptores) {
            if (receptor.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return receptor;
            }
        }
        return null;
    }

    // ===================== LISTAS (copias) =====================

    public List<Donante> getDonantes() {
        return new ArrayList<>(donantes);
    }

    public List<EquipoDonante> getEquiposDonantes() {
        return new ArrayList<>(equiposDonantes);
    }

    public List<ComponenteRecuperado> getComponentes() {
        return new ArrayList<>(componentes);
    }

    public List<EquipoReceptor> getEquiposReceptores() {
        return new ArrayList<>(equiposReceptores);
    }
}
