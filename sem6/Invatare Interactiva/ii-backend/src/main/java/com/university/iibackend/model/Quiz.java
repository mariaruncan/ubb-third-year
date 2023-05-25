package com.university.iibackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_quiz")
public class Quiz {

    @Id
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;
}