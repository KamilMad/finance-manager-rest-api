package pl.madej.finansemanangerrestapi.model;

import jakarta.persistence.*;

@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private InvestmentType type;
    private int quantity;
    private double purchasePrice;
    private double currentUserPrice;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
