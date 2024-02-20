package pl.madej.finansemanangerrestapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
