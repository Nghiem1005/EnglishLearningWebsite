package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.response.AnswerResponseDTO;
import com.example.englishlearningwebsite.dto.response.ExamDetailResponseDTO;
import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.QuestionResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Answer;
import com.example.englishlearningwebsite.entities.Document;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.ExamDetail;
import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.AnswersMapper;
import com.example.englishlearningwebsite.mapper.ExamDetailMapper;
import com.example.englishlearningwebsite.mapper.PartExamMapper;
import com.example.englishlearningwebsite.mapper.QuestionMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.*;
import com.example.englishlearningwebsite.service.ExamDetailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExamDetailServiceImpl implements ExamDetailService {
  @Autowired private ExamDetailRepository examDetailRepository;
  @Autowired private ExamRepository examRepository;
  @Autowired private PartExamRepository partExamRepository;
  @Autowired private DocumentRepository documentRepository;
  @Autowired private QuestionRepository questionRepository;

  @Override
  public ResponseEntity<?> getPartExamByExam(Long examId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    List<ExamDetail> examDetailList = examDetailRepository.findExamDetailsByExamOrderBySerial(exam);

    List<ExamDetailResponseDTO> examDetailResponseDTOS = new ArrayList<>();

    for (ExamDetail examDetail : examDetailList){
      ExamDetailResponseDTO examDetailResponseDTO = ExamDetailMapper.INSTANCE.examDetailToExamDetailResponseDTO(examDetail);

      //Get part exam
      PartExam partExam = examDetail.getPartExam();

      PartExamResponseDTO partExamResponseDTO = PartExamMapper.INSTANCE.partExamToPartExamResponseDTO(partExam);

      //Get creator response
      UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(partExam.getCreator());
      partExamResponseDTO.setCreator(userResponseDTO);

      //Get question and answer of part exam
      List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
      List<Question> questionList = questionRepository.findQuestionsByPartExamOrderBySerial(partExam);
      for (Question question : questionList){

        //Get list answer of question
        List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
        for (Answer answer : question.getAnswers()){
          answerResponseDTOS.add(AnswersMapper.INSTANCE.answerToAnswerResponseDTO(answer));
        }

        QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(question);
        questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS.stream().toArray(AnswerResponseDTO[]::new));
        questionResponseDTOS.add(questionResponseDTO);
      }
      partExamResponseDTO.setQuestionResponseDTOS(
          questionResponseDTOS.stream().toArray(QuestionResponseDTO[]::new));

      //Get document
      Optional<Document> documentOptional = documentRepository.findDocumentByPartExam(partExam);
      if (documentOptional.isPresent()){
        partExamResponseDTO.setDocuments(documentOptional.get().getName());
      }

      examDetailResponseDTO.setPartExamResponseDTO(partExamResponseDTO);

      examDetailResponseDTOS.add(examDetailResponseDTO);
    }


    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Part exam with exam ID = " + examId, examDetailResponseDTOS));
  }

  @Override
  public ResponseEntity<?> addPartExamToExam(Long examId, Long partExamId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    PartExam partExam = partExamRepository.findById(partExamId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find part exam with ID = " + partExamId));

    List<ExamDetail> examDetailList = examDetailRepository.findExamDetailsByExamOrderBySerial(exam);

    ExamDetail examDetail = new ExamDetail();
    examDetail.setExam(exam);
    examDetail.setPartExam(partExam);
    examDetail.setSerial(examDetailList.size() + 1);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add part exam to exam success!", examDetailRepository.save(examDetail)));
  }

  @Override
  public ResponseEntity<?> deletePartExamToExam(Long examId, Long partExamId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    PartExam partExam = partExamRepository.findById(partExamId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find part exam with ID = " + partExamId));

    ExamDetail getExamDetail = examDetailRepository.findExamDetailByExamAndPartExam(exam, partExam)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam detail with exam ID = " + examId + " and part exam ID = " + partExamId));

    examDetailRepository.delete(getExamDetail);

    //Delete exam detail
    List<ExamDetail> examDetailList = examDetailRepository.findExamDetailsByExamOrderBySerial(exam);
    if (examDetailList.size() > 0){
      for (int i = 0; i < examDetailList.size(); i++){
        ExamDetail examDetail = examDetailList.get(i);
        examDetail.setSerial(i + 1);
      }
      examDetailRepository.saveAll(examDetailList);
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete part exam to exam success!"));
  }
}
