package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/cim")
public class ProyectoController {

    @GetMapping("/proyectos")
    public List<String> listarProyectos() {
        return List.of(
            "Edificio Sustentable en CLT - CIM UC",
            "Investigación de Madera Masiva",
            "Laboratorio de Ensayos y Certificación"
        );
    }

    @GetMapping("/posts")
    public List<CimRemotePost> listarPosts() {
        return List.of(
            new CimRemotePost(
                1,
                "Edificio Sustentable en CLT - CIM UC",
                "Diseño y construcción de un edificio sustentable utilizando madera contralaminada (CLT).",
                1
            ),
            new CimRemotePost(
                2,
                "Investigación de Madera Masiva",
                "Investigación aplicada a soluciones estructurales y constructivas en madera masiva.",
                1
            ),
            new CimRemotePost(
                3,
                "Laboratorio de Ensayos y Certificación",
                "Ensayos y certificación de productos y sistemas constructivos de madera.",
                1
            )
        );
    }

    public record CimRemotePost(int id, String title, String body, Integer userId) {}
}