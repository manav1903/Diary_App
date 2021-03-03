package nemosofts.notes.app.Activity.Note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nemosofts.library.NemosoftsText.NemosoftsEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nemosofts.notes.app.Methods.Methods;
import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.Setting;
import nemosofts.notes.app.SharedPref.SharedPref;
import nemosofts.notes.app.database.DeleteDatabase;
import nemosofts.notes.app.database.NotesDatabase;
import nemosofts.notes.app.entities.Note;

public class CreateNoteActivity extends AppCompatActivity {


    private Methods methods;
    private EditText inpuNoteTitle, inpuNoteSubtitle;
    private TextView textDeteTime, textWebURL;
    private RoundedImageView imageNote, imageNote2, imageNote3, imageNote4;
    private String setectedNoteColor;
    private View viewSubtitleIndicator;
    private LinearLayout layoutWebURL;
    private String setectedImagePath, selectedImagePath2, selectedImagePath3, selectedImagePath4;
    private static final int REQUST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUST_CODE_SELECT_IMAGE = 2;
    private AlertDialog dialogAddURL;
    private AlertDialog dialogDeletNote;
    private Note alreadyAvailableNote;
    private NemosoftsEditText nemosoftsEditText;
    ImageView createMood;
    private String Note_Mood = "happy";
    LinearLayout linearMiscellaneous;
    LinearLayout linearMiscellaneous2;
    LinearLayout linearMiscellaneous3;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior3;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.AppTheme2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        methods = new Methods(this);
        linearMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        linearMiscellaneous2 = findViewById(R.id.layoutMiscellaneous2);
        linearMiscellaneous3 = findViewById(R.id.layoutMiscellaneous3);
        bottomSheetBehavior = BottomSheetBehavior.from(linearMiscellaneous);
        bottomSheetBehavior2 = BottomSheetBehavior.from(linearMiscellaneous2);
        bottomSheetBehavior3 = BottomSheetBehavior.from(linearMiscellaneous3);

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
        Toolbar toolbar = findViewById(R.id.create_toolbar_note);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        inpuNoteTitle = findViewById(R.id.create_inputNoteTitle);
        inpuNoteSubtitle = findViewById(R.id.create_inputNoteSubtitle);
        textDeteTime = findViewById(R.id.create_textDeteTime);
        viewSubtitleIndicator = findViewById(R.id.create_viewSubtitleIndicator);
        imageNote = findViewById(R.id.create_imageNote);
        imageNote2 = findViewById(R.id.create_imageNote2);
        imageNote3 = findViewById(R.id.create_imageNote3);
        imageNote4 = findViewById(R.id.create_imageNote4);

        textWebURL = findViewById(R.id.create_textWebURL);
        layoutWebURL = findViewById(R.id.create_layoutWebURL);

        nemosoftsEditText = findViewById(R.id.create_inputNote);

        nemosoftsEditText.setSelection(nemosoftsEditText.getEditableText().length());
        createMood = findViewById(R.id.create_mood);
        textDeteTime.setText(
                new SimpleDateFormat("EEEE , dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        setectedNoteColor = "#56121212";
        setectedImagePath = "";
        selectedImagePath2 = "";
        selectedImagePath3 = "";
        selectedImagePath4 = "";
        if (getIntent().getBooleanExtra("isViemOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        } else {
            showMoodDialog();
        }
        findViewById(R.id.create_mood).setOnClickListener(v -> showMoodDialog());
        findViewById(R.id.create_imageRemoveWebURL).setOnClickListener(view -> {
            textWebURL.setText(null);
            layoutWebURL.setVisibility(View.GONE);
        });

        findViewById(R.id.create_imageRemoveImage).setOnClickListener(view -> {
            if (!selectedImagePath4.equals("")) {
                imageNote4.setImageBitmap(null);
                imageNote4.setVisibility(View.GONE);
                selectedImagePath4 = "";
            } else if (!selectedImagePath3.equals("")) {
                imageNote3.setImageBitmap(null);
                imageNote3.setVisibility(View.GONE);
                selectedImagePath3 = "";
            } else if (!selectedImagePath2.equals("")) {
                imageNote2.setImageBitmap(null);
                imageNote2.setVisibility(View.GONE);
                selectedImagePath2 = "";
            } else if (!setectedImagePath.equals("")) {

                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.create_imageRemoveImage).setVisibility(View.GONE);
                setectedImagePath = "";
            }
        });

        initMiscellaneous();
        setSubtitleIndicatorColor();

        LinearLayout adView = findViewById(R.id.adView_create);
        methods.showBannerAd(adView);


    }

    private void setViewOrUpdateNote() {
        inpuNoteTitle.setText(alreadyAvailableNote.getTitle());
        inpuNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        nemosoftsEditText.fromHtml(alreadyAvailableNote.getNoteText());
        textDeteTime.setText(alreadyAvailableNote.getDateTime());

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

        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);

            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
            setectedImagePath = alreadyAvailableNote.getImagePath();
        }

        if (alreadyAvailableNote.getImagePath2() != null && !alreadyAvailableNote.getImagePath2().trim().isEmpty()) {
            imageNote2.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath2()));
            imageNote2.setVisibility(View.VISIBLE);

            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath2 = alreadyAvailableNote.getImagePath2();
        }

        if (alreadyAvailableNote.getImagePath3() != null && !alreadyAvailableNote.getImagePath3().trim().isEmpty()) {
            imageNote3.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath3()));
            imageNote3.setVisibility(View.VISIBLE);

            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath3 = alreadyAvailableNote.getImagePath3();
        }

        if (alreadyAvailableNote.getImagePath4() != null && !alreadyAvailableNote.getImagePath4().trim().isEmpty()) {
            imageNote4.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath4()));
            imageNote4.setVisibility(View.VISIBLE);

            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath4 = alreadyAvailableNote.getImagePath4();
        }

        if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
            textWebURL.setText(alreadyAvailableNote.getWebLink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }
    }

    private void saveNote() {
        if (inpuNoteTitle.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note title can't be empty!", "error");
            return;
        } else if (inpuNoteSubtitle.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note Subtitle can't be empty!", "error");
            return;
        } else if (nemosoftsEditText.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note can't be empty!", "error");
            return;
        }

        final Note note = new Note();
        note.setTitle(inpuNoteTitle.getText().toString());
        note.setSubtitle(inpuNoteSubtitle.getText().toString());
        note.setNoteText(nemosoftsEditText.toHtml());
        note.setDateTime(textDeteTime.getText().toString());
        note.setColor(setectedNoteColor);
        note.setImagePath(setectedImagePath);
        note.setImagePath2(selectedImagePath2);
        note.setImagePath3(selectedImagePath3);
        note.setImagePath4(selectedImagePath4);
        note.setMood(Note_Mood);
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWebLink(textWebURL.getText().toString());
        }

        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

    private void initMiscellaneous() {

        ImageButton bold = linearMiscellaneous3.findViewById(R.id.bold), italic = linearMiscellaneous3.findViewById(R.id.italic), underline = linearMiscellaneous3.findViewById(R.id.underline),
                strikethrough = linearMiscellaneous3.findViewById(R.id.strikethrough),
                bullet = linearMiscellaneous.findViewById(R.id.bullet), quote = linearMiscellaneous3.findViewById(R.id.quote), clear = linearMiscellaneous3.findViewById(R.id.clear);
        linearMiscellaneous.findViewById(R.id.textops).setOnClickListener(view -> {
            if (bottomSheetBehavior3.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        linearMiscellaneous.findViewById(R.id.change_color).setOnClickListener(view -> {
            if (bottomSheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        final ImageView imageView1 = linearMiscellaneous2.findViewById(R.id.imageColor1);
        final ImageView imageView2 = linearMiscellaneous2.findViewById(R.id.imageColor2);
        final ImageView imageView3 = linearMiscellaneous2.findViewById(R.id.imageColor3);
        final ImageView imageView4 = linearMiscellaneous2.findViewById(R.id.imageColor4);
        final ImageView imageView5 = linearMiscellaneous2.findViewById(R.id.imageColor5);
        final ImageView imageView6 = linearMiscellaneous2.findViewById(R.id.imageColor6);
        final ImageView imageView7 = linearMiscellaneous2.findViewById(R.id.imageColor7);

        linearMiscellaneous2.findViewById(R.id.imageColor1).setOnClickListener(view -> {
            setectedNoteColor = "#333333";
            imageView1.setImageResource(R.drawable.ic_done);
            imageView2.setImageResource(0);
            imageView3.setImageResource(0);
            imageView4.setImageResource(0);
            imageView5.setImageResource(0);
            imageView6.setImageResource(0);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor2).setOnClickListener(view -> {
            setectedNoteColor = "#fdbe3b";
            imageView1.setImageResource(0);
            imageView2.setImageResource(R.drawable.ic_done);
            imageView3.setImageResource(0);
            imageView4.setImageResource(0);
            imageView5.setImageResource(0);
            imageView6.setImageResource(0);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor3).setOnClickListener(view -> {
            setectedNoteColor = "#E040FB";
            imageView1.setImageResource(0);
            imageView2.setImageResource(0);
            imageView3.setImageResource(R.drawable.ic_done);
            imageView4.setImageResource(0);
            imageView5.setImageResource(0);
            imageView6.setImageResource(0);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor4).setOnClickListener(view -> {
            setectedNoteColor = "#3a52fc";
            imageView1.setImageResource(0);
            imageView2.setImageResource(0);
            imageView3.setImageResource(0);
            imageView4.setImageResource(R.drawable.ic_done);
            imageView5.setImageResource(0);
            imageView6.setImageResource(0);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor5).setOnClickListener(view -> {
            setectedNoteColor = "#F50057";
            imageView1.setImageResource(0);
            imageView2.setImageResource(0);
            imageView3.setImageResource(0);
            imageView4.setImageResource(0);
            imageView5.setImageResource(R.drawable.ic_done);
            imageView6.setImageResource(0);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor6).setOnClickListener(view -> {
            setectedNoteColor = "#00E676";
            imageView1.setImageResource(0);
            imageView2.setImageResource(0);
            imageView3.setImageResource(0);
            imageView4.setImageResource(0);
            imageView5.setImageResource(0);
            imageView6.setImageResource(R.drawable.ic_done);
            imageView7.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        linearMiscellaneous2.findViewById(R.id.imageColor7).setOnClickListener(view -> {
            setectedNoteColor = "#FF3D00";
            imageView1.setImageResource(0);
            imageView2.setImageResource(0);
            imageView3.setImageResource(0);
            imageView4.setImageResource(0);
            imageView5.setImageResource(0);
            imageView6.setImageResource(0);
            imageView7.setImageResource(R.drawable.ic_done);
            setSubtitleIndicatorColor();
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "#333333":
                    setectedNoteColor = "#333333";
                    imageView1.setImageResource(R.drawable.ic_done);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#fdbe3b":
                    setectedNoteColor = "#fdbe3b";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(R.drawable.ic_done);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#E040FB":
                    setectedNoteColor = "#E040FB";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(R.drawable.ic_done);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#3a52fc":
                    setectedNoteColor = "#3a52fc";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(R.drawable.ic_done);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#F50057":
                    setectedNoteColor = "#F50057";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(R.drawable.ic_done);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#00E676":
                    setectedNoteColor = "#00E676";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(R.drawable.ic_done);
                    imageView7.setImageResource(0);
                    setSubtitleIndicatorColor();
                    break;
                case "#FF3D00":
                    setectedNoteColor = "#FF3D00";
                    imageView1.setImageResource(0);
                    imageView2.setImageResource(0);
                    imageView3.setImageResource(0);
                    imageView4.setImageResource(0);
                    imageView5.setImageResource(0);
                    imageView6.setImageResource(0);
                    imageView7.setImageResource(R.drawable.ic_done);
                    setSubtitleIndicatorColor();
                    break;
            }
        }

        linearMiscellaneous.findViewById(R.id.add_image).setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        CreateNoteActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUST_CODE_STORAGE_PERMISSION
                );
            } else {
                selectImage();
            }
        });

        linearMiscellaneous.findViewById(R.id.addUrl).setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showAddURLDialog();
        });

        linearMiscellaneous.findViewById(R.id.share).setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (inpuNoteTitle.getText().toString().trim().isEmpty()) {
                methods.showSnackBar("Note title can't be empty!", "error");
            } else if (inpuNoteSubtitle.getText().toString().trim().isEmpty()) {
                methods.showSnackBar("Note can't be empty!", "error");
            } else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        inpuNoteTitle.getText().toString() + "\n\n" +
                                textDeteTime.getText().toString() + "\n\n" +
                                inpuNoteSubtitle.getText().toString() + "\n\n" +
                                Html.fromHtml(nemosoftsEditText.toHtml()) + "\n" +
                                "https://play.google.com/store/apps/details?id=" + getPackageName()
                );
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        if (alreadyAvailableNote != null) {
            linearMiscellaneous.findViewById(R.id.delete).setVisibility(View.VISIBLE);
            linearMiscellaneous.findViewById(R.id.delete).setOnClickListener(view -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDeletNoteDialog();
            });
        }

        bold.setOnClickListener(v -> nemosoftsEditText.bold(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_BOLD)));
        bold.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
            return true;
        });


        italic.setOnClickListener(v -> nemosoftsEditText.italic(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_ITALIC)));

        italic.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
            return true;
        });
        underline.setOnClickListener(v -> nemosoftsEditText.underline(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_UNDERLINED)));
        underline.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
            return true;
        });
        strikethrough.setOnClickListener(v -> nemosoftsEditText.strikethrough(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_STRIKETHROUGH)));
        strikethrough.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
            return true;
        });

        bullet.setOnClickListener(v -> nemosoftsEditText.bullet(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_BULLET)));
        bullet.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
            return true;
        });


        quote.setOnClickListener(v -> nemosoftsEditText.quote(!nemosoftsEditText.contains(NemosoftsEditText.FORMAT_QUOTE)));

        quote.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
            return true;
        });

        clear.setOnClickListener(v -> nemosoftsEditText.clearFormats());

        clear.setOnLongClickListener(v -> {
            Toast.makeText(CreateNoteActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
            return true;
        });


    }

    private void showDeletNoteDialog() {
        if (dialogDeletNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note_move, (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
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
                            deleteSaveNote();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao()
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

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        switch (setectedNoteColor) {
            case "#333333":
                if (Setting.Dark_Mode) {
                    gradientDrawable.setColor(Color.parseColor("#ECECEC"));
                } else {
                    gradientDrawable.setColor(Color.parseColor("#121212"));
                }
                break;
            default:
                gradientDrawable.setColor(Color.parseColor(setectedNoteColor));
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), REQUST_CODE_SELECT_IMAGE);
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
                Uri imageUri;
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        if (setectedImagePath.equals("")) {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream inputStream;
                            try {
                                inputStream = getContentResolver().openInputStream(imageUri);

                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageNote.setImageBitmap(bitmap);
                            imageNote.setVisibility(View.VISIBLE);
                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                            setectedImagePath = getPathFromUri(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if (selectedImagePath2.equals("")) {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream inputStream;
                            try {
                                inputStream = getContentResolver().openInputStream(imageUri);

                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imageNote2.setImageBitmap(bitmap);
                                imageNote2.setVisibility(View.VISIBLE);
                                findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                                selectedImagePath2 = getPathFromUri(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if (selectedImagePath3.equals("")) {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream inputStream;
                            try {
                                inputStream = getContentResolver().openInputStream(imageUri);

                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imageNote3.setImageBitmap(bitmap);
                                imageNote3.setVisibility(View.VISIBLE);
                                findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                                selectedImagePath3 = getPathFromUri(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if (selectedImagePath4.equals("")) {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream inputStream;
                            try {
                                inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imageNote4.setImageBitmap(bitmap);
                                imageNote4.setVisibility(View.VISIBLE);
                                findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                                selectedImagePath4 = getPathFromUri(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(!selectedImagePath4.equals("")){
                            Toast.makeText(CreateNoteActivity.this, "Only 4 images are allowed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else if (data.getData() != null) {


                    Uri selectedImageUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (setectedImagePath.equals("")) {
                            imageNote.setImageBitmap(bitmap);
                            imageNote.setVisibility(View.VISIBLE);
                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                            setectedImagePath = getPathFromUri(selectedImageUri);
                        } else if (selectedImagePath2.equals("")) {
                            imageNote2.setImageBitmap(bitmap);
                            imageNote2.setVisibility(View.VISIBLE);
                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                            selectedImagePath2 = getPathFromUri(selectedImageUri);
                        } else if (selectedImagePath3.equals("")) {
                            imageNote3.setImageBitmap(bitmap);
                            imageNote3.setVisibility(View.VISIBLE);
                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                            selectedImagePath3 = getPathFromUri(selectedImageUri);
                        } else if (selectedImagePath4.equals("")) {
                            imageNote4.setImageBitmap(bitmap);
                            imageNote4.setVisibility(View.VISIBLE);
                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
                            selectedImagePath4 = getPathFromUri(selectedImageUri);
                        }
                        else {
                            Toast.makeText(CreateNoteActivity.this, "Only 4 images are allowed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

//            if (data != null) {
//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri != null) {
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        if (setectedImagePath.equals("")) {
//                            imageNote.setImageBitmap(bitmap);
//                            imageNote.setVisibility(View.VISIBLE);
//                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
//                            setectedImagePath = getPathFromUri(selectedImageUri);
//                        } else if (selectedImagePath2.equals("")) {
//                            imageNote2.setImageBitmap(bitmap);
//                            imageNote2.setVisibility(View.VISIBLE);
//                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
//                            selectedImagePath2 = getPathFromUri(selectedImageUri);
//                        } else if (selectedImagePath3.equals("")) {
//                            imageNote3.setImageBitmap(bitmap);
//                            imageNote3.setVisibility(View.VISIBLE);
//                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
//                            selectedImagePath3 = getPathFromUri(selectedImageUri);
//                        } else if (selectedImagePath4.equals("")) {
//                            imageNote4.setImageBitmap(bitmap);
//                            imageNote4.setVisibility(View.VISIBLE);
//                            findViewById(R.id.create_imageRemoveImage).setVisibility(View.VISIBLE);
//                            selectedImagePath4 = getPathFromUri(selectedImageUri);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
        }
    }

    public String getPathFromUri(Uri uri) {
        try {
            {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (uri == null) {
                return null;
            }
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String returnn = cursor.getString(column_index);
                cursor.close();
                return returnn;
            }
            return uri.getPath();
        }
    }


    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContiner)
            );
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputUrl);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(view12 -> {
                if (inputURL.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                    Toast.makeText(CreateNoteActivity.this, "Enter Valid URL", Toast.LENGTH_SHORT).show();
                } else {
                    textWebURL.setText(inputURL.getText().toString());
                    layoutWebURL.setVisibility(View.VISIBLE);
                    dialogAddURL.dismiss();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(view1 -> dialogAddURL.dismiss());
        }
        dialogAddURL.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.imageSeve:
                saveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSaveNote() {
        if (inpuNoteTitle.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note title can't be empty!", "error");
            return;
        } else if (inpuNoteSubtitle.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note Subtitle can't be empty!", "error");
            return;
        } else if (nemosoftsEditText.getText().toString().trim().isEmpty()) {
            methods.showSnackBar("Note can't be empty!", "error");
            return;
        }

        final Note note = new Note();
        note.setTitle(inpuNoteTitle.getText().toString());
        note.setSubtitle(inpuNoteSubtitle.getText().toString());
        note.setNoteText(nemosoftsEditText.toHtml());
        note.setDateTime(textDeteTime.getText().toString());
        note.setColor(setectedNoteColor);
        note.setImagePath(setectedImagePath);
        note.setImagePath2(selectedImagePath2);
        note.setImagePath3(selectedImagePath3);
        note.setImagePath4(selectedImagePath4);
        note.setMood(Note_Mood);
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWebLink(textWebURL.getText().toString());
        }

        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class DeleteSaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DeleteDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }
        new DeleteSaveNoteTask().execute();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if (bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if (alreadyAvailableNote == null) {
            if (inpuNoteTitle.getText().toString().trim().isEmpty()) {
                super.onBackPressed();
            } else if (inpuNoteSubtitle.getText().toString().trim().isEmpty()) {
                super.onBackPressed();
            } else if (nemosoftsEditText.getText().toString().trim().isEmpty()) {
                super.onBackPressed();
            } else {
                saveNoteOnBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void showMoodDialog() {
        final Dialog dialog = new Dialog(CreateNoteActivity.this);
        dialog.setContentView(R.layout.dialog_mood);
        dialog.setCancelable(true);

        dialog.show();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.happy_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_baseline_emoji_emotions_24);
            Note_Mood = "happy";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.sick_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_baseline_sick_24);
            Note_Mood = "sick";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.sad_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_round_sentiment_dissatisfied_24);
            Note_Mood = "sad";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.angry_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_angry__2_);
            Note_Mood = "angry";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.love_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_smiley_face_love_png);
            Note_Mood = "love";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.tired_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic__058302_200);
            Note_Mood = "tired";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.sleepy_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic__31790d9e03ed9a2e1481fca20fb5bad);
            Note_Mood = "sleepy";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.wow_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_wow);
            Note_Mood = "wow";
            dialog.dismiss();
        });

        dialog.findViewById(R.id.cry_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic__1vp34fycal);
            Note_Mood = "cry";
            dialog.dismiss();
        });
        dialog.findViewById(R.id.neutral_mood).setOnClickListener(v -> {
            createMood.setImageResource(R.drawable.ic_baseline_sentiment_neutral_24);
            Note_Mood = "neutral";
            dialog.dismiss();
        });
    }

    private void saveNoteOnBackPressed() {
        final Note note = new Note();
        note.setTitle(inpuNoteTitle.getText().toString());
        note.setSubtitle(inpuNoteSubtitle.getText().toString());
        note.setNoteText(nemosoftsEditText.toHtml());
        note.setDateTime(textDeteTime.getText().toString());
        note.setColor(setectedNoteColor);
        note.setImagePath(setectedImagePath);
        note.setImagePath2(selectedImagePath2);
        note.setImagePath3(selectedImagePath3);
        note.setImagePath4(selectedImagePath4);
        note.setMood(Note_Mood);
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWebLink(textWebURL.getText().toString());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

}