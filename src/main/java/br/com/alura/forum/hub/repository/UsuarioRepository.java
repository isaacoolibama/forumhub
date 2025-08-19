package br.com.alura.forum.hub.repository;

import br.com.alura.forum.hub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByNome(String nome);
    
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE p.nome = :nomePerfil")
    Optional<Usuario> findByPerfilNome(@Param("nomePerfil") String nomePerfil);
    
    boolean existsByEmail(String email);
}
