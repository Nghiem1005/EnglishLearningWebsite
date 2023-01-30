package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.AnswerRequestDTO;
import com.example.englishlearningwebsite.dto.request.PartExamRequestDTO;
import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.response.AnswerResponseDTO;
import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.QuestionResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Answer;
import com.example.englishlearningwebsite.entities.Document;
import com.example.englishlearningwebsite.entities.ExamDetail;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.enums.FileType;
import com.example.englishlearningwebsite.entities.enums.PartExamType;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.AnswersMapper;
import com.example.englishlearningwebsite.mapper.PartExamMapper;
import com.example.englishlearningwebsite.mapper.QuestionMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.AnswerRepository;
import com.example.englishlearningwebsite.repository.DocumentRepository;
import com.example.englishlearningwebsite.repository.ExamDetailRepository;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.PartExamRepository;
import com.example.englishlearningwebsite.repository.QuestionRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.ImageStorageService;
import com.example.englishlearningwebsite.service.PartExamService;
import com.example.englishlearningwebsite.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PartExamServiceImpl implements PartExamService {

  @Autowired
  private LessonRepository lessonRepository;
  @Autowired
  private PartExamRepository partExamRepository;
  @Autowired
  private FileStorageService fileStorageService;
  @Autowired
  private ExamDetailRepository examDetailRepository;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DocumentRepository documentRepository;

  @Override
  public ResponseEntity<?> createPartExam(Long userId, PartExamRequestDTO partExamRequestDTO,
      MultipartFile document) {

    User creator = userRepository.findById(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    PartExam partExam = new PartExam();
    partExam.setCreator(creator);

    partExam.setName(partExamRequestDTO.getName());

    partExam = partExamRepository.save(partExam);

    if (document != null) {
      storageDocument(partExam, document);
    }

    PartExamResponseDTO partExamResponseDTO = PartExamMapper.INSTANCE.partExamToPartExamResponseDTO(
        partExamRepository.save(
            partExam));

    partExamResponseDTO.setQuestionResponseDTOS(
        saveQuestionAndAnswer(partExam, partExamRequestDTO));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create part exam success!",
            partExamResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePartExam(Long id, PartExamRequestDTO partExamRequestDTO,
      MultipartFile document) {
    PartExam partExam = partExamRepository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find part exam with ID = " + id));

    partExam.setName(partExamRequestDTO.getName());

    if (document != null) {
      //Delete document
      Optional<Document> documentOptional = documentRepository.findDocumentByPartExam(partExam);
      if (documentOptional.isPresent()) {
        documentRepository.delete(documentOptional.get());
      }

      storageDocument(partExam, document);
    }

    PartExamResponseDTO partExamResponseDTO = PartExamMapper.INSTANCE.partExamToPartExamResponseDTO(
        partExamRepository.save(
            partExam));

    List<Question> questionList = questionRepository.findQuestionsByPartExamOrderBySerial(partExam);
    questionRepository.deleteAll(questionList);
    partExamResponseDTO.setQuestionResponseDTOS(
        saveQuestionAndAnswer(partExam, partExamRequestDTO));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update part exam success!",
            partExamResponseDTO));
  }

  @Override
  public ResponseEntity<?> deletePartExam(Long id) {
    PartExam partExam = partExamRepository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find part exam with ID = " + id));

    //Delete document
    Optional<Document> documentOptional = documentRepository.findDocumentByPartExam(partExam);
    if (documentOptional.isPresent()) {
      documentRepository.delete(documentOptional.get());
    }

    List<ExamDetail> examDetailList = examDetailRepository.findExamDetailsByPartExam(partExam);
    examDetailRepository.deleteAll(examDetailList);

    partExamRepository.delete(partExam);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete part exam success!"));
  }

  @Override
  public ResponseEntity<?> getAllPartExam(Pageable pageable) {
    Page<PartExam> partExamPage = partExamRepository.findAll(pageable);
    List<PartExam> partExamList = partExamPage.getContent();
    List<PartExamResponseDTO> partExamResponseDTOS = new ArrayList<>();
    for (PartExam partExam : partExamList) {
      PartExamResponseDTO partExamResponseDTO = PartExamMapper.INSTANCE.partExamToPartExamResponseDTO(
          partExam);

      //Get document
      Optional<Document> documentOptional = documentRepository.findDocumentByPartExam(partExam);
      if (documentOptional.isPresent()) {
        partExamResponseDTO.setDocuments(documentOptional.get().getName());
      }

      partExamResponseDTOS.add(partExamResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(HttpStatus.OK, "List all part exam.", partExamResponseDTOS,
            partExamPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getPartExamById(Long id) {
    PartExam partExam = partExamRepository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find part exam with ID = " + id));

    PartExamResponseDTO partExamResponseDTO = PartExamMapper.INSTANCE.partExamToPartExamResponseDTO(
        partExam);

    //Get creator response
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(
        partExam.getCreator());
    partExamResponseDTO.setCreator(userResponseDTO);

    //Get question and answer of part exam
    List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
    for (Question question : partExam.getQuestions()) {

      //Get list answer of question
      List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
      for (Answer answer : question.getAnswers()) {
        answerResponseDTOS.add(AnswersMapper.INSTANCE.answerToAnswerResponseDTO(answer));
      }

      QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(
          question);
      questionResponseDTO.setAnswerResponseDTOS(
          answerResponseDTOS.stream().toArray(AnswerResponseDTO[]::new));
      questionResponseDTOS.add(questionResponseDTO);
    }
    partExamResponseDTO.setQuestionResponseDTOS(
        questionResponseDTOS.stream().toArray(QuestionResponseDTO[]::new));

    //Get document
    Optional<Document> documentOptional = documentRepository.findDocumentByPartExam(partExam);
    if (documentOptional.isPresent()) {
      partExamResponseDTO.setDocuments(documentOptional.get().getName());
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Part exam with ID = " + id, partExamResponseDTO));
  }

  private boolean isImageFile(MultipartFile file) {
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
        .contains(fileExtension.trim().toLowerCase());
  }

  private QuestionResponseDTO[] saveQuestionAndAnswer(PartExam partExam,
      PartExamRequestDTO partExamRequestDTO) {
    //Save question
    List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
    List<Question> questions = new ArrayList<>();
    for (QuestionRequestDTO questionRequestDTO : partExamRequestDTO.getQuestionRequestDTOS()) {
      Question question = new Question();
      question.setPartExam(partExam);

      //Check type question
      if (!questionRequestDTO.getContent().equals("")) {
        //Check if name lesson in course or no
        Optional<Question> getQuestion = questionRepository.findQuestionByContent(
            questionRequestDTO.getContent());
        if (getQuestion.isPresent()) {
          if (getQuestion.get().getPartExam() == partExam) {
            throw new ResourceAlreadyExistsException("This course has already lesson.");
          }
        }

        question.setContent(questionRequestDTO.getContent());
      } else if (questionRequestDTO.getDocument() != null) {
        String nameFiles = fileStorageService.storeFile(questionRequestDTO.getDocument(),
            Utils.IMAGE_UPLOAD_QUESTION_URI);
        question.setDocument(nameFiles);
      }

      //Check if name lesson in course or no
      /*Optional<Question> getQuestion = questionRepository.findQuestionByContent(
          questionRequestDTO.getContent());
      if (getQuestion.isPresent()) {
        if (getQuestion.get().getPartExam() == partExam) {
          throw new ResourceAlreadyExistsException("This course has already lesson.");
        }
      }*/

      question.setContent(questionRequestDTO.getContent());

      //Check serial greater previous serial and consecutive
      question.setSerial(
          Arrays.asList(partExamRequestDTO.getQuestionRequestDTOS()).indexOf(questionRequestDTO)
              + 1);
      question = questionRepository.save(question);

      //Save answer
      List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
      List<Answer> answers = new ArrayList<>();
      for (AnswerRequestDTO answerRequestDTO : questionRequestDTO.getAnswerRequestDTOS()) {
        Answer answer = new Answer();
        answer.setQuestion(question);

        //Check if content answer has already
        Optional<Answer> getAnswer = answerRepository.findAnswerByContentAndQuestion(
            answerRequestDTO.getContent(), question);
        if (getAnswer.isPresent()) {
          throw new ResourceAlreadyExistsException("This answer has already in question.");
        }

        answer.setContent(answerRequestDTO.getContent());
        answer.setCorrect(answerRequestDTO.isCorrect());

        Answer answerSaved = answerRepository.save(answer);

        answers.add(answerSaved);

        AnswerResponseDTO answerResponseDTO = AnswersMapper.INSTANCE.answerToAnswerResponseDTO(
            answerSaved);
        answerResponseDTOS.add(answerResponseDTO);
      }

      question.setAnswers(answers);

      Question questionSaved = questionRepository.save(question);

      questions.add(questionSaved);

      QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(
          questionSaved);
      questionResponseDTO.setAnswerResponseDTOS(
          answerResponseDTOS.stream().toArray(AnswerResponseDTO[]::new));
      questionResponseDTOS.add(questionResponseDTO);
    }

    partExam.setQuestions(questions);
    return questionResponseDTOS.stream().toArray(QuestionResponseDTO[]::new);
  }

  private void storageDocument(PartExam partExam, MultipartFile documentFile) {
    //Store document
    if (documentFile != null) {
      Document document = new Document();
      document.setPartExam(partExam);

      String nameFiles = "";
      //Save type file
      if (isImageFile(documentFile)) {
        nameFiles = fileStorageService.storeFile(documentFile, Utils.IMAGE_UPLOAD_PART_EXAM_URI);
        document.setType(FileType.IMAGE);
        partExam.setType(PartExamType.READING);
      } else {
        nameFiles = fileStorageService.storeFile(documentFile, Utils.AUDIO_UPLOAD_PART_EXAM_URI);
        document.setType(FileType.AUDIO);
        partExam.setType(PartExamType.LISTENING);
      }
      document.setName(nameFiles);
      documentRepository.save(document);
    }
  }

}
