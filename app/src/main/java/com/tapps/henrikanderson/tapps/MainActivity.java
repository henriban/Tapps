package com.tapps.henrikanderson.tapps;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation animation = new AlphaAnimation(1, (float) 0.3); // Change alpha from fully visible to invisible
        animation.setDuration(700); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        final Button btn = (Button) findViewById(R.id.colorButton);
        changeColor(btn, "#00FF00");
        btn.startAnimation(animation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.clearAnimation();
                start(view);
            }
        });

        //Lock the screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *Init the game,setts the buttonGried
     */
    public void start(View view){
        initButtonGried();
        startTime();
        enableAllButtons();
        changeAllButtonColor();
        setColorButton();
        buttonGrid.clear();
        noTextColorButton();
        tapps = 0;
    }

    public void noTextColorButton(){
        Button cB = (Button) findViewById(R.id.colorButton);
        cB.setText("");
    }

    /**
     * Find the index of the button pressed
     * @param view xmlview
     */
    public void buttonPressed(View view){
        initButtonGried();
        String tag = view.getTag().toString();
        int index = Integer.parseInt(tag);

        //Button button = buttonGrid.get(index);
        //changeColor(button, randomColorString());

        changeAllButtonColor();
        setColorButton();

        tappsCheck(index);
    }


    /**
     * Set the color of the colorButton (the button that show the color to press)
     */
    public void setColorButton(){
        Button colorButton = (Button) findViewById(R.id.colorButton);

        changeColor(colorButton, theColor);
        ButtonClass colorButtonClass = new ButtonClass(17, colorButton);
        colorButtonClass.setColor(theColor);

        buttonGrid.removeAll(buttonGrid);
        colorUsedList.clear();

    }

    List<Button> buttonGrid = new ArrayList<>();
    List<String> colorUsedList = new ArrayList<>();

    /**
     * Changing the color of the buttonGrid
     * @param button the button that wil be changed
     */
    public void changeColor(Button button, String color){
        try {
            GradientDrawable gd = (GradientDrawable) button.getBackground().getCurrent();
            gd.setColor(Color.parseColor(color));

            //gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
            //gd.setStroke(1, Color.parseColor("#FF0000"), 5, 6);

        }catch (Exception e){
            System.out.println(e);
            button.setBackgroundColor(Color.RED);
        }
    }

    private String theColor;

    public void changeAllButtonColor(){
        Collections.shuffle(colorList);
        int colorButtonCorrectIndex = getRandomNum(16);
        int size = 0;
        for (Button button : buttonGrid){
            changeColor(button, colorList.get(size));
            if(size == colorButtonCorrectIndex){
                theColor = colorList.get(size);
                buttonClassList.get(size).setCorrect(true);
            }
            buttonClassList.get(size).setColor(colorList.get(size));
            size++;
        }
    }

    public String getNewColor(int random, int size){
        String color = colorList.get(random);
        while (!colorUsedList.contains(color)){
            color = colorList.get(getRandomNum(size));
            colorUsedList.add(color);
        }
        return color;
    }

    public int getRandomNum(int size){
        Random ran = new Random();
        int x = ran.nextInt(size);
        return x;
    }

    /**
     * Controls the clickable
     */
    public void enableAllButtons(){
        for (Button button : buttonGrid){
            button.setEnabled(true);
        }
        Button colorButton = (Button) findViewById(R.id.colorButton);
        colorButton.setEnabled(false);
    }

    public void disableAllButtons(){
        initButtonGried();
        for (Button button : buttonGrid){
            button.setClickable(false);
        }
        //Button colorButton = (Button) findViewById(R.id.colorButton);
        //colorButton.setEnabled(false);
    }

    /**
     * Making a random hex color string
     * @return hex color string
     */
    private List<String> colorList = Arrays.asList("#00FFFF", "#000000", "#0000FF", "#FF00FF", "#808080", "#008000", "#00FF00", "#800000", "#000080", "#808000", "#800080", "#FF0000", "#C0C0C0", "#008080", "#FFFFFF", "#FFFF00");

    public String randomColorString(){
        List<String> charList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
        String colorString ="#";
        for (int i = 0; i < 6; i++){
            Random ran = new Random();
            int x = ran.nextInt(16);
            String kar = charList.get(x);
            colorString += kar;
        }
        if(colorList.contains(colorString)){
            randomColorString();
        }
        //colorList.add(colorString);
        return colorString;
    }


    /**
     * The scoring (tapps) system
     */
    private int tapps;

    public void tappsCheck(int pressed){
        TextView text = (TextView) findViewById(R.id.tapps);
        if(buttonClassList.get(pressed).isCorrect()){
            tapps++;
            buttonClassList.get(pressed).setCorrect(false);
        }else{
            if(tapps-1 >= 0){
                tapps--;
            }
        }
        String str = "" + tapps;
        text.setText(str);
    }

    public String getTapps() {
        String str = "";
        return str + tapps;
    }
    /**
     * The timer
     */
    private long startTime;
    private Timer timer;
    private int gameTime = 10;

    public void startTime(){
        Date date = new Date();
        startTime = date.getTime();
        timer = new Timer();
        timer.schedule(new countTime(), 0, 100);
    }

    public void stopTime(){
        timer.cancel();
        timer.purge();
    }

    class countTime extends TimerTask{
        TextView time = (TextView) findViewById(R.id.time);
        @Override
        public void run() {
            long nowTime = new Date().getTime();
            float x = (float) (nowTime - startTime)/1000;
            float y = gameTime - x;
            setTextTime(time,String.format("%.1f", y));
            if(y <= 0.0){
                stopTime();
                setTextTime(time, "0");
                disableAllButtons();

               // r

                runOnUiThread(new Runnable() {
                    RelativeLayout relout = (RelativeLayout) findViewById(R.id.relout);
                    @Override
                    public void run() {
                       relout.setVisibility(View.VISIBLE);
                    }
                });

                TextView gameOver = (TextView) findViewById(R.id.textGameOverTapps);

                gameOver.setText(getTapps());
                //gameOverMenu?
            }
        }
    }

    private void setTextTime(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    public void setGameTime(int gameTime) {
        if(gameTime > 0){
            this.gameTime = gameTime;
        }
    }

    public int getGameTime() {
        return gameTime;
    }

    private List<ButtonClass> buttonClassList = new ArrayList<>();
    /**
     * Making the gried of buttons and adding them to the list buttonGried
     */
    public void initButtonGried(){

        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button button10 = (Button) findViewById(R.id.button10);
        Button button11 = (Button) findViewById(R.id.button11);
        Button button12 = (Button) findViewById(R.id.button12);
        Button button13 = (Button) findViewById(R.id.button13);
        Button button14 = (Button) findViewById(R.id.button14);
        Button button15 = (Button) findViewById(R.id.button15);
        Button button16 = (Button) findViewById(R.id.button16);

        buttonGrid.add(button);
        buttonGrid.add(button2);
        buttonGrid.add(button3);
        buttonGrid.add(button4);
        buttonGrid.add(button5);
        buttonGrid.add(button6);
        buttonGrid.add(button7);
        buttonGrid.add(button8);
        buttonGrid.add(button9);
        buttonGrid.add(button10);
        buttonGrid.add(button11);
        buttonGrid.add(button12);
        buttonGrid.add(button13);
        buttonGrid.add(button14);
        buttonGrid.add(button15);
        buttonGrid.add(button16);

        int index = 0;
        for(Button btn : buttonGrid){
            buttonClassList.add(new ButtonClass(index, btn));
            index++;
        }
    }
}
