package nemosofts.notes.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import nemosofts.notes.app.Activity.Note.CreateNoteActivity;
import nemosofts.notes.app.Methods.Methods;
import nemosofts.notes.app.Methods.NavigationUtil;
import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.Setting;
import nemosofts.notes.app.SharedPref.SharedPref;
import nemosofts.notes.app.adapters.NoteAdapter;
import nemosofts.notes.app.database.NotesDatabase;
import nemosofts.notes.app.entities.Note;
import nemosofts.notes.app.listeners.InterAdListener;
import nemosofts.notes.app.listeners.NotesListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Methods methods;
    private NavigationView navigationView;
    public  static  final int REQUEST_CODE_ADD_NOTE = 1;
    public  static  final int REQUEST_CODE_UPDATE_NOTE = 2;
    private static final int REQUST_CODE_SHOW_NOTES = 3;
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private int noteClickedPostion = -1;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        methods = new Methods(this);

        methods = new Methods(this, (position, note, type) -> {
            noteClickedPostion = position;
            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            intent.putExtra("isViemOrUpdate", true);
            intent.putExtra("note", note);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
        });


        Toolbar toolbar =  findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        ImageView imageViewAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageViewAddNoteMain.setOnClickListener(view -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class),
                REQUEST_CODE_ADD_NOTE
        ));
        ConstraintLayout constraintLayout=findViewById(R.id.mainLayout);
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
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList, (note, position) -> methods.showInter(position, note, ""));
        notesRecyclerView.setAdapter(noteAdapter);
        getNotes(REQUST_CODE_SHOW_NOTES, false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noteList.size() != 0){
                    noteAdapter.searchNote(editable.toString());
                }

            }
        });

        LinearLayout adView = findViewById(R.id.adView_main);
        methods.showBannerAd(adView);

    }


    private void getNotes(final int requestCode, final  boolean isNoteDeleted){

        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUST_CODE_SHOW_NOTES){
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }else if (requestCode == REQUEST_CODE_ADD_NOTE){
                    noteList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                }else if (requestCode == REQUEST_CODE_UPDATE_NOTE){
                    noteList.remove(noteClickedPostion);
                    if (isNoteDeleted){
                        noteAdapter.notifyItemRemoved(noteClickedPostion);
                    }else {
                        noteList.add(noteClickedPostion, notes.get(noteClickedPostion));
                        noteAdapter.notifyItemChanged(noteClickedPostion);
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if (data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_set :
                NavigationUtil.SettingActivity(MainActivity.this);
                break;

            case R.id.nav_delete :
                NavigationUtil.DeleteActivity(MainActivity.this);
                break;
            case R.id.nav_theme :
                NavigationUtil.ThemeActivity(MainActivity.this);
                break;

            default :
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}