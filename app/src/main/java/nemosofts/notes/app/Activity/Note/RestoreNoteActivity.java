package nemosofts.notes.app.Activity.Note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nemosofts.notes.app.Methods.Methods;
import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.Setting;
import nemosofts.notes.app.SharedPref.SharedPref;
import nemosofts.notes.app.database.DeleteDatabase;
import nemosofts.notes.app.database.NotesDatabase;
import nemosofts.notes.app.entities.Note;

public class RestoreNoteActivity extends AppCompatActivity {

    private Methods methods;
    private TextView inpuNoteTitle, inpuNoteSubtitle, inpuNoteText, textDeteTime, textWebURL;
    private RoundedImageView imageNote, imageNote2, imageNote3, imageNote4;
    private String setectedNoteColor;
    private View viewSubtitleIndicator;
    private LinearLayout layoutWebURL;
    private String setectedImagePath, selectedImagePath2, selectedImagePath3, selectedImagePath4;
    private static final int REQUST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUST_CODE_SELECT_IMAGE = 2;
    private AlertDialog dialogDeletNote;
    private Note alreadyAvailableNote;
    ImageView createMood;
    String Note_Mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.AppTheme2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reatore_note);

        methods = new Methods(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reatore_toolbar_note);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);
        createMood = findViewById(R.id.create_mood);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        CoordinatorLayout constraintLayout=findViewById(R.id.create_i);
        SharedPref sharedPref = new SharedPref(this);
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
        inpuNoteTitle = findViewById(R.id.reatore_inputNoteTitle);
        inpuNoteSubtitle = findViewById(R.id.reatore_inputNoteSubtitle);
        inpuNoteText = findViewById(R.id.reatore_inputNote);
        textDeteTime = findViewById(R.id.reatore_textDeteTime);
        viewSubtitleIndicator = findViewById(R.id.reatore_viewSubtitleIndicator);
        imageNote = findViewById(R.id.restore_imageNote);
        imageNote2 = findViewById(R.id.restore_imageNote2);
        imageNote3 = findViewById(R.id.restore_imageNote3);
        imageNote4 = findViewById(R.id.restore_imageNote4);
        textWebURL = findViewById(R.id.reatore_textWebURL);
        layoutWebURL = findViewById(R.id.reatore_layoutWebURL);

        textDeteTime.setText(
                new SimpleDateFormat("EEEE , dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        setectedNoteColor = "#333333";
        setectedImagePath = "";

        selectedImagePath2 = "";
        selectedImagePath3 = "";
        selectedImagePath4 = "";
        if (getIntent().getBooleanExtra("isViemOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        setSubtitleIndicatorColor();

        LinearLayout adView = findViewById(R.id.adView_reatore);
        methods.showBannerAd(adView);

    }

    private void setViewOrUpdateNote() {
        inpuNoteTitle.setText(alreadyAvailableNote.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            inpuNoteText.setText(Html.fromHtml(alreadyAvailableNote.getNoteText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            inpuNoteText.setText(Html.fromHtml(alreadyAvailableNote.getNoteText()));
        }

        inpuNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());

        textDeteTime.setText(alreadyAvailableNote.getDateTime());

        setectedNoteColor = alreadyAvailableNote.getColor();

        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.reatore_imageRemoveImage).setVisibility(View.VISIBLE);
            setectedImagePath = alreadyAvailableNote.getImagePath();
        }

        if (alreadyAvailableNote.getImagePath2() != null && !alreadyAvailableNote.getImagePath2().trim().isEmpty()) {
            imageNote2.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath2()));
            imageNote2.setVisibility(View.VISIBLE);

            selectedImagePath2 = alreadyAvailableNote.getImagePath2();
        }

        if (alreadyAvailableNote.getImagePath3() != null && !alreadyAvailableNote.getImagePath3().trim().isEmpty()) {
            imageNote3.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath3()));
            imageNote3.setVisibility(View.VISIBLE);

            selectedImagePath3 = alreadyAvailableNote.getImagePath3();
        }

        if (alreadyAvailableNote.getImagePath4() != null && !alreadyAvailableNote.getImagePath4().trim().isEmpty()) {
            imageNote4.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath4()));
            imageNote4.setVisibility(View.VISIBLE);

            selectedImagePath4 = alreadyAvailableNote.getImagePath4();
        }
        if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
            textWebURL.setText(alreadyAvailableNote.getWebLink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }

        switch (alreadyAvailableNote.getMood()) {
            case "happy":
                Note_Mood = "happy";
                createMood.setImageResource(R.drawable.ic_baseline_emoji_emotions_24);
                break;
            case "sick":
                Note_Mood = "sick";
                createMood.setImageResource(R.drawable.ic_baseline_sick_24);
                break;
            case "sad":
                Note_Mood = "sad";
                createMood.setImageResource(R.drawable.ic_round_sentiment_dissatisfied_24);
                break;
            case "angry":
                Note_Mood = "angry";
                createMood.setImageResource(R.drawable.ic_angry__2_);
                break;
            case "love":
                Note_Mood = "love";
                createMood.setImageResource(R.drawable.ic_smiley_face_love_png);
                break;
            case "tired":
                Note_Mood = "tired";
                createMood.setImageResource(R.drawable.ic__058302_200);
                break;
            case "sleepy":
                Note_Mood = "sleepy";
                createMood.setImageResource(R.drawable.ic__31790d9e03ed9a2e1481fca20fb5bad);
                break;
            case "wow":
                Note_Mood = "wow";
                createMood.setImageResource(R.drawable.ic_wow);
                break;
            case "cry":
                Note_Mood = "cry";
                createMood.setImageResource(R.drawable.ic__1vp34fycal);
                break;
            case "neutral":
                Note_Mood = "neutral";
                createMood.setImageResource(R.drawable.ic_baseline_sentiment_neutral_24);
                break;
        }

    }

    private void saveNote() {

        final Note note = new Note();
        note.setTitle(alreadyAvailableNote.getTitle());
        note.setSubtitle(alreadyAvailableNote.getSubtitle());
        note.setNoteText(alreadyAvailableNote.getNoteText());
        note.setDateTime(alreadyAvailableNote.getDateTime());
        note.setColor(setectedNoteColor);
        note.setImagePath(setectedImagePath);

        note.setImagePath2(selectedImagePath2);
        note.setImagePath3(selectedImagePath3);
        note.setImagePath4(selectedImagePath4);
        note.setMood(alreadyAvailableNote.getMood());
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWebLink(textWebURL.getText().toString());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                DeleteDatabase.getNotesDatabase(getApplicationContext()).noteDao().deletNote(alreadyAvailableNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                intent.putExtra("isNoteDeleted", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(setectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permissions Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.reatore_imageRemoveImage).setVisibility(View.VISIBLE);

                        setectedImagePath = getPathFromUri(selectedImageUri);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contenUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contenUri, null, null, null, null);

        if (cursor == null) {
            filePath = contenUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_restore:
                saveNote();
                break;
            case R.id.nav_delete:
                showDeletNoteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeletNoteDialog() {
        if (dialogDeletNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RestoreNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeletNote = builder.create();
            if (dialogDeletNote.getWindow() != null) {
                dialogDeletNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            DeleteDatabase.getNotesDatabase(getApplicationContext()).noteDao()
                                    .deletNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            dialogDeletNote.dismiss();
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(view1 -> dialogDeletNote.dismiss());
        }

        dialogDeletNote.show();
    }

}