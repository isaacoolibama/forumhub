package br.com.alura.forum.hub.repository;

import br.com.alura.forum.hub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso ORDER BY t.dataCriacao DESC")
    List<Topico> findAllWithAutorAndCurso();
    
    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso WHERE t.id = :id")
    Topico findByIdWithAutorAndCurso(@Param("id") Long id);
}
