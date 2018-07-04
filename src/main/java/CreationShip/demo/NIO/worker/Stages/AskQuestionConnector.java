package CreationShip.demo.NIO.worker.Stages;

import CreationShip.demo.NIO.comunic.Reader;
import CreationShip.demo.NIO.comunic.Writer;
import CreationShip.demo.models.Question;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AskQuestionConnector implements IConnector {

    private static final Logger logger = LoggerFactory.getLogger(AskQuestionConnector.class);

    private Reader reader;
    private Writer writer;
    private MessageService messageService;
    private QuestionService questionService;
    private Question question;
    private int counter = 1;

    public AskQuestionConnector(MessageService messageService, QuestionService questionService){
        this.messageService = messageService;
        this.questionService = questionService;
    }

    @Override
    public boolean getStateStage() {
        if(counter > 1){
            counter = 1;
            return true;
        }
        return false;
    }

    @Override
    public void setCounterDefoultValue() {
        counter = 1;
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public String read() {
        question = new Question(reader.read());
        question = questionService.saveOrUpdate(question);
        return question.getQuestion();
    }

    @Override
    public void write() {
        counter++;
        logger.info("ask question");
        writer.write("Ask question, please");
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
