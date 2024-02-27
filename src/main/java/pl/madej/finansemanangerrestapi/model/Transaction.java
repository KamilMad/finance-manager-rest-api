package pl.madej.finansemanangerrestapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.madej.finansemanangerrestapi.model.enums.Category;
import pl.madej.finansemanangerrestapi.model.enums.TransactionType;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    //@ManyToOne
    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
