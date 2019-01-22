package com.example.lubna.cloverweb;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
 Context mContext;
 List<Food_Model> mData;
 Dialog mydialog;
 public RecyclerViewAdapter(Context mContext,List<Food_Model> mData)
   {
    this.mContext = mContext;
    this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_food,parent,false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        mydialog =  new Dialog(mContext);
        mydialog.setContentView(R.layout.dialog_food);
        viewHolder.item_food.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext,"Test Click"+String.valueOf(viewHolder.getAdapterPosition()),Toast.LENGTH_LONG).show();
                mydialog.show();
            }
        });
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
          myViewHolder.food.setText(mData.get(position).getName());
          myViewHolder.foodname.setText(mData.get(position).getPhone());
          myViewHolder.foodimage.setImageResource(mData.get(position).getPhoto());
          myViewHolder.buton.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v)
              {
                  AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                  alertDialog.setTitle("hi");
                  alertDialog.setMessage("this is my app");

                  alertDialog.setButton("Continue..", new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                        }
                  });
                  alertDialog.show();
              }
          });
      }
    @Override
    public int getItemCount()
    {
        return mData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView food,foodname;
        private Button buton;
        private ImageView foodimage;
        private LinearLayout item_food;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            item_food = itemView.findViewById(R.id.item_food);
            food = itemView.findViewById(R.id.name_food);
            foodname = itemView.findViewById(R.id.food_detail);
            foodimage = itemView.findViewById(R.id.image_food);
          //  buton = itemView.findViewById(R.id.recyclebuton);
        }
    }
}
