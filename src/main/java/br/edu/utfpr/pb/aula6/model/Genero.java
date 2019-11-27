package br.edu.utfpr.pb.aula6.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "genero")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Genero implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "O campo 'nome' deve ser preenchido!")
	@Column(length = 254, nullable = false)
	private String nome;
	
}
