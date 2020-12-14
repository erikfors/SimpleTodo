package com.fors.erik.simpletodo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    private List<ListItem> itemsList;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<ListItem> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.itemsList = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //use layout inflater to inflate a view
        View todoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_recycle_item,viewGroup,false);
        //wrap it inside a view to inflate a view
        return new ViewHolder(todoView);
    }

    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //grab item at the position
        ListItem item = itemsList.get(position);
        //bind the item into the specified view holder
        viewHolder.bind(item);
    }

    //tells recycler view how many items are in the list
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout tvItem;
        TextView itemText;
        TextView dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.linearLayout);
            itemText = itemView.findViewById(R.id.text1);
            dateText = itemView.findViewById(R.id.text2);
        }

        //Update view inside of the view holder with the data
        public void bind(ListItem item) {

            itemText.setText(item.getItem());
            dateText.setText(item.getDate());
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
