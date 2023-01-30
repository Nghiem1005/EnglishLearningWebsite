package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.response.QuestionResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.QuestionMapper;
import com.example.englishlearningwebsite.repository.PartExamRepository;
import com.example.englishlearningwebsite.repository.QuestionRepository;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.QuestionService;
import com.example.englishlearningwebsite.utils.Utils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {
  @Autowired private QuestionRepository questionRepository;
  @Autowired private PartExamRepository partExamRepository;
  @Autowired private FileStorageService fileStorageService;

  @Override
  public ResponseEntity<ResponseObject> getAllQuestionByPractice(Pageable pageable,
      Long questionId) {
    return null;
  }

  @Override
  public ResponseEntity<ResponseObject> createQuestion(Long practiceId,
      QuestionRequestDTO questionRequestDTO) {
    PartExam partExam = partExamRepository.findById(practiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find practice with ID = " + practiceId));

    Question question = new Question();
    question.setPartExam(partExam);

    //Check if name lesson in course or no
    Optional<Question> getQuestion = questionRepository.findQuestionByContent(questionRequestDTO.getContent());
    if (getQuestion.isPresent()){
      if (getQuestion.get().getPartExam() == partExam){
        throw new ResourceAlreadyExistsException("This course has already lesson.");
      }
    }

    question.setContent(questionRequestDTO.getContent());

    if (questionRequestDTO.getDocument() != null) {
      String nameFiles = fileStorageService.storeFile(questionRequestDTO.getDocument(), Utils.IMAGE_UPLOAD_QUESTION_URI);
      question.setDocument(nameFiles);
    }

    //Check serial greater previous serial and consecutive
    List<Question> questionList = questionRepository.findQuestionsByPartExamOrderBySerial(
        partExam);
    if (questionList.size() != 0){
      question.setSerial(questionList.get(questionList.size() - 1).getSerial() + 1);
    } else {
      question.setSerial(1);
    }

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionRepository.save(question));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create question success!", questionResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateQuestion(Long id, QuestionRequestDTO questionRequestDTO) {
    Question question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + id));;

    //Check if name lesson in course or no
    Optional<Question> getQuestion = questionRepository.findQuestionByContent(questionRequestDTO.getContent());
    if (getQuestion.isPresent()){
      if (getQuestion.get().getPartExam() == question.getPartExam() && getQuestion.get().getId() != id){
        throw new ResourceAlreadyExistsException("This course has already lesson.");
      }
    }

    question.setContent(questionRequestDTO.getContent());

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionRepository.save(question));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update question success!", questionResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteQuestion(Long id) {
    Question getQuestion = questionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + id));

    questionRepository.delete(getQuestion);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete question successfully!"));
  }
}
