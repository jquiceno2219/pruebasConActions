package co.edu.cue.pruebasConActions.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InicioController {

@GetMapping("/")
    public String mostrarMensaje() {
            return "Hola mundo";
    }
}