package com.iandp.happy.database.manager;

import com.iandp.happy.database.DBProduct;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;

/**
 * Created on 30.10.2016.
 */

interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();

    /**
     * Insert a product into the DB
     *
     * @param product to be inserted
     */
    DBProduct insertProduct(Product product);

    /**
     * Update a product from the DB
     *
     * @param product to be updated
     */
    void updateProduct(Product product);

    /**
     * Remove a product from the DB
     *
     * @param productId to be updated
     */
    void removeProduct(Long productId);

    /**
     * @param productId of the news we want to fetch
     * @return Return a product by its id
     */
    Product getProductById(Long productId);

    /**
     * List the product from the DB
     *
     * @param lastId      ID of the last product on the page
     * @param limit       Quantity items to return
     * @return list of product
     */
    ArrayList<Product> listProduct(int lastId, int limit);

}
