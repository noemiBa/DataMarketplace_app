package myapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer order_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Date date;

    private String status;

    private Float totPrice;

    public Orders() {}

    public Float getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(Float totPrice) {
        this.totPrice = totPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }
}