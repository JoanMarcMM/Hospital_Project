package com.example.hospital_project;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hospital_project.Nurse;
import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
	List<Nurse>findByNameContainingIgnoreCase(String name);
}
