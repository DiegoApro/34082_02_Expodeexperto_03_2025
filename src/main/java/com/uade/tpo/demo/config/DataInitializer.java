package com.uade.tpo.demo.config;  
import com.uade.tpo.demo.entity.*;
import com.uade.tpo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EquipoRepository equipoRepository;
    private final CategoryRepository categoriaRepository;
    private final TalleRepository talleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear equipos de Primera Nacional (CABA)
        if (equipoRepository.count() == 0) {
            String[][] equipos = {
                {"Atlanta", "Primera Nacional", "Argentina"},
                {"Barracas Central", "Primera Nacional", "Argentina"},
                {"Defensores de Belgrano", "Primera Nacional", "Argentina"},
                {"Deportivo Riestra", "Primera Nacional", "Argentina"},
                {"Sacachispas", "Primera Nacional", "Argentina"},
                {"Tristán Suárez", "Primera Nacional", "Argentina"},
                {"Almagro", "Primera Nacional", "Argentina"},
                {"Comunicaciones", "Primera Nacional", "Argentina"}
            };
            
            for (String[] equipo : equipos) {
                Equipo e = new Equipo();
                e.setNombre(equipo[0]);
                e.setLiga(equipo[1]);
                e.setPais(equipo[2]);
                equipoRepository.save(e);
            }
            System.out.println("✅ Equipos creados correctamente");
        }
        
        // Crear categorías
        if (categoriaRepository.count() == 0) {
            String[] categorias = {"Remera Titular", "Remera Suplente", "Remera Alternativa", "Campera", "Short", "Medias"};
            for (String cat : categorias) {
                Categoria c = new Categoria();
                c.setNombre(cat);
                categoriaRepository.save(c);
            }
            System.out.println("✅ Categorías creadas correctamente");
        }
        
        // Crear talles
        if (talleRepository.count() == 0) {
            String[] talles = {"S", "M", "L", "XL", "XXL"};
            for (String t : talles) {
                Talle talle = new Talle();
                talle.setNombre(t);
                talleRepository.save(talle);
            }
            System.out.println("✅ Talles creados correctamente");
        }
        
        // Crear usuario admin (vendedor)
        if (userRepository.findByEmail("admin@marketplace.com").isEmpty()) {
            User admin = User.builder()
                .nombre("Admin Vendedor")
                .email("admin@marketplace.com")
                .password(passwordEncoder.encode("admin123"))
                .direccion("Calle Falsa 123")
                .telefono("123456789")
                .role(Role.ADMIN)
                .build();
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado correctamente");
        }
    }
}