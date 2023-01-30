package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.AnswerRequestDTO;
import com.example.englishlearningwebsite.dto.response.AnswerResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.Answer;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.AnswersMapper;
import com.example.englishlearningwebsite.mapper.BillMapper;
import com.example.englishlearningwebsite.repository.AnswerRepository;
import com.example.englishlearningwebsite.repository.QuestionRepository;
import com.example.englishlearningwebsite.service.AnswerService;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerRepository answerRepository;

  @Override
  public ResponseEntity<ResponseObject> getAllAnswerByQuestion(Pageable pageable, Long questionId) {
    return null;
  }

  @Override
  public ResponseEntity<ResponseObject> createAnswer(Long questionId,
      AnswerRequestDTO answerRequestDTO) {
    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + questionId));

    Answer answer = new Answer();
    answer.setQuestion(question );

    //Check if content answer has already
    Optional<Answer> getAnswer = answerRepository.findAnswerByContentAndQuestion(answerRequestDTO.getContent(), question);
    if (getAnswer.isPresent()){
      throw new ResourceAlreadyExistsException("This answer has already in question.");
    }

    answer.setContent(answerRequestDTO.getContent());
    answer.setCorrect(answerRequestDTO.isCorrect());

    AnswerResponseDTO answerResponseDTO = AnswersMapper.INSTANCE.answerToAnswerResponseDTO(answerRepository.save(answer));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create answer success!", answerResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateAnswer(Long id, AnswerRequestDTO answerRequestDTO) {

    Answer answer = answerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find answer with ID = " + id));

    //Check if content answer has already
    Optional<Answer> getAnswer = answerRepository.findAnswerByContentAndQuestion(answerRequestDTO.getContent(), answer.getQuestion());
    if (getAnswer.isPresent() && getAnswer.get().getId() != id){
      throw new ResourceAlreadyExistsException("This answer has already in question.");
    }

    answer.setContent(answerRequestDTO.getContent());
    answer.setCorrect(answerRequestDTO.isCorrect());

    AnswerResponseDTO answerResponseDTO = AnswersMapper.INSTANCE.answerToAnswerResponseDTO(answerRepository.save(answer));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create answer success!", answerResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteAnswer(Long id) {
    Answer getAnswer = answerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find answer with ID = " + id));

    answerRepository.delete(getAnswer);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete answer successfully!"));
  }
}
