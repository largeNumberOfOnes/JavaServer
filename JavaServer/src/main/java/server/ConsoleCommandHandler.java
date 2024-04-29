package server;

import java.util.concurrent.Exchanger;

public class ConsoleCommandHandler implements Runnable {

    @Override
    public void run() {

        var exchanger = new Exchanger<ConsoleCommand>();
        ConsoleCommand mes = null;

        try {
            mes = exchanger.exchange(null);
            exchanger.exchange(mes);

        }
        catch (InterruptedException e) {

        }

    }

}
