package CreationShip.demo.nio.worker.Stages;

import CreationShip.demo.nio.interaction.Reader;
import CreationShip.demo.nio.interaction.Writer;
import CreationShip.demo.models.Question;

public interface IConnector {

    public String read();
    public void write();
    public Question getQuestion();

    public boolean getStateStage();

    public void setWriter(Writer writer);
    public void setReader(Reader reader);

    public void setCounterDefoultValue();

    public Reader getReader();
    public Writer getWriter();

    public void setQuestion(Question question);
}
