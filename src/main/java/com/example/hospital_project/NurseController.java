package com.example.hospital_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hospital_project.NurseRepository;

@RestController
@RequestMapping("/nurse")
public class NurseController {
	
	
	private final NurseRepository nurseRepository;
    
    public NurseController(NurseRepository nurseRepository) {
    	
        this.nurseRepository = nurseRepository;
    }
	
	
	@GetMapping("/index") 
	public ResponseEntity<List<Nurse>> getAll() throws IOException{
        	DaoJson dao = new DaoJson();
			List<Nurse> nurses = dao.readFile();
			return ResponseEntity.ok(nurses);
			// return ResponseEntity.status(HttpStatus.OK).body(nurses); 
	}
	
	
	
	
    @PostMapping("/login") 
    public ResponseEntity<Boolean> login(
    		@RequestBody Nurse nurse
    		) throws IllegalArgumentException, IOException {

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

    
    
    
    
    
	@GetMapping("/name/{name}")
	public ResponseEntity<Nurse> getByName(@PathVariable String name) throws IOException {
		DaoJson dao = new DaoJson();
		ArrayList<Nurse> nurses = dao.readFile();

		List<Nurse> nurseFound = new ArrayList<>();
		for (Nurse nurse : nurses) {
			if (nurse.getName().toLowerCase().contains(name.toLowerCase())) {
				Nurse nurse1 = new Nurse(nurse.getName(), nurse.getLastname(), nurse.getUser(), nurse.getPw());
				nurseFound.add(nurse1);
				return ResponseEntity.ok(nurse1);
			}
		}

		return ResponseEntity.notFound().build();
		
	}

}