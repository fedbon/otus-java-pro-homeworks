package ru.otus.crm.model;


import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "phone")
public class Phone {
    @Id
    private final Long id;

    @Nonnull
    private final String number;

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceCreator
    private Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return number;
    }
}
