package org.example.judge.classroom.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.judge.auth.domain.User;
import org.example.judge.auth.repository.UserRepository;
import org.example.judge.classroom.domain.ClassRoom;
import org.example.judge.classroom.domain.ClassRoomWithStudents;
import org.example.judge.classroom.domain.CreateClassRoomReq;
import org.example.judge.classroom.repostiroty.ClassRoomRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository, UserRepository userRepository) {
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
    }

    public void createClassRoom(CreateClassRoomReq req) {
        List<User> students = userRepository.findAllById(req.studentIds());
        if (students.size() != req.studentIds().size()) {
            throw new IllegalArgumentException("Some student IDs are invalid");
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName(req.name());
        classRoom.setStudents(new HashSet<>(students));
        classRoomRepository.save(classRoom);
    }

    public List<ClassRoomWithStudents> getAllClassRooms() {
        List<ClassRoomWithStudents> clzz = classRoomRepository.findBy();
        if(clzz == null) {
            throw new IllegalArgumentException("Classrooms not found");
        }
        return clzz;
    }

    public ClassRoomWithStudents getClassRoomById(Long id) {
        return classRoomRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException("Class room not found"));
    }

    public void deleteClassRoom(Long id) {
        ClassRoom classRoom = classRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class room not found"));
        classRoomRepository.delete(classRoom);
    }
}