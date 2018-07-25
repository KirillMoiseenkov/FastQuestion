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
    private int counter;

    public GetAnswerConnector(MessageService messageService, QuestionService questionService){
        this.messageService = messageService;
        this.questionService = questionService;
        this.counter = 1;

        logger.info("initialize");
    }

    @Override
    public boolean getStateStage() {
        if (counter == 3) {
          //  counter = 1;
            return true;
        }
        return false;
    }

    @Override
    public void setCounterDefoultValue() {
        //counter = 1;
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

        System.out.println("pre counter = " + counter);


        if (counter == 1){

            messageList = messageService.getByQuestion(messageId);

            if (messageList.size() == 0)
            return;

            messageList.forEach(message ->
            {
                    writer.write(message.getMessage());
                    System.out.println("face messageList.size() = " + messageList.size());

            });
            oldId = messageList.get(messageList.size() - 1).getId();

            counter++;
        }

        System.out.println("counter = " + counter);

            messageList = messageService.getByQuestion(messageId, oldId);

            if (messageList.size() > 0) {

                messageList.forEach(message -> writer.write(message.getMessage()));
                oldId = messageList.get(messageList.size() - 1).getId();

                System.out.println("inner messageList.size() = " + messageList.size());
                //logger.info("messageList.size() = " + String.valueOf(messageList.size()));
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
