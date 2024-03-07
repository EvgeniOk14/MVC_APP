package com.example.daocradapi.models.cartItem;

import com.example.daocradapi.models.abstractclases.Gender;
import com.example.daocradapi.models.abstractclases.Thing;
import com.example.daocradapi.models.cart.Cart;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CartItem
{
    //region Fields
    /** идентификатор вещи, а так же является первичным ключом для таблицы cart_items **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItem_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "cartItem_gender")
    private Gender cartItem_gender;


    @Column(name = "cartItem_name")
    private String cartItem_name;

    @Column(name = "cartItem_size")
    private int cartItem_size;

    @Column(name = "cartItem_color")
    private String cartItem_color;

    @Column(name = "cartItem_price")
    private int cartItem_price;

    /** указывает количество товаров данного типа в корзине.
     Аннотации:
     @Column(name = "quantity"): Указывает на имя столбца в таблице базы данных,
     в котором хранится количество товаров.
     **/
    @Column(name = "cartItem_quantity")
    private int cartItem_quantity = 1;

    /** данное поле обозначает, что у каждого CartItem может быть только одна корзина,
     * но у одной корзины может быть много элементов CartItem.
     @JoinColumn(name = "cart_id"): Указывает на столбец в таблице cart_items,
     который является внешним ключом, связывающим эту таблицу с таблицей table_carts.
     **/
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /** данное поле представляет собой связь один к одному с таблицей things.
     *  Оно указывает на товар, который находится в данном элементе корзины.
     Аннотации:
     @OneToOne: Обозначает, что у каждого CartItem есть только один товар,
     и каждый товар может быть связан только с одним элементом CartItem.
     @JoinColumn(name = "thing_id"): Указывает на столбец в таблице cart_items,
     который является внешним ключом, связывающим эту таблицу с таблицей things.
     **/
    @OneToOne
    @JoinColumn(name = "thing_id")
    private Thing thing;
    //endRegion

    //region Constructors
    public CartItem(Integer cartItem_id, Gender cartItem_gender, String cartItem_name,
                    int cartItem_size, String cartItem_color, int cartItem_price,
                    int cartItem_quantity, Cart cart, Thing thing)
    {
        this.cartItem_id = cartItem_id;
        this.cartItem_gender = cartItem_gender;
        this.cartItem_name = cartItem_name;
        this.cartItem_size = cartItem_size;
        this.cartItem_color = cartItem_color;
        this.cartItem_price = cartItem_price;
        this.cartItem_quantity = cartItem_quantity;
        this.cart = cart;
        this.thing = thing;
    }
    public CartItem()
    {
        // default Constructor
    }
    //endregion

    //region Getters && Setters

    public Integer getCartItem_id() {
        return cartItem_id;
    }

    public void setCartItem_id(Integer cartItem_id) {
        this.cartItem_id = cartItem_id;
    }

    public Gender getCartItem_gender() {
        return cartItem_gender;
    }

    public void setCartItem_gender(Gender cartItem_gender) {
        this.cartItem_gender = cartItem_gender;
    }

    public String getCartItem_name() {
        return cartItem_name;
    }

    public void setCartItem_name(String cartItem_name) {
        this.cartItem_name = cartItem_name;
    }

    public int getCartItem_size() {
        return cartItem_size;
    }

    public void setCartItem_size(int cartItem_size) {
        this.cartItem_size = cartItem_size;
    }

    public String getCartItem_color() {
        return cartItem_color;
    }

    public void setCartItem_color(String cartItem_color) {
        this.cartItem_color = cartItem_color;
    }

    public int getCartItem_price() {
        return cartItem_price;
    }

    public void setCartItem_price(int cartItem_price) {
        this.cartItem_price = cartItem_price;
    }

    public int getCartItem_quantity() {
        return cartItem_quantity;
    }

    public void setCartItem_quantity(int cartItem_quantity) {
        this.cartItem_quantity = cartItem_quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }
    //endregion
}

