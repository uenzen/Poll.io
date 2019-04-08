package cecs343.pollio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PollRecyclerAdapter extends RecyclerView.Adapter<PollRecyclerAdapter.ViewHolder> {

    private static final String TAG = "PollRecyclerAdapter";

    private ArrayList<PollItem> polls;
    private Context context;

    public PollRecyclerAdapter(Context context, ArrayList<PollItem> polls) {
        this.polls = polls;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_pollitem, viewGroup, false);
        return new ViewHolder(view);
    }

    /** This method essentially builds each PollItem view as you scroll
     * through the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //Set title text of poll
        viewHolder.titleText.setText(polls.get(i).getTitle());
        RadioGroup rg = viewHolder.radioGroup;


        rg.removeAllViews(); //Since RecyclerView literally reuses views, we need to clear the old poll options
                            // (may have been more than this poll)
        rg.setTag(R.id.tag_pollitem_index, i); //We have to set a tag of which index in the ArrayList this poll is.
                    // This lets us know what poll we are referencing when we click on it
        PollItem curPoll = polls.get(i);
        for(PollOption pi : curPoll.getOptions()) {
            //Generate each poll option individually
            RadioButton rb = new RadioButton(context);
            rb.setId(View.generateViewId());
            rb.setText(pi.getText());
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(null); // This prevents triggering onCheckChanged when you scroll by previously checked items
        if (curPoll.getVoted() >= 0){ // if this poll has been voted on, check the option we already touched
            rg.check(rg.getChildAt(curPoll.getVoted()).getId()); //Get the voted radiobutton id by index and check it
        }

        // method that defines what happens when you check one of the poll options
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int index = group.indexOfChild(group.findViewById(checkedId));
                Toast.makeText(context, index + " was selected", Toast.LENGTH_SHORT).show();
                int pollIndex = (int)group.getTag(R.id.tag_pollitem_index);
                polls.get(pollIndex).vote(index);
            }
        });
    }

    @Override
    public int getItemCount(){
        return polls.size();
    }

    // In the future we may implement multiple ViewHolders to allow for multiple different poll views
    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView titleText;
        RadioGroup radioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            radioGroup = itemView.findViewById(R.id.poll_radio);
        }
    }
}
