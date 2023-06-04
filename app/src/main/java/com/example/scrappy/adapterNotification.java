/*
 *   Created by Abhinav Pandey on 03/06/23, 4:12 PM
 */

package com.example.scrappy;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapterNotification extends FirebaseRecyclerAdapter<buyOffers, adapterNotification.myView> {

    public adapterNotification(@NonNull FirebaseRecyclerOptions<buyOffers> options) {
        super(options);
    }

    public class myView extends RecyclerView.ViewHolder {

        TextView notification;
        ImageButton postPhoto;
        CircleImageView buyerProfile;

        public myView(@NonNull View itemView) {
            super(itemView);

            notification = itemView.findViewById(R.id.notificationTxt);
            postPhoto = itemView.findViewById(R.id.postPhoto);
            buyerProfile = itemView.findViewById(R.id.buyerProfile);

        }
    }

    @Override
    protected void onBindViewHolder(@NonNull adapterNotification.myView holder, int position, @NonNull buyOffers model) {

        if (position < getItemCount()) {
            String timeSpent = "";
            String buyerName = model.getBuyerName();

            {
                long postTimestamp = model.getTimestamp();
                long currentTime = System.currentTimeMillis();
                long timeSincePost = currentTime - postTimestamp;

                long secondsSincePost = timeSincePost / 1000;
                long minutesSincePost = secondsSincePost / 60;
                long hoursSincePost = minutesSincePost / 60;
                long daysSincePost = hoursSincePost / 24;
                long monthsSincePost = daysSincePost / 30;
                long yearsSincePost = monthsSincePost / 12;

                if (secondsSincePost < 60)
                    timeSpent = Long.toString(secondsSincePost) + " seconds ago";
                else if (minutesSincePost >= 1 && minutesSincePost < 60)
                    timeSpent = Long.toString(minutesSincePost) + " minutes ago";
                else if (hoursSincePost >= 1 && hoursSincePost < 24)
                    timeSpent = Long.toString(hoursSincePost) + " hours ago";
                else if (daysSincePost >= 1 && daysSincePost < 30)
                    timeSpent = Long.toString(daysSincePost) + " days ago";
                else if (monthsSincePost >= 1 && monthsSincePost < 12)
                    timeSpent = Long.toString(monthsSincePost) + " months ago";
                else if (yearsSincePost >= 1)
                    timeSpent = Long.toString(yearsSincePost) + " years ago";

            }


            String notificationText = buyerName + " wants to buy scrap. " + "For further clarifications, you can message " + model.getBuyerName() + ".  " + timeSpent;

            String fullString = notificationText;
            String boldString = buyerName;


            SpannableStringBuilder builder = new SpannableStringBuilder();

            edit(fullString, boldString, builder);
            editColor(fullString, timeSpent, builder);

            int index = 0;


            while (index != -1) {

                int startIndex = index;
                int endIndex = startIndex + boldString.length();

                builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                index = fullString.indexOf(boldString, endIndex);
            }

            holder.notification.setText(builder);
//            Glide.with(holder.donorProfile.getContext()).load(model.)
        }
    }

    public void edit(String fullString, String boldString, SpannableStringBuilder builder) {

        if (String.valueOf(builder).equals("")) builder.append(fullString);

        int startIndex = fullString.indexOf(boldString);
        int endIndex = startIndex + boldString.length();

        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fullString = String.valueOf(builder);
    }

    public void editColor(String fullString, String boldString, SpannableStringBuilder builder) {

        int lightGreyColor = Color.rgb(128, 128, 128);   // You can use the RGB color method to create a specific color.
        if (String.valueOf(builder).equals("")) builder.append(fullString);

        int startIndex = fullString.indexOf(boldString);
        int endIndex = startIndex + boldString.length();

        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(lightGreyColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fullString = String.valueOf(builder);
    }

    @NonNull
    @Override
    public adapterNotification.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new adapterNotification.myView(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }


}



