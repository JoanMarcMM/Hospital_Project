package com.example.hospital_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nurse") 
public class NurseController {
	@GetMapping("/index") 
	public ResponseEntity<List<Nurse>> getAll() throws IOException{
        	DaoJson dao = new DaoJson();
			List<Nurse> nurses = dao.readFile();
			return ResponseEntity.ok(nurses);
			// return ResponseEntity.status(HttpStatus.OK).body(nurses); 
	}
    @GetMapping("/login") 
    public ResponseEntity<Boolean> login(
            @RequestParam(value = "user", defaultValue = "") String name,
            @RequestParam(value = "pw", defaultValue = "") String pw
    ) throws IllegalArgumentException, IOException {

        DaoJson dao = new DaoJson();
        ArrayList<Nurse> nurses = dao.readFile();

        boolean loggedin = false;

        for (Nurse nurse : nurses) {
            if (nurse.getUser().equals(name) && nurse.getPw().equals(pw)) {
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
}