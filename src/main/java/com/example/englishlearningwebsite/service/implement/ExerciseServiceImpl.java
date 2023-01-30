package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.response.ExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.ScoreExerciseResponseDTO;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.ExamMapper;
import com.example.englishlearningwebsite.mapper.ScoreExerciseMapper;
import com.example.englishlearningwebsite.repository.ExamRepository;
import com.example.englishlearningwebsite.repository.ExerciseRepository;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.ExerciseService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {
  @Autowired private ExamRepository examRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<?> createExercise(Long lessonId, Long examId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Exercise exercise = new Exercise();
    exercise.setExam(exam);
    exercise.setLesson(lesson);

    exerciseRepository.save(exercise);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create exercise success!", exercise));
  }

  @Override
  public ResponseEntity<?> getExamByLesson(Long lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    List<Exercise> exerciseList = exerciseRepository.findExercisesByLesson(lesson);

    List<ExamResponseDTO> examResponseDTOS = new ArrayList<>();
    for (Exercise exercise : exerciseList){
      examResponseDTOS.add(ExamMapper.INSTANCE.examToExamResponseDTO(exercise.getExam()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get exam by lesson ID = " + lessonId, examResponseDTOS));
  }

  @Override
  public ResponseEntity<ResponseObject> updateScoreStudentExercise(
      ScoreExerciseRequestDTO scoreExerciseRequestDTO) {
    Lesson lesson = lessonRepository.findById(scoreExerciseRequestDTO.getLessonId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + scoreExerciseRequestDTO.getLessonId()));

    Exam exam = examRepository.findById(scoreExerciseRequestDTO.getExamId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + scoreExerciseRequestDTO.getExamId()));

    User student = userRepository.findById(scoreExerciseRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + scoreExerciseRequestDTO.getStudentId()));

    //Create exercise
    Exercise exercise = new Exercise();

    List<Exercise> exerciseStudentList = exerciseRepository.findExercisesByLessonAndExam(lesson, exam);
    if (exerciseStudentList.size() != 0){
      List<Exercise> exerciseList = exerciseRepository.findExercisesByLessonAndExamAndStudentIsNull(lesson, exam);

      if (exerciseList.size() == 0){
        exercise.setStudent(student);
        exercise.setScores(scoreExerciseRequestDTO.getScore());
        exercise.setLesson(lesson);
        exercise.setExam(exam);
      } else {
        exercise = exerciseList.get(0);
        exercise.setStudent(student);
        exercise.setScores(scoreExerciseRequestDTO.getScore());
      }
    } else {
      throw new ResourceNotFoundException("Could not find exercise with exam ID = " + scoreExerciseRequestDTO.getExamId() + " and lesson ID = " + scoreExerciseRequestDTO.getLessonId());
    }

    ScoreExerciseResponseDTO scoreExerciseResponseDTO = ScoreExerciseMapper.INSTANCE.exerciseToScoreExerciseResponseDTO(exerciseRepository.save(exercise));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update score exercise user success!", scoreExerciseResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteExercise(Long lessonId, Long examId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    List<Exercise> exerciseStudentList = exerciseRepository.findExercisesByLessonAndExam(lesson, exam);

    exerciseRepository.deleteAll(exerciseStudentList);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete exercise success!"));
  }

  @Override
  public ResponseEntity<?> getScoreExercise(Long lessonId, Long examId, Long studentId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Exercise exercise = exerciseRepository.findExerciseByExamAndLessonAndStudent(exam, lesson, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exercise with student ID = " + studentId + " and lesson ID = " + lessonId
        + " and exam ID = " + examId));

    ScoreExerciseResponseDTO scoreExerciseResponseDTO = ScoreExerciseMapper.INSTANCE.exerciseToScoreExerciseResponseDTO(exercise);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get score exercise success!", scoreExerciseResponseDTO));
  }
}
