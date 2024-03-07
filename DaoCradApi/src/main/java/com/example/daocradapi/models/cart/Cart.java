package com.example.daocradapi.models.cart;

import com.example.daocradapi.models.Person;
import com.example.daocradapi.models.products.NewThing;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "table_carts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Cart
{
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person person;

    @ManyToMany
    @JoinTable(
                name = "table_listOfThingsInOneCart",
                joinColumns = @JoinColumn(name = "cart_id"),
                inverseJoinColumns = @JoinColumn(name = "thing_id")
              )
    private List<NewThing> listOfnewThings;
    //endregion


    //region Constructors
    public Cart(Person user, List<NewThing> listOfnewThings)
    {
        this.person = user;
        this.listOfnewThings = listOfnewThings;
    }
    public Cart()
    {
        //default consrtuctor
    }
    //endregion


    //region Getters & Setters
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }

    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }
    public List<NewThing> getListOfnewThings()
    {
        return listOfnewThings;
    }
    public void setListOfnewThings(List<NewThing> listOfnewThings)
    {
        this.listOfnewThings = listOfnewThings;
    }
    //endregion


    //region methods
    /** получение общего количества товаров в корзине **/
    public int getSumThingInCart()
    {
        return (int) listOfnewThings.stream().count();
    }

    /** получение общей суммы всех товаров в корзине **/
    public int getTotalCost()
    {
        return listOfnewThings.stream().mapToInt(NewThing::getThing_price).sum();
    }
    //endregion

    @Override
    public String toString()
    {
        return "Cart{" +
                "id=" + id +
                ", person=" + person +
                ", listOfnewThings=" + listOfnewThings +
                '}';
    }
}





