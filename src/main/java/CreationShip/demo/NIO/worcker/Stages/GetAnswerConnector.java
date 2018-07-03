package CreationShip.demo.NIO.worcker.Stages;

import CreationShip.demo.NIO.comunic.Reader;
import CreationShip.demo.NIO.comunic.Writer;
import CreationShip.demo.models.Message;
import CreationShip.demo.models.Question;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;

import java.util.List;

public class GetAnswerConnector implements IConnector {

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
       return false;
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

        reader.enableWriteMode(false);
        return "Empty";

    }

    @Override
    public void write() {

        writer.enableReadMode(false);
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
