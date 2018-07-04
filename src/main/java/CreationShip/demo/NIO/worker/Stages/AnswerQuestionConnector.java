package CreationShip.demo.NIO.worker.Stages;

import CreationShip.demo.NIO.comunic.Reader;
import CreationShip.demo.NIO.comunic.Writer;
import CreationShip.demo.models.Message;
import CreationShip.demo.models.Question;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnswerQuestionConnector implements IConnector {

    private static final Logger logger = LoggerFactory.getLogger(AnswerQuestionConnector.class);

    private Reader reader;
    private Writer writer;
    private MessageService messageService;
    private QuestionService questionService;
    private Question question;
    private int counter = 1;

    public AnswerQuestionConnector(MessageService messageService, QuestionService questionService){
        this.messageService = messageService;
        this.questionService = questionService;
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public boolean getStateStage() {
        if(counter > 3){
            counter = 1;
            return true;
        }
            return false;

    }

    @Override
    public void setCounterDefoultValue() {
        counter = 1;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public String read() {
        String response = reader.read();
        Message message = new Message(question,response);
        messageService.saveOrUpdate(message);
        return response;
    }

    @Override
    public void write() {

        counter++;

        question = questionService.getRandomQuestion(1).get(0);

        while (question.getQuestion().length() < 4) {
            question = questionService.getRandomQuestion(1).get(0);
        }

        writer.write(question.getQuestion() + System.lineSeparator());
        logger.info("question is " + question.getQuestion());

    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }

}
