package com.example.shiftlabjavatask.repository.entity;

import com.example.shiftlabjavatask.repository.entity.enums.DesktopPcFormFactor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DesktopDescription {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Product id;

    @Column
    @Enumerated(EnumType.STRING)
    private DesktopPcFormFactor formFactor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DesktopDescription that = (DesktopDescription) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
