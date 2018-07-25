package CreationShip.demo.nio.worker;

import CreationShip.demo.nio.interaction.Reader;
import CreationShip.demo.nio.interaction.Writer;
import CreationShip.demo.nio.worker.Stages.AnswerQuestionConnector;
import CreationShip.demo.nio.worker.Stages.AskQuestionConnector;
import CreationShip.demo.nio.worker.Stages.GetAnswerConnector;
import CreationShip.demo.nio.worker.Stages.IConnector;
import CreationShip.demo.models.Question;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Connection implements IConnection{

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private int stage = 1;
    private IConnector iConnector;
    private AnswerQuestionConnector answerQuestionConnector;
    private AskQuestionConnector askQuestionConnector;
    private GetAnswerConnector getAnswerConnector;

    private HashMap<Integer, IConnector> stages = new HashMap<>();

    public Connection(MessageService messageService, QuestionService questionService){

        answerQuestionConnector = new AnswerQuestionConnector(messageService,questionService);
        askQuestionConnector = new AskQuestionConnector(messageService,questionService);
        getAnswerConnector = new GetAnswerConnector(messageService,questionService);

        stages.put(1,answerQuestionConnector);
        stages.put(2,askQuestionConnector);
        stages.put(3,getAnswerConnector);

        iConnector = stages.get(1);
    }

    public Reader getReader(){
        return answerQuestionConnector.getReader();
    }

    public Writer getWriter(){
        return answerQuestionConnector.getWriter();
    }

    public void setReader(Reader reader){
        answerQuestionConnector.setReader(reader);
        askQuestionConnector.setReader(reader);
        getAnswerConnector.setReader(reader);
    }

    public void setWriter(Writer write){
        answerQuestionConnector.setWriter(write);
        askQuestionConnector.setWriter(write);
        getAnswerConnector.setWriter(write);
    }

    public int getStage() {
        return stage;
    }

    @Override
    public void upStage() {
        stage++;
        iConnector = stages.get(stage);
        logger.info("up stage");

    }

    @Override
    public void downStage() {
        stage--;
        iConnector = stages.get(stage);
        logger.info("down stage");
    }

    @Override
    public void selectStage(int number) {
        stage = number;
        iConnector.setCounterDefoultValue();
        iConnector = stages.get(stage);
        logger.info("stage set on " + stage);

    }

    @Override
    public void write() {

        transferQuestion();

        iConnector.write();
    }

    private void transferQuestion() {

        if(iConnector.getStateStage()) {
            Question question = new Question();
            question = iConnector.getQuestion();
            upStage();
            iConnector.setQuestion(question);
        }

    }

    @Override
    public String read() {

        String response = iConnector.read();


        if (response.contains("n:s")) {
            int newStage = Integer.valueOf(String.valueOf(response.charAt(0)));
            logger.info("new stage is: " + newStage);
            transferQuestion();
            selectStage(newStage);
        }

        return response;
    }


}
