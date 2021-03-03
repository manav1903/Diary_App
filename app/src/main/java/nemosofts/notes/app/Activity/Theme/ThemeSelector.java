package nemosofts.notes.app.Activity.Theme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import nemosofts.notes.app.Activity.MainActivity;
import nemosofts.notes.app.Activity.Setting.SettingActivity;
import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.SharedPref;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class ThemeSelector extends AppCompatActivity {

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selector);
        ConstraintLayout constraintLayout = findViewById(R.id.themeback);
        sharedPref = new SharedPref(this);
switch (sharedPref.getTheme()){
    case "diary2":
        constraintLayout.setBackgroundResource(R.drawable.diary2);
        break;
    case "diary3":
        constraintLayout.setBackgroundResource(R.drawable.diary3);
        break;
    case "diary4":
        constraintLayout.setBackgroundResource(R.drawable.diary4);
        break;
    case "diary5":
        constraintLayout.setBackgroundResource(R.drawable.diary5);
        break;
    case "diary6":
        constraintLayout.setBackgroundResource(R.drawable.diary6);
        break;
    case "diary7":
        constraintLayout.setBackgroundResource(R.drawable.diary7);
        break;
    case "diary8":
        constraintLayout.setBackgroundResource(R.drawable.diary8);
        break;
    case "diary9":
        constraintLayout.setBackgroundResource(R.drawable.diary9);
        break;
    default:
        constraintLayout.setBackgroundResource(R.drawable.diary1);
}
        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.diary1).setOnClickListener(v -> {

            sharedPref.setTheme("diary1");
            constraintLayout.setBackgroundResource(R.drawable.diary1);
        });

        findViewById(R.id.diary2).setOnClickListener(v -> {
            sharedPref.setTheme("diary2");
            constraintLayout.setBackgroundResource(R.drawable.diary2);
        });

        findViewById(R.id.diary3).setOnClickListener(v -> {
            sharedPref.setTheme("diary3");
            constraintLayout.setBackgroundResource(R.drawable.diary3);
        });

        findViewById(R.id.diary4).setOnClickListener(v -> {
            sharedPref.setTheme("diary4");
            constraintLayout.setBackgroundResource(R.drawable.diary4);
        });

        findViewById(R.id.diary5).setOnClickListener(v -> {
            sharedPref.setTheme("diary5");
            constraintLayout.setBackgroundResource(R.drawable.diary5);
        });
        findViewById(R.id.diary6).setOnClickListener(v -> {
            sharedPref.setTheme("diary6");
            constraintLayout.setBackgroundResource(R.drawable.diary6);
        });
        findViewById(R.id.diary7).setOnClickListener(v -> {
            sharedPref.setTheme("diary7");
            constraintLayout.setBackgroundResource(R.drawable.diary7);
        });
        findViewById(R.id.diary8).setOnClickListener(v -> {
            sharedPref.setTheme("diary8");
            constraintLayout.setBackgroundResource(R.drawable.diary8);
        });
        findViewById(R.id.diary9).setOnClickListener(v -> {
            sharedPref.setTheme("diary9");
            constraintLayout.setBackgroundResource(R.drawable.diary9);
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            overridePendingTransition(0, 0);
            overridePendingTransition(0, 0);
            startActivity(new Intent(ThemeSelector.this, MainActivity.class));
            finish();
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        startActivity(new Intent(ThemeSelector.this, MainActivity.class));
        finish();
    }
}