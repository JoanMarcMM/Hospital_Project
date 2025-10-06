package com.example.hospital_project;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nurse") 
public class NurseController {

    @PostMapping("/login") 
    public ResponseEntity<Boolean> login(
    		@RequestBody Nurse nurse
    		) throws IllegalArgumentException, IOException {

        DaoJson dao = new DaoJson();
        ArrayList<Nurse> nurses = dao.readFile();

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
}