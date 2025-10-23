package com.example.hospital_project;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hospital_project.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
    
}
