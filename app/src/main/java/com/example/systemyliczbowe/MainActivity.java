package com.example.systemyliczbowe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Button[] buttons = new Button[40];
    TextView textView1, textView2;
    SeekBar seekBar1, seekBar2;
    int oldSeekBar = 2, sys1 = 2, sys2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
    }

    void setViews() {
        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "button" + (i + 1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            if (i < 38)
                buttons[i].setOnClickListener(this::write);
        }
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
    }

    void write(View v) {
        Button b = (Button)v;

        if (b.getText().equals("->"))
            remove();
        else if (b.getText().equals("="))
            calc();
        else
            textView1.append(b.getText());
    }

    void remove() {
        String text = textView1.getText().toString();
        if (!text.equals(""))
            textView1.setText(text.substring(0, text.length()-1));
    }

    void calc() {
        try {
            textView2.setText(toSystem(textView1.getText().toString(), sys1, sys2));
        }
        catch(Exception e) {
            textView2.setText(R.string.error);
        }
    }

    int getNumber(char c) {
        int index = ALPHABET.indexOf(c);
        return index == -1 ? (int)c-'0' : index+10;
    }

    String getChar(int val) {
        return String.valueOf(val).length() == 1 ? val+"" : ALPHABET.charAt(val-10)+"";
    }

    int toDecimal(String val, int sys) {
        int result = 0;
        for (int i = 0; i < val.length(); i++) {
            result += getNumber(val.charAt(i)) * (int)Math.pow(sys, val.length()-1-i);
        }
        return result;
    }

    String toSystem(String val, int sys1, int sys2) {
        if (val.equals(""))
            return "";

        StringBuilder result = new StringBuilder();
        int rest = 0, num = 0;
        if (sys1 == 10)
            num= Integer.parseInt(val);
        else
            num = toDecimal(val, sys1);

        if (sys2 == 10)
            return num+"";

        while (num != 0) {
            rest = num%sys2;
            result.append(getChar(rest));
            num /= sys2;
        }
        return result.reverse().toString();
    }

    void checkInput() {
        char[] text = textView1.getText().toString().toCharArray();
        for (char c : text) {
            if (getNumber(c) > sys1 - 1) {
                textView1.setText("");
            }
        }
    }

    void enableButtons() {
        int range = sys1 - oldSeekBar;
        if (range < 0) {
            oldSeekBar--;
            while (range != 0) {
                buttons[oldSeekBar].setBackgroundColor(0xFFAAAAAA);
                buttons[oldSeekBar].setEnabled(false);
                oldSeekBar--;
                range++;
            }
        }
        else {
            while (range != 0) {
                buttons[oldSeekBar].setBackgroundColor(0xFF0099CC);
                buttons[oldSeekBar].setEnabled(true);
                oldSeekBar++;
                range--;
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int value = i + 2;
        if (seekBar.getId() == R.id.seekBar1) {
            buttons[38].setText(String.valueOf(value));
            sys1 = value;
        }
        else {
            buttons[39].setText(String.valueOf(value));
            sys2 = value;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        oldSeekBar = sys1;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        checkInput();
        enableButtons();
    }
}