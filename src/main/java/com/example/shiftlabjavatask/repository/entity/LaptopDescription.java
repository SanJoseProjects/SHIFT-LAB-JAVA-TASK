package com.example.shiftlabjavatask.repository.entity;

import com.example.shiftlabjavatask.repository.entity.enums.LaptopSize;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LaptopDescription {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Product id;

    @Column
    @Enumerated(EnumType.STRING)
    private LaptopSize size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaptopDescription that = (LaptopDescription) o;
        return Objects.equals(id, that.id) && size == that.size;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
