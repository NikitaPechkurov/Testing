package model;

public abstract class Colleague {
    protected Mediator mediator;

    Question receivedMessage;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    public abstract void notifyColleague(Question message);

    public void receive(Question message){
        this.receivedMessage = message;
    }

    public Question getReceivedMessage() {
        return this.receivedMessage;
    }
}
