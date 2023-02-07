package com.rodrigocompany.financas.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table( name="usuario" , schema = "financas")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Data equivalente ao @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @ToString , @EqualsAndHashCode
public class Usuario {

    @Id
    @Column(name="id")
    @GeneratedValue( strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    @Column(name="nome")
    private String nome;
    @Column(name="email")
    private String email;

    @Column(name="senha")
    private String senha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Usuario usuario = (Usuario) o;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
