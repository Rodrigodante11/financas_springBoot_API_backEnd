package com.rodrigocompany.financas.model.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="lancamento" , schema = "financas")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class Lancamento {

    @Id
    @Column(name="id")
    @GeneratedValue( strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    @Column(name="descricao")
    private String descricao;

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

    @Column(name="tipo")
    @Enumerated(value =  EnumType.STRING ) // pega pelo valor (RECEITA,DESPESA)
    // @Enumerated(value =  EnumType.ORDINAL ) // pega pelo Ordem (0,1)
    private TipoLancamento tipo;

    @Column(name="status")
    @Enumerated(value =  EnumType.STRING ) // pega pelo valor (RECEITA,DESPESA)
    private StatusLancamento status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lancamento that = (Lancamento) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
