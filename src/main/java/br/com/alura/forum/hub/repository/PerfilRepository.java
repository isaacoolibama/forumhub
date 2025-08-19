package br.com.alura.forum.hub.repository;

import br.com.alura.forum.hub.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    Optional<Perfil> findByNome(String nome);
}
