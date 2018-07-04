package CreationShip.demo.nio.worker.Stages;

import CreationShip.demo.nio.interaction.Reader;
import CreationShip.demo.nio.interaction.Writer;
import CreationShip.demo.models.Message;
import CreationShip.demo.models.Question;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetAnswerConnector implements IConnector {

    private static final Logger logger = LoggerFactory.getLogger(GetAnswerConnector.class);

    private Reader reader;
    private Writer writer;
    private MessageService messageService;
    private QuestionService questionService;
    private Question question;
    private Long messageId;
    private Long oldId;
    private List<Message> messageList;
    private int counter = 1;

    public GetAnswerConnector(MessageService messageService, QuestionService questionService){
        this.messageService = messageService;
        this.questionService = questionService;
    }

    @Override
    public boolean getStateStage() {
        if (counter == 3) {
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
    public Reader getReader() {
        return reader;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public String read() {
        logger.info("message is = " + reader.read());

        return reader.getResponce();

    }

    @Override
    public void write() {
        if (counter == 1){

            messageList = messageService.getByQuestion(messageId);

            if (messageList.size() == 0)
            return;

            messageList.forEach(message -> writer.write(message.getMessage()));
            oldId = messageList.get(messageList.size() - 1).getId();

            counter++;
        }

            messageList = messageService.getByQuestion(messageId, oldId);

            if (messageList.size() > 0) {
                messageList.forEach(message -> writer.write(message.getMessage()));
                oldId = messageList.get(messageList.size() - 1).getId();
            }


    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
        messageId = question.getId();
    }
}
