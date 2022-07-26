package br.com.hypeit.videoaula;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
public class VideoaulaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoaulaApplication.class, args);
	}

}

@Entity
class AnimalEntity {

	@Id
	UUID id;
	String nome;
	Long idade;
	String especie;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdade() {
		return idade;
	}

	public void setIdade(Long idade) {
		this.idade = idade;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public AnimalEntity() {
	}

	public AnimalEntity(UUID id, String nome, Long idade, String especie) {
		this.id = id;
		this.nome = nome;
		this.idade = idade;
		this.especie = especie;
	}

	static AnimalEntity of(AnimalDTO dto) {
		return new AnimalEntity(
				UUID.randomUUID(),
				dto.nome(),
				dto.idade(),
				dto.especie()
		);
	}

}

@Repository
interface AnimalRepository extends JpaRepository<AnimalEntity, UUID> {}


@RestController
class ZoologicoController {

	@Autowired
	AnimalRepository animalRepository;


	@GetMapping
	public List<AnimalDTO> get() {
		return animalRepository.findAll().stream().map(
				AnimalDTO::of
		).collect(Collectors.toList());
	}

	@PostMapping
	public AnimalDTO post(@Valid @RequestBody AnimalDTO animal) {
		animalRepository.save(AnimalEntity.of(animal));
		return animal;
	}

}

@Validated
record AnimalDTO(
		@Size(min = 2)
		String nome,
		Long idade,
		String especie
) {
	static AnimalDTO of(AnimalEntity entity) {
		return new AnimalDTO(
				entity.getNome(),
				entity.getIdade(),
				entity.getEspecie()
		);
	}
}
