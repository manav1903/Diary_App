package nemosofts.notes.app.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nemosofts.notes.app.R;
import nemosofts.notes.app.SharedPref.Setting;
import nemosofts.notes.app.entities.Note;
import nemosofts.notes.app.listeners.NotesListener;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final NotesListener notesListener;
    private Timer timer;
    private final List<Note> noteSource;

    public NoteAdapter(Context context, List<Note> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        this.noteSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.layoutNote.setOnClickListener(view -> notesListener.onNoteClicked(notes.get(position), position));
        int step = 1;
        int final_step = 1;
        for (int i = 1; i < position + 1; i++) {
            if (i == position + 1) {
                final_step = step;
            }
            step++;
        }

        holder.item_new.setVisibility(View.GONE);
        switch (note.getMood()) {
            case "happy":
                holder.noteMood.setImageResource(R.drawable.ic_baseline_emoji_emotions_24);
                break;
            case "sick":
                holder.noteMood.setImageResource(R.drawable.ic_baseline_sick_24);
                break;
            case "sad":
                holder.noteMood.setImageResource(R.drawable.ic_round_sentiment_dissatisfied_24);
                break;
            case "angry":
                holder.noteMood.setImageResource(R.drawable.ic_angry__2_);
                break;
            case "love":
                holder.noteMood.setImageResource(R.drawable.ic_smiley_face_love_png);
                break;
            case "tired":
                holder.noteMood.setImageResource(R.drawable.ic__058302_200);
                break;
            case "sleepy":
                holder.noteMood.setImageResource(R.drawable.ic__31790d9e03ed9a2e1481fca20fb5bad);
                break;
            case "wow":
                holder.noteMood.setImageResource(R.drawable.ic_wow);
                break;
            case "cry":
                holder.noteMood.setImageResource(R.drawable.ic__1vp34fycal);
                break;
            case "neutral":
                holder.noteMood.setImageResource(R.drawable.ic_baseline_sentiment_neutral_24);
                break;
        }
        holder.textTitle.setText(note.getTitle());
        if (note.getSubtitle().trim().isEmpty()) {
            holder.textSubtitle.setVisibility(View.GONE);
        } else {
            holder.textSubtitle.setText(note.getSubtitle());
        }
//        holder.textDateTime.setText(note.getDateTime());
        String str = note.getDateTime();
        String kept = str.substring(0, str.indexOf("202"));
        String remainder = kept.substring(str.indexOf(",") + 1);
        SpannableString content = new SpannableString(remainder);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.item_date.setText(content);
        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNote.getBackground();
        if (note.getColor() != null) {
            if ("#333333".equals(note.getColor())) {
                if (Setting.Dark_Mode) {
                    holder.noteMood.setBackgroundResource(R.color.Background_Light_S);
                    gradientDrawable.setColor(Color.parseColor("#ECECEC"));
                    holder.textTitle.setTextColor(Color.parseColor("#161616"));
                    holder.textSubtitle.setTextColor(Color.parseColor("#6D6D6D"));
                    holder.textDateTime.setTextColor(Color.parseColor("#6D6D6D"));
                    holder.item_date.setTextColor(Color.parseColor("#161616"));
                } else {
                    gradientDrawable.setColor(Color.parseColor("#121212"));
                    holder.textTitle.setTextColor(Color.parseColor("#DBDBDB"));
                    holder.textSubtitle.setTextColor(Color.parseColor("#E9A0A0A0"));
                    holder.textDateTime.setTextColor(Color.parseColor("#E9A0A0A0"));
                    holder.item_date.setTextColor(Color.parseColor("#DBDBDB"));
                }
            } else {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }
        } else {
            if (Setting.Dark_Mode) {
                holder.noteMood.setBackgroundResource(R.color.Background_Light_S);
                gradientDrawable.setColor(Color.parseColor("#ECECEC"));
                holder.textTitle.setTextColor(Color.parseColor("#161616"));
                holder.textSubtitle.setTextColor(Color.parseColor("#6D6D6D"));
                holder.textDateTime.setTextColor(Color.parseColor("#6D6D6D"));
                holder.item_date.setTextColor(Color.parseColor("#161616"));
            } else {
                gradientDrawable.setColor(Color.parseColor("#121212"));
                holder.textTitle.setTextColor(Color.parseColor("#DBDBDB"));
                holder.textSubtitle.setTextColor(Color.parseColor("#E9A0A0A0"));
                holder.textDateTime.setTextColor(Color.parseColor("#E9A0A0A0"));
                holder.item_date.setTextColor(Color.parseColor("#DBDBDB"));
            }
        }

        if (note.getImagePath() != null) {
            holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            holder.imageNote.setVisibility(View.VISIBLE);
        } else {
            holder.imageNote.setVisibility(View.GONE);
        }
        if (note.getImagePath2() != null) {
            holder.imageNote2.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath2()));
            holder.imageNote2.setVisibility(View.VISIBLE);
        } else {
            holder.imageNote2.setVisibility(View.GONE);
        }
        if (note.getImagePath3() != null) {
            holder.imageNote3.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath3()));
            holder.imageNote3.setVisibility(View.VISIBLE);
        } else {
            holder.imageNote3.setVisibility(View.GONE);
        }
        if (note.getImagePath4() != null) {
            holder.imageNote4.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath4()));
            holder.imageNote4.setVisibility(View.VISIBLE);
        } else {
            holder.imageNote4.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textSubtitle, textDateTime, item_new, item_date;
        RelativeLayout layoutNote;
        RoundedImageView imageNote,imageNote2,imageNote3,imageNote4;
        ImageView noteMood;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            item_new = itemView.findViewById(R.id.item_new);
            textTitle = itemView.findViewById(R.id.item_textTitlem);
            textSubtitle = itemView.findViewById(R.id.item_textSubTitle);
            textDateTime = itemView.findViewById(R.id.item_textDateTime);
            layoutNote = itemView.findViewById(R.id.item_layoutNote);
            imageNote = itemView.findViewById(R.id.item_imageNote);
            imageNote2 = itemView.findViewById(R.id.item_imageNote2);
            imageNote3 = itemView.findViewById(R.id.item_imageNote3);
            imageNote4 = itemView.findViewById(R.id.item_imageNote4);
            noteMood = itemView.findViewById(R.id.item_mood);
            item_date = itemView.findViewById(R.id.item_date);
        }

    }

    public void searchNote(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    notes = noteSource;
                } else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
