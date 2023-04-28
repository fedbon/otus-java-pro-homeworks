package ru.otus.crm.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class Phone implements Cloneable {

    @Id
    @SequenceGenerator(name = "phones_generator", sequenceName = "phones_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Override
    public Phone clone() {
        return new Phone(id, number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
