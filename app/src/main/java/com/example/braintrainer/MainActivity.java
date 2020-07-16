package com.example.braintrainer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer countDown;
    private TextView challengeText;

    private int result = 0;
    private int count = 0;
    private int guessed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        challengeText = findViewById(R.id.challengeText);

        updateChallenge();

        this.countDown = new CountDownTimer(10000, 100) {
            public void onTick(long l) {
                updateTimeText(l);
            }

            public void onFinish() {
                updateTimeText(0);
                setElementVisible(findViewById(R.id.playAgain));
                setButtonsEnabled(false);
            }
        }.start();
    }

    public void result(View view) {
        Button button = findViewById(R.id.botonesLayout).findViewWithTag(view.getTag());

        this.count++;

        buttonWrongCorrect(button.getText().equals(String.valueOf(this.result)));

        updateChallenge();
    }

    private void setElementVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void playAgain(View view) {
        view.setVisibility(View.INVISIBLE);
        setButtonsEnabled(true);
        this.guessed = 0;
        this.count = 0;
        this.countDown.start();
        updateChallenge();
    }

    private int randomize(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    private void updateTimeText(long l) {
        TextView time = findViewById(R.id.time);
        time.setText(String.format(Locale.getDefault(), "%.01f", (float) l / 1000));
    }

    private void updateChallengeText(int operation) {
        int randomButton = randomize(0, 3);
        int x = 0;
        int y = 0;

        String operationString = "";
        switch (operation) {
            case 0: // "sum"
                operationString = "+";
                x = randomize(0, 50);
                y = randomize(0, 50);
                this.result = x + y;
                break;
            case 1: // "sub"
                operationString = "-";
                x = randomize(0, 100);
                y = randomize(0, x);
                this.result = x - y;
                break;
            case 2: // "mul"
                x = randomize(0, 10);
                y = randomize(0, 10);
                operationString = "*";
                this.result = x * y;
                break;
            case 3: // "div"
                x = randomize(0, 20);
                y = randomize(1, 10);
                operationString = "/";
                this.result = x;
                x = x * y;
                break;
        }

        Log.i("Operation", String.format(Locale.getDefault(), "x: %d %s y: %d", x, operationString, y));

        for (int i = 0; i < 4; i++) {
            Button button = findViewById(R.id.botonesLayout).findViewWithTag("b" + i);
            button.setText(i != randomButton ?
                    String.valueOf(randomize(Math.max(this.result - 20, 0), Math.min(this.result + 20, 100))) :
                    String.valueOf(this.result)
            );
        }

        this.challengeText.setText(String.format(Locale.getDefault(), "%d%s%d", x, operationString, y));
    }

    private void buttonWrongCorrect(boolean correct) {
        TextView wrongCorrectText = findViewById(R.id.wrongCorrectText);
        wrongCorrectText.setVisibility(View.VISIBLE);

        if (correct) {
            this.guessed++;
            wrongCorrectText.setText("CORRECT!");
            wrongCorrectText.setTextColor(Color.parseColor("#00AB14"));

        } else {
            wrongCorrectText.setText("WRONG!");
            wrongCorrectText.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    private void setButtonsEnabled(boolean activated) {
        for (int i = 0; i < 4; i++) {
            Button button = findViewById(R.id.botonesLayout).findViewWithTag("b" + i);
            button.setEnabled(activated);
        }
    }

    private void updateChallenge() {
        TextView score = findViewById(R.id.score);
        score.setText(String.format(Locale.getDefault(), "%d/%d", this.guessed, this.count));
        updateChallengeText(randomize(0, 4));
    }
}