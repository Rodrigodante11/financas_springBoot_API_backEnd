package com.rodrigocompany.financas.model.entity;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="lancamento" , schema = "financas")
public class Lancamento {

    @Id
    @Column(name="id")
    @GeneratedValue( strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    @Column(name="mes")
    private Integer mes;

    @Column(name="ano")
    private Integer ano;

    @ManyToOne   // muitos lancamentos para 1 usuario
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @Column(name="valor")
    private BigDecimal valor;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") outro jeito de coneverter a data em um formato string do json
    @Column(name="data_cadastro")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class) // convertendo a data para o BD
    private LocalDate dataCadastro;


}
