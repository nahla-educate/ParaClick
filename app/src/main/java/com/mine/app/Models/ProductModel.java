package com.mine.app.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModel implements Serializable {
            String productId,productTitle,productCategory,productBrand,productQuantity,originalPrice,productColor,
                    productDescription,productIcon,timestamp,uid;


    public ProductModel(String productId, String productTitle, String productCategory, String productBrand, String productQuantity, String originalPrice, String productColor, String productDescription, String productIcon, String timestamp, String uid) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.productQuantity = productQuantity;
        this.originalPrice = originalPrice;
        this.productColor = productColor;
        this.productDescription = productDescription;
        this.productIcon = productIcon;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public ProductModel(String productTitle, String productDescription) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
    }

    public ProductModel() {
    }


    public ProductModel(String productTitle, String productCategory, String productBrand, String productQuantity, String originalPrice, String productColor, String productDescription,  String uid) {
        this.productTitle = productTitle;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.productQuantity = productQuantity;
        this.originalPrice = originalPrice;
        this.productColor = productColor;
        this.productDescription = productDescription;
        this.uid = uid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //add last time
    int QuantityInCmd;
    double price;

    public int getQuantityInCmd() {
        return QuantityInCmd;
    }

    public void setQuantityInCmd(int QuantityInCmd) {
        this.QuantityInCmd = QuantityInCmd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
   /* private static int lastContactId = 0;

    public static ArrayList<ProductModel> createContactsList(int numContacts) {
        
        ArrayList<ProductModel> contacts = new ArrayList<ProductModel>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ProductModel("Person " + ++lastContactId, i <= numContacts / 2));
        }

        return contacts;
    }*/
}
