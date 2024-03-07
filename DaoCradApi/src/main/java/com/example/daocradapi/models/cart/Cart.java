package com.example.daocradapi.models.cart;

import com.example.daocradapi.models.cartItem.CartItem;
import com.example.daocradapi.models.person.Person;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "table_carts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Cart
{
    //region Fields
    /** поле id корзины **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Поле person представляет связь один к одному с сущностью Person.
     *  Это означает, что каждая корзина связана с одним и только одним пользователем.
     *  @JoinColumn(name = "user_id"): Указывает на столбец в таблице базы данных,
     *  который представляет внешний ключ, связывающий корзину с пользователем.
     *  В данном случае, user_id - это имя столбца, который является внешним ключом в таблице table_carts,
     *  связывающим каждую корзину с конкретным пользователем **/
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person person;

    /** представляет коллекцию объектов CartItem, которые содержатся в данной корзине.
     * @OneToMany: Обозначает отношение один ко многим между Cart и CartItem. Это означает,
     * что одна корзина может содержать много элементов CartItem, но каждый CartItem принадлежит только одной корзине.
     * mappedBy = "cart": Указывает на поле cart в классе CartItem, которое управляет этой связью.
     * Это означает, что связь между Cart и CartItem управляется полем cart в классе CartItem.
     * cascade = CascadeType.ALL: Определяет каскадную операцию, которая будет применена к этой связи.
     * В данном случае, CascadeType.ALL означает, что все операции (создание, обновление, удаление)
     * над корзиной будут распространяться на связанные с ней CartItem. То есть, если удалить корзину, то
     * все ее элементы CartItem также будут удалены из базы данных.
     * **/
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> listOfCartItems;
    //endregion


    //region Constructors


    public Cart(Integer id, Person person, List<CartItem> listOfCartItems) {
        this.id = id;
        this.person = person;
        this.listOfCartItems = listOfCartItems;
    }

    public Cart()
    {
        // default Constructor
    }
    //endregion

    //region Getters && Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<CartItem> getListOfCartItems() {
        return listOfCartItems;
    }

    public void setListOfCartItems(List<CartItem> listOfCartItems) {
        this.listOfCartItems = listOfCartItems;
    }

    //endregion

    //region methods
    /** получение общего количества товаров в корзине **/
    public int getSumThingInCart()
    {
        return (int) listOfCartItems.stream().count();
    }

    /** получение общей суммы всех товаров в корзине **/
    public int getTotalCost()
    {
        return listOfCartItems.stream().mapToInt(CartItem::getCartItem_price).sum();
    }
    //endregion
}








//import com.example.daocradapi.models.products.NewThing;

//    @ManyToMany  // было поле в начале
//    @JoinTable(
//                name = "table_listOfThingsInOneCart",
//                joinColumns = @JoinColumn(name = "cart_id"),
//                inverseJoinColumns = @JoinColumn(name = "thing_id")
//              )
//    private List<NewThing> listOfnewThings;



