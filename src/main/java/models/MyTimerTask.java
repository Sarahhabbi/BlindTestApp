package models;

import java.util.Scanner;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private String answer;
    private int score;
    private int round;

    public MyTimerTask() {
        this.answer = null;
        this.score=0;
        this.round=0;
    }

    @Override
    public void run() {
        String user_answer;
        Scanner reader = new Scanner(System.in);
        user_answer= reader.nextLine();
        if (user_answer.equals(answer)) {
            score++;
            System.out.println("Bonne réponse !");
        }
        else{
            System.out.println("Mauvaise réponse");
        }
    }

    private void completeTask() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   /* public static void main(String args[]){
        TimerTask timerTask = new MyTimerTask();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
        System.out.println("TimerTask started");
        //cancel after sometime
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
        System.out.println("TimerTask cancelled");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}