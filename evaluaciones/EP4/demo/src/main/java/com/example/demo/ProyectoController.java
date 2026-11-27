package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/cim")
public class ProyectoController {

    @GetMapping("/posts")
    public List<CimRemotePost> listarPosts() {
        return List.of(
            new CimRemotePost(
                1,
                "Innovación Tecnológica en Madera Masiva",
                "Nuevos avances en ingeniería de uniones y diseño sismorresistente para edificios en altura.",
                1
            ),
            new CimRemotePost(
                2,
                "Capacitación en Construcción Sustentable",
                "Programa de formación técnica para profesionales y constructores en sistemas industrializados.",
                1
            ),
            new CimRemotePost(
                3,
                "Transferencia Tecnológica I+D+i",
                "Vinculación con la industria forestal y de la construcción para la adopción de estándares BIM y madera.",
                1
            )
        );
    }

    public record CimRemotePost(int id, String title, String body, Integer userId) {}
}
