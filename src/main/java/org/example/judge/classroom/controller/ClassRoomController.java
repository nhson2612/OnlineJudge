package org.example.judge.classroom.controller;

import org.example.judge.classroom.domain.ClassRoomWithStudents;
import org.example.judge.classroom.domain.CreateClassRoomReq;
import org.example.judge.classroom.service.ClassRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classroom")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassRoomWithStudents> getClassById(@RequestParam Long id){
        return ResponseEntity.ok(classRoomService.getClassRoomById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<Iterable<ClassRoomWithStudents>> getAllClasses() {
        return ResponseEntity.ok(classRoomService.getAllClassRooms());
    }
    @PostMapping("/create")
    public ResponseEntity<Void> createClassRoom(@RequestBody CreateClassRoomReq req) {
        classRoomService.createClassRoom(req);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public  ResponseEntity<Void> deleteClassRoom(@RequestParam Long id){
        classRoomService.deleteClassRoom(id);
        return ResponseEntity.noContent().build();
    }
}
