package br.com.alura.forum.hub.controller;

import br.com.alura.forum.hub.dto.ApiResponse;
import br.com.alura.forum.hub.model.Curso;
import br.com.alura.forum.hub.model.Topico;
import br.com.alura.forum.hub.model.Usuario;
import br.com.alura.forum.hub.repository.CursoRepository;
import br.com.alura.forum.hub.repository.TopicoRepository;
import br.com.alura.forum.hub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    
    @Autowired
    private TopicoRepository topicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> criarTopico(
            @RequestBody Map<String, Object> request, 
            HttpServletRequest httpRequest) {
        
        // Verificar autenticação de forma mais rigorosa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || 
            "anonymousUser".equals(authentication.getName()) ||
            !authentication.isAuthenticated() ||
            authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return ResponseEntity.status(401).body(
                ApiResponse.error("Você precisa estar autenticado para criar tópicos", httpRequest.getRequestURI())
            );
        }
        
        try {
            // Buscar o usuário autenticado no banco
            String emailUsuario = authentication.getName();
            
            // Buscar o usuário real do banco pelo email
            Usuario usuarioAutenticado = usuarioRepository.findByEmail(emailUsuario).orElse(null);
            if (usuarioAutenticado == null) {
                return ResponseEntity.status(400).body(
                    ApiResponse.error("Usuário autenticado não encontrado no banco", httpRequest.getRequestURI())
                );
            }
            
            // Criar novo tópico
            Topico novoTopico = new Topico();
            novoTopico.setTitulo((String) request.get("titulo"));
            novoTopico.setMensagem((String) request.get("mensagem"));
            novoTopico.setStatus(Topico.StatusTopico.ABERTO);
            novoTopico.setDataCriacao(java.time.LocalDateTime.now());
            
            // Usar o usuário autenticado real
            novoTopico.setAutor(usuarioAutenticado);
            
            // Buscar o curso real do banco
            Long cursoId = ((Number) request.get("cursoId")).longValue();
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso == null) {
                return ResponseEntity.status(400).body(
                    ApiResponse.error("Curso com ID " + cursoId + " não encontrado", httpRequest.getRequestURI())
                );
            }
            novoTopico.setCurso(curso);
            
            // Salvar no banco
            Topico topicoSalvo = topicoRepository.save(novoTopico);
            
            // Buscar novamente para pegar os dados completos
            topicoSalvo = topicoRepository.findByIdWithAutorAndCurso(topicoSalvo.getId());
            
            // Preparar resposta
            Map<String, Object> topicoData = new HashMap<>();
            topicoData.put("id", topicoSalvo.getId());
            topicoData.put("titulo", topicoSalvo.getTitulo());
            topicoData.put("mensagem", topicoSalvo.getMensagem());
            topicoData.put("status", topicoSalvo.getStatus());
            topicoData.put("dataCriacao", topicoSalvo.getDataCriacao());
            topicoData.put("autor", topicoSalvo.getAutor().getNome());
            topicoData.put("curso", topicoSalvo.getCurso().getNome());
            
            return ResponseEntity.status(201).body(
                ApiResponse.success("Tópico criado com sucesso", topicoData, httpRequest.getRequestURI())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error("Erro interno ao criar tópico: " + e.getMessage(), httpRequest.getRequestURI())
            );
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> atualizarTopico(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        
        // Verificar autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).body(
                ApiResponse.error("Você precisa estar autenticado para atualizar tópicos", httpRequest.getRequestURI())
            );
        }
        
        try {
            // Buscar o tópico no banco
            Topico topico = topicoRepository.findByIdWithAutorAndCurso(id);
            if (topico == null) {
                return ResponseEntity.status(404).body(
                    ApiResponse.error("Tópico com ID " + id + " não encontrado", httpRequest.getRequestURI())
                );
            }
            
            // Verificar se o usuário é o autor do tópico ou admin
            String usuarioAtual = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            boolean isAutor = topico.getAutor().getEmail().equals(usuarioAtual);
            
            if (!isAdmin && !isAutor) {
                return ResponseEntity.status(403).body(
                    ApiResponse.error("Você não tem permissão para atualizar este tópico. Apenas o autor ou administradores podem fazer alterações.", httpRequest.getRequestURI())
                );
            }
            
            // Atualizar os dados do tópico
            topico.setTitulo((String) request.get("titulo"));
            topico.setMensagem((String) request.get("mensagem"));
            
            // Salvar no banco
            Topico topicoAtualizado = topicoRepository.save(topico);
            
            // Buscar novamente para pegar os dados atualizados
            topicoAtualizado = topicoRepository.findByIdWithAutorAndCurso(id);
            
            // Preparar resposta
            Map<String, Object> topicoData = new HashMap<>();
            topicoData.put("id", topicoAtualizado.getId());
            topicoData.put("titulo", topicoAtualizado.getTitulo());
            topicoData.put("mensagem", topicoAtualizado.getMensagem());
            topicoData.put("status", topicoAtualizado.getStatus());
            topicoData.put("dataCriacao", topicoAtualizado.getDataCriacao());
            topicoData.put("autor", topicoAtualizado.getAutor().getNome());
            topicoData.put("curso", topicoAtualizado.getCurso().getNome());
            
            return ResponseEntity.ok(
                ApiResponse.success("Tópico atualizado com sucesso", topicoData, httpRequest.getRequestURI())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error("Erro interno ao atualizar tópico: " + e.getMessage(), httpRequest.getRequestURI())
            );
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletarTopico(
            @PathVariable Long id, 
            HttpServletRequest httpRequest) {
        
        // Verificar autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).body(
                ApiResponse.error("Você precisa estar autenticado para deletar tópicos", httpRequest.getRequestURI())
            );
        }
        
        try {
            // Buscar o tópico no banco
            Topico topico = topicoRepository.findByIdWithAutorAndCurso(id);
            if (topico == null) {
                return ResponseEntity.status(404).body(
                    ApiResponse.error("Tópico com ID " + id + " não encontrado", httpRequest.getRequestURI())
                );
            }
            
            // Verificar se o usuário é o autor do tópico ou admin
            String usuarioAtual = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            boolean isAutor = topico.getAutor().getEmail().equals(usuarioAtual);
            
            if (!isAdmin && !isAutor) {
                return ResponseEntity.status(403).body(
                    ApiResponse.error("Você não tem permissão para deletar este tópico. Apenas o autor ou administradores podem fazer alterações.", httpRequest.getRequestURI())
                );
            }
            
            // Deletar do banco
            topicoRepository.deleteById(id);
            
            return ResponseEntity.ok(
                ApiResponse.success("Tópico com ID " + id + " foi deletado com sucesso", httpRequest.getRequestURI())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error("Erro interno ao deletar tópico: " + e.getMessage(), httpRequest.getRequestURI())
            );
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> listarTopicos(HttpServletRequest httpRequest) {
        try {
            // Buscar tópicos do banco com autor e curso
            List<Topico> topicos = topicoRepository.findAllWithAutorAndCurso();
            
            // Converter para formato de resposta
            List<Map<String, Object>> topicosResponse = topicos.stream()
                .map(topico -> {
                    Map<String, Object> topicoMap = new HashMap<>();
                    topicoMap.put("id", topico.getId());
                    topicoMap.put("titulo", topico.getTitulo());
                    topicoMap.put("mensagem", topico.getMensagem());
                    topicoMap.put("status", topico.getStatus());
                    topicoMap.put("dataCriacao", topico.getDataCriacao());
                    topicoMap.put("autor", topico.getAutor().getNome());
                    topicoMap.put("curso", topico.getCurso().getNome());
                    return topicoMap;
                })
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", topicosResponse);
            response.put("totalElements", topicos.size());
            response.put("totalPages", 1);
            response.put("size", topicos.size());
            response.put("number", 0);
            
            return ResponseEntity.ok(
                ApiResponse.success("Tópicos listados com sucesso", response, httpRequest.getRequestURI())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error("Erro interno ao listar tópicos: " + e.getMessage(), httpRequest.getRequestURI())
            );
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> buscarTopico(
            @PathVariable Long id, 
            HttpServletRequest httpRequest) {
        try {
            Topico topico = topicoRepository.findByIdWithAutorAndCurso(id);
            
            if (topico == null) {
                return ResponseEntity.status(404).body(
                    ApiResponse.error("Tópico com ID " + id + " não encontrado", httpRequest.getRequestURI())
                );
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", topico.getId());
            response.put("titulo", topico.getTitulo());
            response.put("mensagem", topico.getMensagem());
            response.put("status", topico.getStatus());
            response.put("dataCriacao", topico.getDataCriacao());
            response.put("autor", topico.getAutor().getNome());
            response.put("curso", topico.getCurso().getNome());
            
            return ResponseEntity.ok(
                ApiResponse.success("Tópico encontrado com sucesso", response, httpRequest.getRequestURI())
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error("Erro interno ao buscar tópico: " + e.getMessage(), httpRequest.getRequestURI())
            );
        }
    }
}
