package nemosofts.notes.app.Activity.About;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nemosofts.notes.app.Constant.Constant;
import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.Setting;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView company, email, website, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        company = findViewById(R.id.company);
        email = findViewById(R.id.email);
        website =findViewById(R.id.website);
        contact = findViewById(R.id.contact);

        company.setText(Constant.company);
        email.setText(Constant.email);
        website.setText(Constant.website);
        contact.setText(Constant.contact);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}