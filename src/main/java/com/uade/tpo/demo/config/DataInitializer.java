package com.uade.tpo.demo.config;

import com.uade.tpo.demo.entity.*;
import com.uade.tpo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EquipoRepository equipoRepository;
    private final CategoryRepository categoriaRepository;
    private final TalleRepository talleRepository;
    private final UserRepository userRepository;
    private final DescuentoRepository descuentoRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        
        // ============================================
        // 1. CREAR EQUIPOS (Primera Nacional - CABA)
        // ============================================
        if (equipoRepository.count() == 0) {
            String[][] equipos = {
                {"River", "Primera", "Argentina"},
                {"Boca", "Primera", "Argentina"},
                {"Barracas Central", "Primera", "Argentina"},
                {"Deportivo Riestra", "Primera", "Argentina"},
                {"San Lorenzo", "Primera", "Argentina"},
                {"Huracan", "Primera", "Argentina"},
                {"Velez Sarfield", "Primera", "Argentina"},
                {"Argentinos Juniors", "Primera", "Argentina"}
            };
            
            for (String[] equipo : equipos) {
                Equipo e = new Equipo();
                e.setNombre(equipo[0]);
                e.setLiga(equipo[1]);
                e.setPais(equipo[2]);
                equipoRepository.save(e);
            }
            System.out.println("✅ " + equipos.length + " equipos creados correctamente");
        }
        
        // ============================================
        // 2. CREAR CATEGORÍAS
        // ============================================
        if (categoriaRepository.count() == 0) {
            String[] categorias = {
                "Remera Titular", 
                "Remera Suplente", 
                "Talle",
                "Retro",
                "Versión Fan",
                "Versión Jugador"
            };
            for (String cat : categorias) {
                Categoria c = new Categoria();
                c.setNombre(cat);
                categoriaRepository.save(c);
            }
            System.out.println("✅ " + categorias.length + " categorías creadas correctamente");
        }
        
        // ============================================
        // 3. CREAR TALLES
        // ============================================
        if (talleRepository.count() == 0) {
            String[] talles = {"S", "M", "L", "XL", "XXL"};
            for (String t : talles) {
                Talle talle = new Talle();
                talle.setNombre(t);
                talleRepository.save(talle);
            }
            System.out.println("✅ " + talles.length + " talles creados correctamente");
        }
        
        // ============================================
        // 4. CREAR USUARIO ADMIN (VENDEDOR)
        // ============================================
        if (userRepository.findByEmail("admin@marketplace.com").isEmpty()) {
            User admin = User.builder()
                .nombre("Admin Vendedor")
                .email("admin@marketplace.com")
                .password(passwordEncoder.encode("admin123"))
                .direccion("Av. Libertador 1234, CABA")
                .telefono("11-1234-5678")
                .role(Role.ADMIN)
                .build();
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado: admin@marketplace.com / admin123");
        }
        
        // ============================================
        // 5. CREAR USUARIO DE PRUEBA (COMPRADOR)
        // ============================================
        if (userRepository.findByEmail("cliente@test.com").isEmpty()) {
            User cliente = User.builder()
                .nombre("Cliente Test")
                .email("cliente@test.com")
                .password(passwordEncoder.encode("cliente123"))
                .direccion("Av. Corrientes 456, CABA")
                .telefono("11-8765-4321")
                .role(Role.USER)
                .build();
            userRepository.save(cliente);
            System.out.println("✅ Usuario cliente creado: cliente@test.com / cliente123");
        }
        
        // ============================================
        // 6. CREAR DESCUENTOS
        // ============================================
        if (descuentoRepository.count() == 0) {
            
            // Descuento general activo (10% de descuento)
            Descuento descuentoGeneral = new Descuento();
            descuentoGeneral.setNombre("Descuento General");
            descuentoGeneral.setPorcentaje(10.0);
            descuentoGeneral.setActivo(true);
            descuentoGeneral.setDescripcion("10% de descuento en toda la tienda");
            descuentoGeneral.setCodigo(null);
            descuentoGeneral.setFechaInicio(null);
            descuentoGeneral.setFechaFin(null);
            descuentoRepository.save(descuentoGeneral);
            
            // Descuento por código (15% de descuento)
            Descuento descuentoCodigo = new Descuento();
            descuentoCodigo.setNombre("Código Bienvenida");
            descuentoCodigo.setPorcentaje(15.0);
            descuentoCodigo.setCodigo("BIENVENIDA15");
            descuentoCodigo.setFechaInicio(LocalDateTime.now());
            descuentoCodigo.setFechaFin(LocalDateTime.now().plusMonths(1));
            descuentoCodigo.setActivo(true);
            descuentoCodigo.setDescripcion("15% de descuento para nuevos usuarios");
            descuentoRepository.save(descuentoCodigo);
            
            // Descuento por temporada (20% de descuento - comienza en 1 mes)
            Descuento descuentoTemporada = new Descuento();
            descuentoTemporada.setNombre("Descuento Temporada");
            descuentoTemporada.setPorcentaje(20.0);
            descuentoTemporada.setCodigo("TEMPORADA20");
            descuentoTemporada.setFechaInicio(LocalDateTime.now().plusMonths(1));
            descuentoTemporada.setFechaFin(LocalDateTime.now().plusMonths(2));
            descuentoTemporada.setActivo(false);  // Inactivo hasta la fecha
            descuentoTemporada.setDescripcion("20% de descuento en temporada alta");
            descuentoRepository.save(descuentoTemporada);
            
            System.out.println("✅ " + descuentoRepository.count() + " descuentos creados correctamente");
        }
    
        
        // ============================================
        // 8. RESUMEN FINAL
        // ============================================
        System.out.println("\n========== ¡DATOS INICIALIZADOS CORRECTAMENTE! ==========");
        System.out.println("📊 Equipos: " + equipoRepository.count());
        System.out.println("📂 Categorías: " + categoriaRepository.count());
        System.out.println("👕 Talles: " + talleRepository.count());
        System.out.println("👤 Usuarios: " + userRepository.count());
        System.out.println("🏷️ Descuentos: " + descuentoRepository.count());
        System.out.println("=======================================================\n");
    }
}