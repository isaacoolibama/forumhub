package br.com.alura.forum.hub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class CadastroRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
    
    @Min(value = 1, message = "Tipo de perfil deve ser 1 (USUARIO) ou 2 (ADMIN)")
    @Max(value = 2, message = "Tipo de perfil deve ser 1 (USUARIO) ou 2 (ADMIN)")
    private Integer tipoPerfil = 1; // 1 = ROLE_USUARIO, 2 = ROLE_ADMIN (padrão: usuário comum)
}
