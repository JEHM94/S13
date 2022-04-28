package com.example.seccion_13_notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.textViewTitle2)
    TextView textViewTittle;
    @BindView(R.id.textViewMessage2)
    TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setIntentValues();
    }

    private void setIntentValues(){
        if (getIntent() != null){
            textViewTittle.setText(getIntent().getStringExtra("tittle"));
            textViewMessage.setText(getIntent().getStringExtra("message"));
        }
    }
}