package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Person name is required")
    @Column(name = "person_name", nullable = false)
    private String personName;

    @NotBlank(message = "Event is required")
    @Column(nullable = false)
    private String event;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    // Конструкторы
    public Attendance() {}

    public Attendance(String personName, String event, LocalDate date) {
        this.personName = personName;
        this.event = event;
        this.date = date;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
