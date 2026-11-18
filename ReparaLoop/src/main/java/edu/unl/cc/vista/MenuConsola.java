package vista;

import dominio.ComponenteRecuperado;
import dominio.Donante;
import dominio.EquipoDonante;
import dominio.EquipoReceptor;
import dominio.Reparacion;
import dominio.Tecnico;
import negocio.GestorReparaciones;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de texto de ReparaLoop. Lee los datos, llama al gestor y muestra los resultados.
 * Cada dato se vuelve a pedir hasta que sea correcto, sin detener el programa.
 */
public class MenuConsola {

    // Constante: número de la opción para salir
    public static final int SALIR_DEL_SISTEMA = 0;

    private final Scanner scanner;
    private final GestorReparaciones gestorReparaciones;
    private int opcionSeleccionada;

    public MenuConsola() {
        scanner = new Scanner(System.in);
        gestorReparaciones = new GestorReparaciones();
        opcionSeleccionada = -1;
    }

    public static void main(String[] args) {
        MenuConsola menu = new MenuConsola();
        menu.iniciar();
    }

    public void iniciar() {
        do {
            mostrarMenuPrincipal();
            opcionSeleccionada = leerEntradaEntera("Seleccione una opción: ");
            ejecutarOpcion(opcionSeleccionada);
        } while (opcionSeleccionada != SALIR_DEL_SISTEMA);
        scanner.close();
    }

    public void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("========== REPARALOOP ==========");
        System.out.println("1. Registrar donante");
        System.out.println("2. Registrar equipo donante");
        System.out.println("3. Registrar componente recuperado");
        System.out.println("4. Registrar equipo receptor");
        System.out.println("5. Registrar técnico");
        System.out.println("6. Registrar reparación");
        System.out.println("7. Ver componentes compatibles");
        System.out.println("8. Asignar componente a una reparación");
        System.out.println("9. Cerrar reparación");
        System.out.println("10. Consultar trazabilidad");
        System.out.println("11. Historial de un equipo receptor");
        System.out.println("12. Listar registros");
        System.out.println(SALIR_DEL_SISTEMA + ". Salir");
    }

    /**
     * Ejecuta la opción elegida. Las excepciones que no se resuelven volviendo a pedir
     * el dato (por ejemplo, un componente incompatible) se capturan aquí.
     */
    public void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    registrarDonante();
                    break;
                case 2:
                    registrarEquipoDonante();
                    break;
                case 3:
                    registrarComponente();
                    break;
                case 4:
                    registrarEquipoReceptor();
                    break;
                case 5:
                    registrarTecnico();
                    break;
                case 6:
                    registrarReparacion();
                    break;
                case 7:
                    mostrarCompatibles();
                    break;
                case 8:
                    asignarComponente();
                    break;
                case 9:
                    cerrarReparacion();
                    break;
                case 10:
                    consultarTrazabilidad();
                    break;
                case 11:
                    consultarHistorial();
                    break;
                case 12:
                    listarRegistros();
                    break;
                case SALIR_DEL_SISTEMA:
                    mostrarMensaje("Programa finalizado.");
                    break;
                default:
                    mostrarError("Opción no válida. Elija un número del menú.");
            }
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    // ===================== LECTURA DE DATOS =====================

    public String leerEntradaTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    /**
     * Pide un número entero. Si el usuario escribe letras, se captura
     * NumberFormatException y se vuelve a pedir el dato.
     */
    public int leerEntradaEntera(String mensaje) {
        while (true) {
            String texto = leerEntradaTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero. Usted escribió: \"" + texto + "\"");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    // ===================== LECTURA DE DATOS VÁLIDOS =====================
    // Cada método repite la pregunta hasta que el dato sea correcto.

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            String texto = leerEntradaTexto(mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarError("Este dato es obligatorio. Intente de nuevo.");
        }
    }

    private int leerEnteroMinimo(String mensaje, int minimo) {
        while (true) {
            int numero = leerEntradaEntera(mensaje);
            if (numero >= minimo) {
                return numero;
            }
            mostrarError("El valor debe ser mayor o igual a " + minimo + ". Intente de nuevo.");
        }
    }

    /**
     * Pide un número que solo puede ser opcion1 u opcion2 (por ejemplo, 1 o 0).
     */
    private int leerOpcion(String mensaje, int opcion1, int opcion2) {
        while (true) {
            int numero = leerEntradaEntera(mensaje);
            if (numero == opcion1 || numero == opcion2) {
                return numero;
            }
            mostrarError("Solo puede ingresar " + opcion1 + " o " + opcion2 + ". Intente de nuevo.");
        }
    }

    private String leerTelefono() {
        while (true) {
            String texto = leerEntradaTexto("Teléfono (10 dígitos): ");
            try {
                return Donante.validarTelefono(texto);
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage() + " Intente de nuevo.");
            }
        }
    }

    private String leerCorreo() {
        while (true) {
            String texto = leerEntradaTexto("Correo: ");
            try {
                return Donante.validarCorreo(texto);
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage() + " Intente de nuevo.");
            }
        }
    }

    /**
     * Pide un código que todavía no esté registrado (para registrar algo nuevo).
     */
    private String leerCodigoNuevo(String mensaje, String tipo) {
        while (true) {
            String codigo = leerTextoObligatorio(mensaje);
            if (!existeCodigo(tipo, codigo)) {
                return codigo;
            }
            mostrarError("Ya existe un registro de " + tipo + " con el código " + codigo + ". Ingrese otro.");
        }
    }

    /**
     * Pide un código que ya esté registrado (para usar algo que existe).
     */
    private String leerCodigoExistente(String mensaje, String tipo) {
        while (true) {
            String codigo = leerTextoObligatorio(mensaje);
            if (existeCodigo(tipo, codigo)) {
                return codigo;
            }
            mostrarError("No existe un registro de " + tipo + " con el código " + codigo + ". Intente de nuevo.");
        }
    }

    private boolean existeCodigo(String tipo, String codigo) {
        switch (tipo) {
            case "donante":
                return gestorReparaciones.buscarDonante(codigo) != null;
            case "equipo donante":
                return gestorReparaciones.buscarEquipoDonante(codigo) != null;
            case "componente":
                return gestorReparaciones.buscarComponente(codigo) != null;
            case "equipo receptor":
                return gestorReparaciones.buscarEquipoReceptor(codigo) != null;
            case "técnico":
                return gestorReparaciones.buscarTecnico(codigo) != null;
            case "reparación":
                return gestorReparaciones.buscarReparacion(codigo) != null;
            default:
                throw new IllegalArgumentException("Tipo de registro desconocido: " + tipo);
        }
    }

    // ===================== OPCIONES DEL MENÚ =====================

    private void registrarDonante() {
        mostrarMensaje("--- Registrar donante ---");
        String codigo = leerCodigoNuevo("Código: ", "donante");
        String nombre = leerTextoObligatorio("Nombre completo: ");
        String telefono = leerTelefono();
        String correo = leerCorreo();
        String direccion = leerTextoObligatorio("Dirección: ");
        Donante donante = gestorReparaciones.registrarDonante(codigo, nombre, telefono, correo, direccion);
        mostrarMensaje("Donante registrado: " + donante);
        mostrarMensaje("Total de donantes creados: " + Donante.getContadorDonantes());
    }

    private void registrarEquipoDonante() {
        mostrarMensaje("--- Registrar equipo donante ---");
        if (gestorReparaciones.getDonantes().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un donante.");
        }
        String codigoDonante = leerCodigoExistente("Código del donante: ", "donante");
        String codigo = leerCodigoNuevo("Código del equipo: ", "equipo donante");
        String marca = leerTextoObligatorio("Marca: ");
        String modelo = leerTextoObligatorio("Modelo: ");
        String diagnostico = leerTextoObligatorio("Diagnóstico: ");
        EquipoDonante equipo = gestorReparaciones.registrarEquipoDonante(codigoDonante, codigo, marca, modelo, diagnostico);
        mostrarMensaje("Equipo donante registrado: " + equipo);
    }

    private void registrarComponente() {
        mostrarMensaje("--- Registrar componente recuperado ---");
        if (gestorReparaciones.getEquiposDonantes().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un equipo donante.");
        }
        String codigoEquipo = leerCodigoExistente("Código del equipo donante: ", "equipo donante");
        String codigo = leerCodigoNuevo("Código del componente: ", "componente");
        String tipo = leerTextoObligatorio("Tipo (RAM, Disco, Fuente...): ");
        int capacidad = leerEnteroMinimo("Capacidad: ", ComponenteRecuperado.CAPACIDAD_MINIMA);
        String interfaz = leerTextoObligatorio("Interfaz: ");
        int prueba = leerOpcion("¿Aprobó la prueba de funcionamiento? (1 = sí, 0 = no): ", 1, 0);
        boolean aprobo = (prueba == 1);
        ComponenteRecuperado componente = gestorReparaciones.registrarComponente(codigoEquipo, codigo, tipo, capacidad, interfaz, aprobo);
        mostrarMensaje("Componente registrado: " + componente);
    }

    private void registrarEquipoReceptor() {
        mostrarMensaje("--- Registrar equipo receptor ---");
        String codigo = leerCodigoNuevo("Código: ", "equipo receptor");
        String marca = leerTextoObligatorio("Marca: ");
        String modelo = leerTextoObligatorio("Modelo: ");
        String tipo = leerTextoObligatorio("Tipo de componente que necesita: ");
        int capacidadMinima = leerEnteroMinimo("Capacidad mínima requerida: ", ComponenteRecuperado.CAPACIDAD_MINIMA);
        EquipoReceptor receptor = gestorReparaciones.registrarEquipoReceptor(codigo, marca, modelo, tipo, capacidadMinima);
        mostrarMensaje("Equipo receptor registrado: " + receptor);
    }

    private void registrarTecnico() {
        mostrarMensaje("--- Registrar técnico ---");
        String id = leerCodigoNuevo("ID del técnico: ", "técnico");
        String nombre = leerTextoObligatorio("Nombre: ");
        Tecnico tecnico = gestorReparaciones.registrarTecnico(id, nombre);
        mostrarMensaje("Técnico registrado: " + tecnico);
    }

    private void registrarReparacion() {
        mostrarMensaje("--- Registrar reparación ---");
        if (gestorReparaciones.getEquiposReceptores().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un equipo receptor.");
        }
        if (gestorReparaciones.getTecnicos().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un técnico.");
        }
        String codigo = leerCodigoNuevo("Código de la reparación: ", "reparación");
        String codigoReceptor = leerCodigoExistente("Código del equipo receptor: ", "equipo receptor");
        String idTecnico = leerCodigoExistente("ID del técnico responsable: ", "técnico");
        Reparacion reparacion = gestorReparaciones.registrarNuevaReparacion(codigo, codigoReceptor, idTecnico);
        mostrarMensaje("Reparación registrada: " + reparacion);
    }

    private void mostrarCompatibles() {
        mostrarMensaje("--- Componentes compatibles ---");
        if (gestorReparaciones.getReparaciones().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos una reparación.");
        }
        String codigo = leerCodigoExistente("Código de la reparación: ", "reparación");
        List<ComponenteRecuperado> compatibles = gestorReparaciones.buscarCompatibles(codigo);
        if (compatibles.isEmpty()) {
            mostrarMensaje("No hay componentes compatibles y disponibles.");
        } else {
            for (ComponenteRecuperado componente : compatibles) {
                mostrarMensaje(componente.toString());
            }
        }
    }

    private void asignarComponente() {
        mostrarMensaje("--- Asignar componente ---");
        if (gestorReparaciones.getReparaciones().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos una reparación.");
        }
        if (gestorReparaciones.getComponentes().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un componente.");
        }
        String codigoReparacion = leerCodigoExistente("Código de la reparación: ", "reparación");
        String codigoComponente = leerCodigoExistente("Código del componente: ", "componente");
        gestorReparaciones.asignarComponenteAReparacion(codigoReparacion, codigoComponente);
        mostrarMensaje("Componente " + codigoComponente + " asignado a la reparación " + codigoReparacion + ".");
    }

    private void cerrarReparacion() {
        mostrarMensaje("--- Cerrar reparación ---");
        if (gestorReparaciones.getReparaciones().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos una reparación.");
        }
        String codigo = leerCodigoExistente("Código de la reparación: ", "reparación");
        int opcion = leerOpcion("Resultado (1 = exitoso, 2 = fallido): ", 1, 2);
        String resultado;
        if (opcion == 1) {
            resultado = Reparacion.RESULTADO_EXITOSO;
        } else {
            resultado = Reparacion.RESULTADO_FALLIDO;
        }
        gestorReparaciones.cerrarReparacion(codigo, resultado);
        mostrarMensaje("Reparación cerrada con resultado: " + resultado);
        if (opcion == 2) {
            mostrarMensaje("El componente vuelve a estar disponible para otra reparación.");
        }
    }

    private void consultarTrazabilidad() {
        mostrarMensaje("--- Trazabilidad ---");
        if (gestorReparaciones.getReparaciones().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos una reparación.");
        }
        String codigo = leerCodigoExistente("Código de la reparación: ", "reparación");
        mostrarMensaje(gestorReparaciones.consultarTrazabilidad(codigo));
    }

    private void consultarHistorial() {
        mostrarMensaje("--- Historial de un equipo receptor ---");
        if (gestorReparaciones.getEquiposReceptores().isEmpty()) {
            throw new IllegalStateException("Primero debe registrar al menos un equipo receptor.");
        }
        String codigo = leerCodigoExistente("Código del equipo receptor: ", "equipo receptor");
        List<Reparacion> historial = gestorReparaciones.consultarHistorialPorEquipo(codigo);
        if (historial.isEmpty()) {
            mostrarMensaje("El equipo no tiene reparaciones registradas.");
        } else {
            for (Reparacion reparacion : historial) {
                mostrarMensaje(reparacion.toString());
            }
        }
    }

    private void listarRegistros() {
        mostrarMensaje("--- Donantes ---");
        for (Donante donante : gestorReparaciones.getDonantes()) {
            mostrarMensaje(donante.toString());
        }
        mostrarMensaje("--- Equipos donantes ---");
        for (EquipoDonante equipo : gestorReparaciones.getEquiposDonantes()) {
            mostrarMensaje(equipo.toString());
        }
        mostrarMensaje("--- Componentes recuperados ---");
        for (ComponenteRecuperado componente : gestorReparaciones.getComponentes()) {
            mostrarMensaje(componente.toString());
        }
        mostrarMensaje("--- Equipos receptores ---");
        for (EquipoReceptor receptor : gestorReparaciones.getEquiposReceptores()) {
            mostrarMensaje(receptor.toString());
        }
        mostrarMensaje("--- Técnicos ---");
        for (Tecnico tecnico : gestorReparaciones.getTecnicos()) {
            mostrarMensaje(tecnico.toString());
        }
        mostrarMensaje("--- Reparaciones ---");
        for (Reparacion reparacion : gestorReparaciones.getReparaciones()) {
            mostrarMensaje(reparacion.toString());
        }
    }
}
