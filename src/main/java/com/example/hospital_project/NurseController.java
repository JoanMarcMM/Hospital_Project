package com.example.hospital_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hospital_project.NurseRepository;

@RestController
@RequestMapping("/nurse")

public class NurseController {

	@Autowired
	private NurseRepository nurseRepository;

	@PostMapping("/new")
	public ResponseEntity<?> createNurse(@RequestBody Nurse nurse) {
		try {
			String validationError = validateNurse(nurse);
			if (validationError != null) {
				return ResponseEntity.badRequest().body(validationError);
			}

			Nurse savedNurse = nurseRepository.save(nurse);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedNurse);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al crear el enfermero: " + e.getMessage());
		}
	}

	private String validateNurse(Nurse nurse) {
		if (nurse == null) {
			return "El objeto enfermero no puede ser nulo.";
		}

		if (isNullOrEmpty(nurse.getName()) || isNullOrEmpty(nurse.getLastname()) || isNullOrEmpty(nurse.getUser())
				|| isNullOrEmpty(nurse.getPw())) {
			return "Faltan campos obligatorios.";
		}

		if (!isValidPassword(nurse.getPw())) {
			return "La contraseña debe tener al menos 8 caracteres, una letra mayúscula y un número.";
		}

		return null;
	}

	private boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean isValidPassword(String password) {
		String regex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
		return password.matches(regex);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Nurse>> getNurse(@PathVariable("id") Long idNurse) throws IOException {
		Optional<Nurse> nurse = nurseRepository.findById(idNurse);
		if (nurse.isPresent()) {
			return ResponseEntity.ok(nurse);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNurse(@PathVariable long id) {

		if (nurseRepository.existsById(id)) {
			nurseRepository.deleteById(id);
			return ResponseEntity.ok("Nurse with ID " + id + " deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse with ID " + id + " not found.");
		}
	}

	@GetMapping("/index")
	public ResponseEntity<List<Nurse>> getAll() throws IOException {
		List<Nurse> nurses = nurseRepository.findAll();
		return ResponseEntity.ok(nurses);
	}

	@PostMapping("/login")
	public ResponseEntity<Boolean> login(@RequestBody Nurse nurse) throws IllegalArgumentException, IOException {

		List<Nurse> list = nurseRepository.findAll();
		ArrayList<Nurse> nurses = new ArrayList<Nurse>(list);

		boolean loggedin = false;

		for (Nurse nurse2 : nurses) {
			if (nurse2.getUser().equals(nurse.getUser()) && nurse2.getPw().equals(nurse.getPw())) {
				loggedin = true;
				break;
			}
		}

		if (loggedin) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable(value = "id", required = true) Long id,
			@RequestBody Nurse nurseBody) {

		Optional<Nurse> optionalNurse = nurseRepository.findById(id);

		if (optionalNurse.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Nurse nurseDB = optionalNurse.get();

		List<String> errors = new ArrayList<>();

		// ====== NAME ======
		if (nurseBody.getName() != null) {
			String name = nurseBody.getName().trim();
			if (!name.isEmpty()) {
				if (!name.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
					errors.add("name no puede contener números ni caracteres raros");
				} else {
					nurseDB.setName(name);
				}
			}
		}

		// ====== LASTNAME ======
		if (nurseBody.getLastname() != null) {
			String lastname = nurseBody.getLastname().trim();
			if (!lastname.isEmpty()) {
				if (!lastname.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
					errors.add("lastname no puede contener números ni caracteres raros");
				} else {
					nurseDB.setLastname(lastname);
				}
			}
		}

		// ====== USER ======
		if (nurseBody.getUser() != null) {
			String user = nurseBody.getUser().trim();
			if (!user.isEmpty()) {
				if (!user.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ._-]+$")) {
					errors.add("user no puede contener números ni caracteres raros");
				} else {
					nurseDB.setUser(user);
				}
			}
		}

		// ====== PW ======
		if (nurseBody.getPw() != null) {
			String pw = nurseBody.getPw().trim();
			if (!pw.isEmpty()) {
				nurseDB.setPw(pw);
			}
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		Nurse nurseUpdated = nurseRepository.save(nurseDB);

		return ResponseEntity.ok(nurseUpdated);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Nurse>> findByName(@PathVariable("name") String name) {
		List<Nurse> nurses = nurseRepository.findByNameContainingIgnoreCase(name);

		if (nurses.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(nurses);
		}
	}

}