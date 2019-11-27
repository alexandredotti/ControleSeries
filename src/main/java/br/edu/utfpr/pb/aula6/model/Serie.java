package br.edu.utfpr.pb.aula6.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "serie")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "nome"})
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Serie implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "O campo 'nome' deve ser preenchido!")
	@Column(length = 254, nullable = false)
	private String nome;
	
	@NotEmpty(message = "O campo 'resumo' deve ser preenchido!")
	@Column(length = 1024, nullable = false)
	private String resumo;
	
	@NotNull(message = "O campo 'Estréia' deve ser preenchido!")
	@Column(name = "data_estreia", nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")  
	private LocalDate dataEstreia;
		
	@Column(name = "data_encerramento", nullable = true)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataEncerramento;
	
	@NotNull(message = "O campo 'nota' deve ser preenchido!")
	@Min(value = 1, message = "A nota deve ser maior do que 0 (zero)!")
	@Max(value = 100, message = "A nota deve ser menor do que 100 (cem)!")
	@Column(nullable = false)
	private BigDecimal nota;
	
	@NotNull(message = "O campo 'genero' deve ser preenchido!")
	@ManyToOne
	@JoinColumn(name = "genero_id", referencedColumnName = "id")
	private Genero genero;
	
	@NotNull(message = "O campo 'produtora' deve ser preenchido!")
	@ManyToOne
	@JoinColumn(name = "produtora_id", referencedColumnName = "id")
	private Produtora produtora;
	
	@Column(length = 1024, nullable = true)
	private String imagem;
}
