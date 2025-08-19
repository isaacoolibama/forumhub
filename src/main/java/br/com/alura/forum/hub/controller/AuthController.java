package br.com.alura.forum.hub.controller;

import br.com.alura.forum.hub.dto.CadastroRequest;
import br.com.alura.forum.hub.dto.LoginRequest;
import br.com.alura.forum.hub.dto.LoginResponse;
import br.com.alura.forum.hub.model.Usuario;
import br.com.alura.forum.hub.model.Perfil;
import br.com.alura.forum.hub.repository.UsuarioRepository;
import br.com.alura.forum.hub.repository.PerfilRepository;
import br.com.alura.forum.hub.service.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PerfilRepository perfilRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/cadastro")
    public ResponseEntity<LoginResponse> cadastrar(@RequestBody @Valid CadastroRequest request) {
        // Verificar se usuário já existe
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Determinar o perfil baseado no tipoPerfil da requisição
        String perfilNome;
        if (request.getTipoPerfil() != null && request.getTipoPerfil() == 2) {
            perfilNome = "ROLE_ADMIN";
        } else {
            perfilNome = "ROLE_USUARIO";
        }
        
        // Buscar perfil
        Optional<Perfil> perfilOpt = perfilRepository.findByNome(perfilNome);
        if (perfilOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.getPerfis().add(perfilOpt.get());
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        // Gerar token JWT
        String token = jwtService.gerarToken(usuarioSalvo);
        
        // Determinar tipo de perfil para resposta
        String tipoPerfil = perfilNome.equals("ROLE_ADMIN") ? "ADMIN" : "USUARIO";
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setEmail(usuarioSalvo.getEmail());
        response.setNome(usuarioSalvo.getNome());
        response.setPerfil(tipoPerfil);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Gerar token JWT
        String token = jwtService.gerarToken(usuario);
        
        // Determinar tipo de perfil baseado nos perfis do usuário
        String tipoPerfil = "USUARIO";
        for (Perfil perfil : usuario.getPerfis()) {
            if (perfil.getNome().equals("ROLE_ADMIN")) {
                tipoPerfil = "ADMIN";
                break;
            }
        }
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setNome(usuario.getNome());
        response.setPerfil(tipoPerfil);
        return ResponseEntity.ok(response);
    }
}
