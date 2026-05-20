package com.fadergs.salalilas.backend.user.repository;

import com.fadergs.salalilas.backend.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findAllByOrderByNomeAsc();

    @Query("""
        SELECT u FROM Usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :busca, '%'))
        ORDER BY u.nome ASC
    """)
    List<Usuario> findByNomeOrEmailContaining(@Param("busca") String busca);
}
