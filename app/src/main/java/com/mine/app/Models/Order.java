package com.mine.app.Models;

import static com.mine.app.Products.ListOrders.totalPriceView;

import com.mine.app.Products.ProductDetails;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Order  implements Serializable {
    private String id, status, address, street;
    private String dateOfOrder;
    private double totalPrice;
    private ArrayList<ProductModel> productArrayList;


    public Order(String id, String status, String address, String street, String dateOfOrder, double totalPrice, ArrayList<ProductModel> productArrayList) {
        this.id = id;
        this.status = status;
        this.address = address;
        this.street = street;
        this.dateOfOrder = dateOfOrder;
        this.totalPrice = totalPrice;
        this.productArrayList = productArrayList;
    }

    public Order() {
        totalPrice = 0;
        productArrayList = new ArrayList<ProductModel>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<ProductModel> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<ProductModel> productArrayList) {
        this.productArrayList = productArrayList;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ProductModel> getCartProductList() {
        return productArrayList;
    }

    public void setCartProductList(ArrayList<ProductModel> cartMedicineList) {
        this.productArrayList = cartMedicineList;
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addProduct(ProductModel product) {
        totalPrice = totalPrice + product.getPrice() * product.getQuantityInCmd();
        productArrayList.add(product);
    }

    public void addTotal(ProductModel product) {
        totalPrice = totalPrice + product.getPrice();
        totalPriceView.setText(new DecimalFormat("##.##").format(totalPrice));
    }

    public void minusTotal(ProductModel product) {
        totalPrice = totalPrice - product.getPrice();
        totalPriceView.setText(new DecimalFormat("##.##").format(totalPrice));
    }

    public void removeProduct(ProductModel product) {
        totalPrice = totalPrice - product.getPrice() * product.getQuantityInCmd();
        productArrayList.remove(product);
        if(totalPrice>0){
            totalPriceView.setText(new DecimalFormat("##.##").format(totalPrice));
        }
        else{
            totalPriceView.setText(new DecimalFormat("##.##").format(0.00));
        }

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", street='" + street + '\'' +
                ", dateOfOrder='" + dateOfOrder + '\'' +
                ", totalPrice=" + totalPrice +
                ", productArrayList=" + productArrayList +
                '}';
    }
}

