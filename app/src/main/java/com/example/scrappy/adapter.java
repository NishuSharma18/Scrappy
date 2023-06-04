/*
 *   Created by Abhinav Pandey on 06/05/23, 4:25 AM
 */

package com.example.scrappy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter extends RecyclerView.Adapter<adapter.PostViewHolder> {
    private List<model> postList;
    private static Button buyBtn;
    public static String sellerId="",buyerId="",postId="";

    public adapter(List<model> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        model post = postList.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView caption;
        private TextView address;
        private ViewPager2 viewPager;
        private ImagePagerAdapter imagePagerAdapter;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            caption = itemView.findViewById(R.id.caption);
            viewPager = itemView.findViewById(R.id.images);
            imagePagerAdapter = new ImagePagerAdapter();
            viewPager.setAdapter(imagePagerAdapter);
            buyBtn = itemView.findViewById(R.id.buyBtn);
        }

        public void bind(model post) {
            caption.setText(post.getCaption());
            address.setText(post.getAddress());
            sellerId = post.getUserId();
            postId=post.getPostId();
            List<String> imageUrlList = post.getImageUrl();
            imagePagerAdapter.setImageUrls(imageUrlList);
            imagePagerAdapter.notifyDataSetChanged();
        }

    }

    public static class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
        private List<String> imageUrlList;

        public void setImageUrls(List<String> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_pager, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            String imageUrl = imageUrlList.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);

            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendNotification();

                }
            });
        }

        @Override
        public int getItemCount() {
            return imageUrlList == null ? 0 : imageUrlList.size();
        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image_view);
            }
        }
    }

    public static void sendNotification() {


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth auth= FirebaseAuth.getInstance();
        buyerId= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        mDatabase.getReference("users").child("buyerId").child("userName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Get the username value from the snapshot
                        String buyerName = dataSnapshot.getValue(String.class);
                        String notificationId = mDatabase.getReference("users").child("sellerId").child("notifications").push().getKey();

                        buyOffers offer= new buyOffers(notificationId,buyerId, sellerId,postId,buyerName,System.currentTimeMillis());

                        mDatabase.getReference("users").child(sellerId).child("notifications").child(notificationId).setValue(offer);
                        Toast.makeText(buyBtn.getContext(), "A notification to buy the scrap has been sent", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
