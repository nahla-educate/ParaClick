package com.mine.app.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mine.app.Auth.Login;
import com.mine.app.MainActivity;
import com.mine.app.Models.ProductModel;
import com.mine.app.Products.AddProduct;
import com.mine.app.Products.ProductDetails;
import com.mine.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public  class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

        ArrayList<ProductModel> mProductList;
        Context mContext;
    private boolean mIsAdmin;

    public ProductAdapter(ArrayList<ProductModel> productList, Context context, boolean isAdmin) {
        mProductList = productList;
        mContext = context;
        mIsAdmin = isAdmin;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new ProductViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductModel currentItem = mProductList.get(position);
        ProductModel itemToUpdate = mProductList.get(position);
        Log.d("TAG1", "Message to log"+itemToUpdate);
        Log.d("TAG2", "Message to log"+position);

        // Set the values of the views in the ViewHolder
        holder.mTextViewName.setText(currentItem.getProductTitle());
        holder.mTextViewPrice.setText(String.valueOf(currentItem.getOriginalPrice()));

        Glide.with(holder.img.getContext())
                .load(currentItem.getProductIcon())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        Log.d("TAG2", "Admin"+mIsAdmin);

        // Hide the admin options if the user is not an admin
        if (!mIsAdmin) {
            holder.mButtonEdit.setVisibility(View.GONE);
            holder.mButtonDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextViewName;
        public TextView mTextViewPrice;
        public Button mButtonEdit;
        public Button mButtonDelete, mButtonView;
        ImageView img;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.title_txt);
            mTextViewPrice = itemView.findViewById(R.id.price_tv);

            img = itemView.findViewById(R.id.prod_image);

            mButtonEdit = itemView.findViewById(R.id.btn_update);
            mButtonDelete = itemView.findViewById(R.id.btn_delete);
            mButtonView = itemView.findViewById(R.id.btn_view);



            itemView.setOnClickListener(this);
            mButtonEdit.setOnClickListener(this);
            mButtonDelete.setOnClickListener(this);
            mButtonView.setOnClickListener(this);
            Log.d("TAG2", "Message to log"+mIsAdmin);

            // Hide the admin options if the user is not an admin
            if (!mIsAdmin) {
                mButtonEdit.setVisibility(View.GONE);
                mButtonDelete.setVisibility(View.GONE);
            }



        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ProductModel item = mProductList.get(position);
            Log.d("TAG1", "Message to log"+item);
            Log.d("TAG2", "Message to log"+position);
            if (position != RecyclerView.NO_POSITION) {
                Log.d("TAG3", "Message to log"+item);
                // Log.d("TAG4", "Message to log"+itemToUpdate);
                Log.d("TAG5", "Message to log"+position);
                Log.d("TAG6", "Message to log"+item.getUid());
                Log.d("TAG7", "Message to log"+item.getProductId());
                // Get the reference of the item at the given position
                DatabaseReference itemRef = getItemReference(position);

                // Perform the desired action using the item reference
                switch (v.getId()) {
                   case R.id.btn_update:
                        // 1. Inflate the layout for the popup
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View popupView = inflater.inflate(R.layout.update_popup, null);

                        // 2. Create the AlertDialog.Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        // 3. Set the view for the dialog
                        builder.setView(popupView);

                        // 4. Set any other properties for the dialog
                        builder.setTitle("Popup Title");

                        // 5. Create and show the dialog
                        AlertDialog dialog = builder.create();

                        Log.d("TAG 00000000000001", "it"+itemRef);
                        Log.d("TAG 00000000000001", "it"+item);

                        // Code to run when OK button is clicked
                        EditText editTextDescription=popupView.findViewById(R.id.editTextNumber3);
                        EditText editTextPrice= popupView.findViewById(R.id.editTextNumber2);
                        EditText editTextBrand=popupView.findViewById(R.id.editTextNumber6);

                        EditText editTextTitle=popupView.findViewById(R.id.editTextNumber8);
                        EditText editTextQuantity= popupView.findViewById(R.id.editTextNumber5);
                        EditText editTextColor=popupView.findViewById(R.id.editTextNumber4);

                        EditText editTextCategory=popupView.findViewById(R.id.editTextNumber1);


                        // Set the initial values of the dialog fields
                        editTextDescription.setText(item.getProductDescription());
                        editTextPrice.setText(item.getOriginalPrice());
                        editTextBrand.setText(item.getProductBrand());

                        editTextQuantity.setText(item.getProductQuantity());
                        editTextTitle.setText(item.getProductTitle());
                        editTextColor.setText(item.getProductColor());
                        editTextCategory.setText(item.getProductCategory());



                        Button editButton = popupView.findViewById(R.id.button);

                        builder.show();
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("TAG 00000000000001", "yes");
                                // Code to run when edit button is clicked
                                // Obtain a reference to the Firebase database
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                                // Get a reference to the node containing the item you want to update
                                DatabaseReference itemRef = databaseRef.child("Products").child(item.getProductId());

                                // Create a map of the updated data
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("productTitle", editTextTitle.getText().toString());
                                updatedData.put("productCategory", editTextCategory.getText().toString());
                                updatedData.put("productColor", editTextColor.getText().toString());
                                updatedData.put("productDescription", editTextDescription.getText().toString());
                                updatedData.put("productQuantity", editTextQuantity.getText().toString());
                                updatedData.put("originalPrice", editTextPrice.getText().toString());
                                updatedData.put("productBrand", editTextBrand.getText().toString());

                                Log.d("TAG 00000000000001", "it"+itemRef);
                                Log.d("TAG 00000000000002", "it"+item);
                                // Update the item in the database
                                itemRef.updateChildren(updatedData);
                                Log.d("TAG 00000000000002", "done");
                                dialog.dismiss();


                            /*    Intent intent=new Intent(mContext, RecyclerView.class);
                                intent.putExtra("product",item);
                                mContext.startActivity(intent);*/



                                //.child(getRef(position).getKey()).updateChildren(map)
                                //  DatabaseReference getRef = FirebaseDatabase.getInstance().getReference().child("Products");

                                //  DatabaseReference itemRef = itemRef.getRef(position);
                                //String itemKey = getSnapshots().getSnapshot(position).getKey();
                                //String unique = reference.push().getKey();
                                // Get the reference to the current item using getRef()


                            }
                        });

                      /*  final DialogPlus dialogPlus = DialogPlus.newDialog(mContext)
                                .setContentHolder(new ViewHolder(R.layout.update_popup))
                                .setExpanded(true, 1200)
                                .create();
                        View view = dialogPlus.getHolderView();

                        EditText editTextDescription=view.findViewById(R.id.editTextNumber3);
                        EditText editTextPrice=view.findViewById(R.id.editTextNumber2);
                        EditText editTextBrand=view.findViewById(R.id.editTextNumber1);


                        // Set the initial values of the dialog fields
                        editTextDescription.setText(item.getProductTitle());
                        editTextPrice.setText(item.getOriginalPrice());
                        editTextBrand.setText(item.getProductBrand());
                        dialogPlus.show();*/


                /*last comment
                        // Get the ProductModel object for the item

                        ProductModel itemToUpdate = mProductList.get(position);
                        // Get the ProductModel object for the item
                        //ProductModel itemToUpdate = mProductList.get(position);
                        Log.d("TAG9", "Message to log"+itemToUpdate);
                        Log.d("TAG9", "Message to log"+item.getProductId());
                       // String positionn =item.getProductId();
                        // Call the updateItem method with the ProductModel object and the DatabaseReference object
                        updateItem(position, itemToUpdate);
*/                       dialog.dismiss();
                        break;
                    case R.id.btn_delete:
                        // Call the deleteItem method with the DatabaseReference object

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        builder1.setTitle("Are you sure ?");
                        builder1.setMessage("Deleted data can't be undo.");

                        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                                dialog.dismiss();
                            }


                        });
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which){
                                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();


                            }
                        });
                        builder1.show();

                        break;
                    case  R.id.btn_view:
                        Intent intent=new Intent(mContext, ProductDetails.class);
                        intent.putExtra("product",item);
                        mContext.startActivity(intent);
                    default:
                        // Open the details of the item
                        break;
                }
            }

        }
    }
    private void deleteItem(int position) {
        DatabaseReference itemRef = getItemReference(position);
        itemRef.removeValue();
        notifyItemRemoved(position);
    }
    private void updateItem(int position, ProductModel newProduct) {
        DatabaseReference itemRef = getItemReference(position);
        itemRef.setValue(newProduct);
        notifyItemChanged(position);
    }


    public DatabaseReference getItemReference(int position) {
        String productId = mProductList.get(position).getProductId();
        return FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
    }
}