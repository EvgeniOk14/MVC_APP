package com.example.daocradapi.dao.cartItem;

import com.example.daocradapi.models.cartItem.CartItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartItemDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCartItem(CartItem cartItem) {
        entityManager.persist(cartItem);
    }

    @Transactional
    public void createCartItem(CartItem cartItem) {
        entityManager.persist(cartItem);
    }

    @Transactional
    public CartItem getCartItemById(Integer id) {
        return entityManager.find(CartItem.class, id);
    }

    @Transactional
    public void updateCartItem(CartItem cartItem) {
        entityManager.merge(cartItem);
    }

    @Transactional
    public void deleteCartItem(Integer id) {
        CartItem cartItem = entityManager.find(CartItem.class, id);
        if (cartItem != null) {
            entityManager.remove(cartItem);
        }
    }

    @Transactional
    public List<CartItem> getAllCartItems() {
        return entityManager.createQuery("SELECT ci FROM CartItem ci", CartItem.class).getResultList();
    }

    @Transactional
    public List<CartItem> getCartItemsByCartId(Integer cartId) {
        return entityManager.createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId", CartItem.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Transactional
    public List<CartItem> getCartItemsByThingId(Integer thingId) {
        return entityManager.createQuery("SELECT ci FROM CartItem ci WHERE ci.thing.id = :thingId", CartItem.class)
                .setParameter("thingId", thingId)
                .getResultList();
    }

    @Transactional
    public List<CartItem> getCartItemsByQuantityGreaterThan(int quantity) {
        return entityManager.createQuery("SELECT ci FROM CartItem ci WHERE ci.cartItem_quantity > :quantity", CartItem.class)
                .setParameter("quantity", quantity)
                .getResultList();
    }
}
