package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public String showAttendanceList(Model model) {
        model.addAttribute("attendances", attendanceService.getAllAttendances());
        return "attendance-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        return "attendance-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveAttendance(@Valid @ModelAttribute Attendance attendance,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "attendance-form";
        }

        attendanceService.saveAttendance(attendance);
        return "redirect:/attendance";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        if (attendance.isPresent()) {
            model.addAttribute("attendance", attendance.get());
            return "attendance-form";
        } else {
            return "redirect:/attendance";
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAttendance(@PathVariable("id") Long id) {
        if (attendanceService.existsById(id)) {
            attendanceService.deleteAttendance(id);
        }
        return "redirect:/attendance";
    }
}