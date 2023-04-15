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
@Table(name = "addresses")
public class Address implements Cloneable{

    @Id
    @SequenceGenerator(name = "addresses_generator", sequenceName = "addresses_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @Override
    public Address clone() {
        return new Address(id, street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
